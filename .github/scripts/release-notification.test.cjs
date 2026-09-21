const assert = require('node:assert/strict');
const { spawnSync } = require('node:child_process');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { test } = require('node:test');
const YAML = require('yaml');

const workflows = path.resolve(__dirname, '../workflows');
const workflow = YAML.parse(fs.readFileSync(path.join(workflows, 'deploy-npm.yml'), 'utf8'));
const steps = workflow.jobs['deploy-npm'].steps;
const verify = steps.find((step) => step.id === 'verify');
const prepare = steps.find((step) => step.name === 'Prepare release notification');
const send = steps.find((step) => step.with?.method === 'chat.postMessage');

function fixture(t, versions, missing = '') {
  const cwd = fs.mkdtempSync(path.join(os.tmpdir(), 'rn-notification-test-'));
  t.after(() => fs.rmSync(cwd, { recursive: true, force: true }));
  const bin = path.join(cwd, 'bin');
  fs.mkdirSync(bin);
  versions.forEach((version, index) => {
    const dir = path.join(cwd, 'libs', `package-${index}`);
    fs.mkdirSync(dir, { recursive: true });
    fs.writeFileSync(path.join(dir, 'project.json'), JSON.stringify({ targets: { deploy: {} } }));
    fs.writeFileSync(
      path.join(dir, 'package.json'),
      JSON.stringify({ name: `@rudderstack/package-${index}`, version }),
    );
  });
  // No registry calls, publishing, or Slack messages occur in these tests.
  fs.writeFileSync(path.join(bin, 'git'), '#!/bin/bash\nprintf \'{"version":"1.0.0"}\\n\'\n', {
    mode: 0o755,
  });
  fs.writeFileSync(
    path.join(bin, 'npm'),
    '#!/bin/bash\n[[ "$1" == view && "$3" == version ]] || exit 2\n[[ "$2" != "$MISSING_PACKAGE" ]]\n',
    { mode: 0o755 },
  );
  const output = path.join(cwd, 'output');
  const env = {
    ...process.env,
    PATH: `${bin}:${process.env.PATH}`,
    RUNNER_TEMP: cwd,
    GITHUB_OUTPUT: output,
    PREVIOUS_TAG: 'v2.45.0',
    MISSING_PACKAGE: missing,
    SLACK_CHANNEL_ID: 'test-channel',
    RELEASE_URL: 'https://github.com/rudderlabs/rudder-sdk-react-native/releases/tag/v2.46.0',
  };
  const run = (step) =>
    spawnSync('bash', ['--noprofile', '--norc', '-e', '-o', 'pipefail', '-c', step.run], {
      cwd,
      env,
      encoding: 'utf8',
    });
  return {
    run,
    output: () => (fs.existsSync(output) ? fs.readFileSync(output, 'utf8') : ''),
    payload: () => JSON.parse(fs.readFileSync(path.join(cwd, 'release-notification.json'), 'utf8')),
  };
}

test('one notification follows verification and requires changed packages', () => {
  assert.ok(steps.indexOf(verify) < steps.indexOf(prepare));
  assert.ok(steps.indexOf(prepare) < steps.indexOf(send));
  assert.equal(verify['continue-on-error'], undefined);
  assert.equal(prepare.if, "steps.verify.outputs.has_packages == 'true'");
  assert.equal(send.if, prepare.if);
  assert.equal(send.with['payload-file-path'], '${{ runner.temp }}/release-notification.json');
  assert.equal(send.with['payload-templated'], undefined);
  const notifications = fs
    .readdirSync(workflows)
    .filter((file) => /\.ya?ml$/.test(file))
    .flatMap((file) =>
      Object.values(YAML.parse(fs.readFileSync(path.join(workflows, file), 'utf8')).jobs),
    )
    .flatMap((job) => job.steps || [])
    .filter((step) => step.with?.method === 'chat.postMessage');
  assert.equal(notifications.length, 1);
});

for (const versions of [['3.2.1'], ['3.2.1', '1.2.0', '1.0.0']]) {
  test(`lists actual versions and direct links for ${versions.length} candidate packages`, (t) => {
    const f = fixture(t, versions);
    assert.equal(f.run(verify).status, 0);
    assert.equal(f.output(), 'has_packages=true\n');
    assert.equal(f.run(prepare).status, 0);
    const payload = f.payload();
    assert.equal(payload.channel, 'test-channel');
    assert.equal(payload.unfurl_links, false);
    versions.forEach((version, index) => {
      if (version === '1.0.0') {
        assert.ok(!payload.text.includes(`package-${index}`));
      } else {
        assert.ok(
          payload.text.includes(
            `<https://www.npmjs.com/package/@rudderstack/package-${index}/v/${version}|@rudderstack/package-${index} ${version}>`,
          ),
        );
      }
    });
    assert.ok(payload.text.includes('/releases/tag/v2.46.0|Release notes>'));
    assert.ok(!payload.text.includes('monorepo'));
  });
}

test('no changed packages produces no notification output', (t) => {
  const f = fixture(t, ['1.0.0']);
  assert.equal(f.run(verify).status, 0);
  assert.equal(f.output(), '');
});

test('one unavailable package fails verification and suppresses success output', (t) => {
  const f = fixture(t, ['3.2.1', '1.2.0'], '@rudderstack/package-1@1.2.0');
  assert.equal(f.run(verify).status, 1);
  assert.equal(f.output(), '');
});

test('verification includes already-published packages and resets its message on rerun', (t) => {
  const f = fixture(t, ['3.2.1']);
  assert.equal(f.run(verify).status, 0);
  assert.equal(f.run(verify).status, 0);
  assert.equal(f.run(prepare).status, 0);
  assert.equal(f.payload().text.match(/• /g).length, 1);
});
