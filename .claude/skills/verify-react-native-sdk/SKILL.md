---
name: verify-react-native-sdk
description: Run the full sanity-check matrix on a change to the rudderlabs/rudder-sdk-react-native repo (lint, type-check, test, lib builds, iOS build + run, Android build + run, npm audit, dry-run deploys). Use when verifying any change — dep bump, integration update, refactor, RN upgrade, PR-ready check — before merging.
allowed-tools: Bash, Read, Edit, Grep, Glob, Write
user-invocable: true
---

# Verify rudder-sdk-react-native changes

## When to use

- User asks to verify a change on `rudder-sdk-react-native` (any kind — dep bump, integration update, refactor, RN upgrade)
- User wants pre-merge confidence: "run the RN sanity checks", "is this PR ready", "verify the bump"
- User mentions the [Migrate NX workspace Notion checklist](https://www.notion.so/rudderstacks/Migrate-NX-workspace-to-the-latest-versions-ec059c07a0124785ba01846e3fe6cd6a)
- Before posting any "ready for review" status on a PR touching this repo

## Critical principles (read before doing anything)

1. **Full-wipe first, always.** Both npm and Xcode are incremental tools that silently reuse cached artifacts. Partial cleans give fake greens. The wipe is non-negotiable on a first-of-investigation run.
2. **Match CI conditions.** CI uses Xcode 26.3 and a specific JDK/Ruby/Bundler stack. Differences explain a lot of local-only failures.
3. **Never run destructive commands during verification.** `nx version` creates a commit. `nx deploy` (without `--dry-run`) publishes to npm. The OG Notion checklist mentions both, but they exist for _release_ flows, not verification.
4. **`apps/example` and `apps/expo-example` are different worlds.** The Nx workspace covers only `example`. `expo-example` is on a different RN line (Expo SDK 54 → RN 0.81.5; Expo SDK 55 → RN 0.83.x) and needs separate verification.

## Phase 0 — Pre-flight

### Confirm you're in the right repo

```bash
# Repo root sanity
test -f package.json && test -d apps/example && test -d libs/sdk && \
  grep -q '"@rudderstack/rudder-sdk-react-native-monorepo"' package.json || \
  echo "ERROR: not in rudder-sdk-react-native checkout"
```

If the user is working in a worktree, **prefer the main repo path** for verification. Worktrees at `/tmp/...` hit a known Metro projectRoot symlink trap during `build-ios-ci` (Metro can't resolve `src/main.tsx` SHA-1 because Xcode PhaseScript follows `/tmp → /private/tmp` differently than Nx does). That failure is environmental, not a real regression — but it'll waste time.

### Detect the toolchain

```bash
xcodebuild -version
clang --version | head -1
sw_vers
ruby --version          # apps/example/ios pins 3.1.4 — see Gemfile
java --version          # JDK 17 expected for Android
node --version
```

**Xcode 26.4 (Apple clang 21.0.0) caveat:** Bare RN versions < 0.83.5 transitively pin `fmt@11.0.2`, which uses `consteval` in `FMT_STRING`. Apple clang 21 rejects it with `call to consteval function ... is not a constant expression`. CI on Xcode 26.3 passes the same build. Workarounds: bump RN to 0.83.5+ (which pulls fmt 12.1.0), or downgrade local Xcode to 26.3. See [fmt#4740](https://github.com/fmtlib/fmt/issues/4740) and [react-native#55601](https://github.com/facebook/react-native/issues/55601).

### Required environment variables

`nx build-android example` requires `MOENGAGE_ANDROID_APP_ID` to be set in `apps/example/.env`. CI populates it; locally the field is empty. Use a dummy value for verification:

```bash
grep MOENGAGE_ANDROID_APP_ID apps/example/.env
# If empty, append: MOENGAGE_ANDROID_APP_ID=DUMMY_FOR_BUILD_VERIFICATION
```

### Capture the user's working state

```bash
git status -s
git stash list
```

If there are dirty files, `git stash push -u -m "pre-verify-<date>"` them so the verification runs against the committed state. Restore at the end.

## Phase 1 — Full wipe (MANDATORY for first-of-investigation)

```bash
rm -rf node_modules
rm -rf apps/example/ios/Pods apps/example/ios/Podfile.lock
rm -rf ~/Library/Developer/Xcode/DerivedData/Example-*
npm install --legacy-peer-deps   # this repo's lockfile requires --legacy-peer-deps
npx nx ensure-symlink example    # creates apps/example/node_modules symlink — required before pod install
(cd apps/example/ios && bundle exec pod install)
```

**Why every line matters:**

- `rm -rf node_modules`: `git checkout` doesn't reinstall deps. If anyone earlier in your session ran a different branch's `npm install`, on-disk packages may not match the current `package.json`. Example: an earlier Nx 22 migration in a sibling session can leave `react-native@0.79.7` in `node_modules` even when `package.json` says `0.83.1`. Without this wipe, you verify against the wrong dependency tree.
- `rm -rf Pods Podfile.lock`: stale lock + CocoaPods cache can pin pods to old commits that differ from a fresh resolve.
- `rm -rf DerivedData/Example-*`: Xcode caches per-pod `.o` files under `Build/Intermediates.noindex/Pods.build/<config>/<pod>.build/`. If a heavy pod (e.g. `fmt`) was compiled against an older toolchain, Xcode silently relinks the stale `.o` and reports `BUILD SUCCEEDED` — **masking real compile errors against the current toolchain.** This is the most insidious trap. A "Clean Build Folder" in Xcode only removes `Build/Products/.../Example.app`, not the intermediates.
- `npm install --legacy-peer-deps`: the repo has peer-dep conflicts (RN + integrations) that npm's strict resolver rejects. Always required.
- `npx nx ensure-symlink example`: creates `apps/example/node_modules` as a symlink to root `node_modules`. **Required before `pod install`** — without it, CocoaPods sees `RNRudderSdk` resolved by autolinking via `../../../node_modules/...` AND by Podfile `:path` entries via `../node_modules/...`, treats them as different sources, and aborts with "multiple dependencies with different sources for RNRudderSdk".
- `bundle exec pod install`: NOT `pod install` — Bundler ensures the right CocoaPods version (the project pins via `Gemfile`).

## Phase 2 — Static analysis (cheap, run first)

Each command should be a separate Bash call so you can capture pass/fail individually.

```bash
npx nx run-many --target=lint --skip-nx-cache
```

Expected: `Successfully ran target lint for N projects` (N = number of Nx projects in the workspace). Warnings are fine (4 warnings, 0 errors is current baseline on `develop`).

```bash
npx nx run-many --target=type-check --skip-nx-cache
```

Expected: `Successfully ran target type-check for project rudder-sdk-react-native`. Only one project has the target — the lib.

```bash
npx nx run-many --target=test --skip-nx-cache
```

Expected: `Successfully ran target test for N projects`. There's one actual test file (`apps/example/src/app/App.spec.tsx`); the other projects have no tests to run.

```bash
npm audit
npm run check:security    # wraps `npm audit --audit-level=high`
```

Expected: `npm run check:security` exits 0 (it gates on `high` and above). Moderates are fine.

**If `check:security` fails (i.e. any `high` or `critical`)**, remediate before merging. Workflow:

1. `npm audit fix` — applies safe automatic fixes via semver-compatible upgrades.
2. If that doesn't clear it, inspect the report and consider `overrides` in `package.json` to pin the offending transitive dep to a patched version. Document each override with a comment + a link to the advisory.
3. Re-run `npm run check:security` to confirm zero `high`+ findings, then re-run `npm install --legacy-peer-deps` to regenerate the lockfile cleanly.
4. **Avoid `npm audit fix --force`** unless you're prepared to absorb a major-version bump and re-verify the full matrix.

## Phase 3 — Library builds (cheap; ~30s)

```bash
npx nx run-many --target=build --exclude=example --skip-nx-cache
```

Expected: `Successfully ran target build for N projects` where N = total Nx projects minus `example`. Outputs are `dist/libs/<lib>/index.esm.js` for each lib.

## Phase 4 — Nx housekeeping (cheap)

```bash
npx nx pod-install example
npx nx sync-deps example
npx nx ensure-symlink example
npx nx affected --target=version --base=origin/develop --head=HEAD --dry-run
```

The `affected --target=version --dry-run` typically prints `No tasks were run` for a feature branch with no `version`-eligible commits — that's healthy. **Do NOT** run `nx version` itself; it creates a commit.

## Phase 5 — Bundles (medium; ~15s each)

```bash
npx nx bundle-ios example
npx nx bundle-android example
```

These produce JS bundles at `dist/apps/example/{ios,android}/main.jsbundle`. Both should succeed; failures here usually indicate a Metro config or TypeScript regression — not platform native code.

## Phase 6 — Platform builds (expensive)

### Android (~5 min)

```bash
# Ensure MOENGAGE_ANDROID_APP_ID is set in apps/example/.env (see Phase 0)
npx nx build-android example
```

Expected: `BUILD SUCCESSFUL in N s`. The Android build is generally robust to RN patch-level changes.

### iOS — simulator build (~8-10 min)

```bash
npx nx run-ios example --simulator="iPhone 17" 2>&1 | tee /tmp/ios-build.log
```

Expected:

- `success Successfully built the app`
- `success Successfully launched the app`

If the build fails at `fmt/format.cc` with consteval errors, the toolchain is Xcode 26.4 and the RN pin is < 0.83.5 — see Phase 0 caveat.

If the build reports `BUILD SUCCEEDED` but you didn't see `fmt.o` being compiled in the log:

```bash
grep -c "fmt/" /tmp/ios-build.log    # should be > 0 on a true first-of-investigation
```

Zero hits means Xcode reused a stale cached `fmt.o`. Repeat the full wipe (Phase 1) and re-run. **This is the cached-fmt.o trap that has fooled past investigations** — a partial DerivedData clean leaves intermediates intact, so `fmt` compiles on the _previous_ toolchain but the resulting binary links into the current build, giving a fake green.

**Note**: `nx run-ios` keeps Metro attached as a child process and does not exit on its own after the app launches. Once you see "Successfully launched the app" + the app's PID in `xcrun simctl listapps`, you can stop the nx process manually (or use a background task with a follow-up check). Don't conclude failure from a hung wrapper.

### iOS — CI variant (~9 min)

```bash
npx nx build-ios-ci example
```

This invokes `xcodebuild -workspace Example.xcworkspace -scheme Example -configuration Release CODE_SIGNING_ALLOWED=NO -verbose`. Expected: `** BUILD SUCCEEDED **`. If it fails with `Failed to get the SHA-1 for: /tmp/.../src/main.tsx`, you're running from a `/tmp` worktree — re-run from the canonical repo path. The `/tmp → /private/tmp` symlink trips Metro's projectRoot crawler when invoked via Xcode's PhaseScript (but not via Nx's `@nx/react-native:bundle` executor). Not a regression — env-specific.

## Phase 6.5 — Native iOS sub-track (conditional on diff)

**Activate this phase only if the diff touches any of**:

- `*.podspec` (any package)
- `Podfile` or `Podfile.lock`
- `libs/**/ios/**` (any native Obj-C / Obj-C++ / Swift code)

If your diff is JS/TS-only, or only touches Android/Kotlin, skip Phase 6.5 entirely.

### 6.5a — Podspec hygiene lint (cheap; do first)

Repo design rule: **customers must never need to edit their Podfile beyond choosing a `use_frameworks!` / `:linkage` mode**. Expo apps have no `ios/` directory at all, so any "add this line to your Podfile" instruction is a non-starter.

For each changed `*.podspec`, grep for red flags that would push work onto the consumer:

```bash
grep -nE 'modular_headers|HEADER_SEARCH_PATHS|pod_target_xcconfig|user_target_xcconfig' libs/<changed-lib>/*.podspec
```

Manually review each hit. Patterns that are red flags:

- `HEADER_SEARCH_PATHS` pointing at another pod's `${PODS_CONFIGURATION_BUILD_DIR}/<OtherPod>/...` — this path layout differs per `use_frameworks!` mode (see G9)
- Any xcconfig the consumer would have to mirror in their own Podfile to make their app build
- Forcing `:modular_headers => true` on the consumer for a transitive dependency

Patterns that are usually fine:

- `s.dependency` lines (CocoaPods resolves transitively without consumer action)
- `s.requires_arc`, `s.static_framework`, `s.module_name`, `s.platforms` — pure declarative metadata
- `s.compiler_flags` scoped to the pod itself

### 6.5b — Pod-in-isolation build (fast; ~30s)

Before the 8-10 min full-app build, verify the affected pod compiles by itself. From `apps/example/ios`:

```bash
bundle exec pod install
xcodebuild -workspace Example.xcworkspace -scheme <PodName> -configuration Debug \
  -destination 'generic/platform=iOS Simulator' build CODE_SIGNING_ALLOWED=NO 2>&1 | tail -30
```

Replace `<PodName>` with the pod under test (e.g. `rudder-sdk-react-native`, `Rudder-Sprig`, `rudder-integration-firebase-react-native`). Expected: `** BUILD SUCCEEDED **`.

If this fails but the example-app build passes, you've probably hit Xcode's cached-`.o` trap (G1) — wipe DerivedData and retry.

### 6.5c — 4-mode `use_frameworks!` matrix

Customers consume our pods in all four CocoaPods linkage modes. A podspec or native-code change can silently break three of them while the default keeps passing.

The modes (`apps/example/ios/Podfile` lines 21-23):

| Mode                   | Podfile directive                                                   |
| ---------------------- | ------------------------------------------------------------------- |
| Default (static libs)  | _(no `use_frameworks!` line)_                                       |
| Static frameworks      | `use_frameworks! :linkage => :static`                               |
| Dynamic frameworks     | `use_frameworks! :linkage => :dynamic`                              |
| Bare `use_frameworks!` | ≡ `:linkage => :dynamic` in modern CocoaPods (covered by row above) |

**Where to run the matrix**: if `apps/example` builds cleanly in all 4 modes on `develop` baseline, use it. If `apps/example` is blocked by pre-existing framework-mode rot (currently the case — see G5), **use the fresh-app harness (6.5d) instead**. Don't try to fix `apps/example`'s rot inside an unrelated PR.

For each mode:

```bash
# 1. Edit ios/Podfile to set the mode (or unset for default)
# 2. Full per-mode wipe (CocoaPods caches mode-specific resolutions)
rm -rf ios/Pods ios/Podfile.lock
rm -rf ~/Library/Developer/Xcode/DerivedData/<AppName>-*
# 3. Reinstall pods + build + launch
(cd ios && bundle exec pod install)
npx react-native run-ios --simulator='iPhone 17 Pro'
# 4. Confirm: BUILD SUCCEEDED + visible "init OK" string on screen (process-alive ≠ init-succeeded; see G8)
```

Pass criteria: all 4 modes build, launch, and show the SDK-init confirmation string.

### 6.5d — Fresh-RN-app-outside-Nx harness (escape hatch)

Use when any of:

- `apps/example` is blocked by pre-existing rot (G5 framework modes, G8 Firebase plist) and you can't get a clean signal there
- You need to confirm a failure is from your change vs. pre-existing repo state
- You want to validate the real-customer install path: packed tarball + `npm install` in a vanilla RN app

Use a non-`/tmp` path to avoid G3:

```bash
HARNESS=~/work/RNVerifyHarness
RN_VERSION=$(node -p "require('./libs/sdk/package.json').peerDependencies['react-native'].match(/[\d.]+/)[0]")

# 1. Init a fresh RN app at the RN version libs/sdk targets
npx @react-native-community/cli@latest init RNVerifyHarness --version "$RN_VERSION" --directory "$HARNESS"

# 2. Pack each changed lib from the monorepo root
for lib in libs/<changed-lib-1> libs/<changed-lib-2>; do
  (cd "$lib" && npm pack --pack-destination "$HARNESS")
done

# 3. Install tarballs in the fresh app
cd "$HARNESS"
npm install ./*.tgz

# 4. (Optional) If you have unreleased changes to rudder-sdk-ios or rudder-sdk-android,
#    point at them locally:
#      ios/Podfile:    pod 'Rudder', :path => '/abs/path/to/rudder-sdk-ios'
#      android/build.gradle: implementation project(':rudder-sdk-android-local')
```

Minimal `App.tsx` — render a visible string after `setup` resolves so you can tell init succeeded:

```tsx
import React, { useEffect, useState } from 'react';
import { Text, View } from 'react-native';
import rudderClient from '@rudderstack/rudder-sdk-react-native';
// import any factories under test, e.g.: import sprig from '@rudderstack/rudder-integration-sprig-react-native';

export default function App() {
  const [status, setStatus] = useState('initializing...');
  useEffect(() => {
    rudderClient
      .setup('TEST_WRITE_KEY', {
        dataPlaneUrl: 'https://hosted.rudderlabs.com',
        withFactories: [
          /* sprig */
        ],
      })
      .then(() => setStatus('Rudder init OK'))
      .catch((e) => setStatus('Rudder init FAILED: ' + e.message));
  }, []);
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>{status}</Text>
    </View>
  );
}
```

Now run the 4-mode matrix from 6.5c in this harness. Pass criteria for each mode: `BUILD SUCCEEDED` + simulator screenshot shows "Rudder init OK".

Keep the harness directory around for reviewers — note its path in the PR description so anyone can reproduce.

## Phase 7 — End-to-end launch tests (recommended; need a booted sim + emulator)

Phase 6 only proves the app _builds_. This phase proves it _launches, renders, and initialises the SDK at runtime_ — which is what historical RN-upgrade PRs in this repo (e.g. #540) ticked off as their final test plan item. Run both unless you have a specific reason to skip.

### Prerequisites

```bash
# iOS — boot a sim (or pick an already-booted one)
xcrun simctl list devices booted          # any booted iPhone with iOS matching your Xcode is fine

# Android — boot an emulator
emulator -list-avds                        # pick one
emulator -avd <name> -no-snapshot-save -no-boot-anim &
adb wait-for-device
```

Also ensure Metro is running before launch (Phase 4 leaves it running via `nx run-ios`; if you killed that, start it manually):

```bash
(cd apps/example && nohup npx react-native start --reset-cache > /tmp/metro.log 2>&1 &)
curl -sS http://localhost:8081/status  # expect "packager-status:running"
```

For Android, also wire the emulator to Metro:

```bash
adb reverse tcp:8081 tcp:8081
```

Without `adb reverse`, the emulator can't reach `localhost:8081` and you'll see a red "Unable to load script" screen.

### Launch via the standard RN CLI (not Nx)

Use `npx react-native run-ios` / `run-android` directly — they handle install + launch + Metro wiring more reliably than `nx run-ios`/`nx run-android` (which has had path / port quirks in this monorepo). Run them from `apps/example/`:

```bash
cd apps/example
npx react-native run-ios --simulator='iPhone 17 Pro' --no-packager   # Metro is already running
npx react-native run-android --no-packager
cd -
```

Expected: both report `Successfully launched the app` / `BUILD SUCCESSFUL ... Installed on 1 device`.

### Verification — UI render AND SDK init (both required)

A "process alive" check is **not** enough — the example app can have an alive PID but a blank React root view (see G8). You need:

**Android:**

```bash
# 1. Screenshot — confirm the example UI is showing
adb shell screencap -p /sdcard/screen.png && adb pull /sdcard/screen.png /tmp/android-app.png
# Expected: "Hello there," header + a long list of event buttons.

# 2. Logcat — confirm SDK initialised
adb logcat -d --pid=$(adb shell pidof com.example) | grep -E 'RudderSDK|fetchEventsFromDB|cloudModeProcessor' | head -10
# Expected: lines like `RudderSDK: Debug: CloudModeManager: cloudModeProcessor: ...`
```

**iOS:**

```bash
SIM_ID=$(xcrun simctl list devices booted | grep -oE '[0-9A-F-]{36}' | head -1)
BUNDLE_ID=org.reactjs.native.example.Example    # the default; confirm from apps/example/ios/Example/Info.plist if customised

# 1. Screenshot — confirm the example UI is showing (NOT just the LaunchScreen.storyboard)
xcrun simctl io $SIM_ID screenshot /tmp/ios-app.png
# Expected: same "Hello there," UI as Android.
# WARNING: If you see "Example / Powered by React Native" centered, that's the iOS launch storyboard,
# NOT the React-rendered UI. The bundle hasn't taken over yet — wait longer or see G8.

# 2. System log — confirm SDK initialised
xcrun simctl spawn $SIM_ID log show --last 60s --predicate "processImagePath CONTAINS 'Example.app'" | grep -iE 'rudder|firebase|fatal'
# Expected: Rudder init messages. Watch for `+[FIRApp configure]` exceptions (see G8).
```

If either platform fails to render or initialise the SDK, **do not conclude this is a regression from your change** until you've checked:

- The same launch on `develop` baseline (does it have the same problem?)
- The known gotchas G8 (iOS Firebase plist) and G5 (use_frameworks)
- `apps/example/.env` actually contains a working `TEST_WRITE_KEY` (an empty value silently breaks `rc.setup()`)

## Phase 8 — expo-example (manual; not in Nx workspace)

`apps/expo-example` has no `project.json` so all `nx run-many` calls above silently skip it. It's on Expo SDK 54 / RN 0.81.5 — a different RN line. Its `Podfile.lock` may not even mention `fmt` (Expo's prebuilt RN binaries often bypass source compilation of fmt/folly/glog). To verify:

```bash
cd apps/expo-example
npm install --legacy-peer-deps
(cd ios && bundle exec pod install)
npx expo run:ios       # iOS simulator
npx expo run:android   # Android emulator
```

If the user's change does not affect `apps/expo-example/` directly (e.g. they only touched `libs/sdk` or `apps/example`), expo-example verification can be deferred or skipped — but call it out explicitly in the report so the user makes the call.

## Phase 9 — Dry-run deploys (informational; expected to "fail")

These commands all "fail" with `version X.Y.Z already published` — that's correct npm registry behaviour for a `--dry-run` of an already-released version. They confirm the deploy pipeline machinery still parses correctly.

To get the current list of deployable packages, derive it from the workspace (don't hard-code — new integrations get added):

```bash
# All Nx projects with a `deploy` target:
npx nx show projects --with-target=deploy
```

Then for each:

```bash
npx nx deploy <project-name> --dry-run
```

A "fail" here is **only** informative if it's NOT the "already published" error — e.g. an auth issue, an Nx-target-not-found error, or a missing build artifact. Differentiate clearly when reporting.

Skip this phase entirely unless the change touches deploy-flow files (`scripts/deploy/*`, `project.json` deploy targets, npm publish config, etc.).

## Commands you must NEVER run as part of verification

| Command                               | Why not                                                                                            |
| ------------------------------------- | -------------------------------------------------------------------------------------------------- |
| `nx version`                          | Generates a changelog AND **creates a new commit on your branch**. Only run during a release flow. |
| `nx deploy <pkg>` without `--dry-run` | **Publishes to npm.** Only run during a release flow.                                              |
| `git stash drop` without confirming   | The user's stashed working state may contain unfinished work.                                      |
| `rm -rf .git/objects/*`               | Don't ask.                                                                                         |

## Known gotchas (the embedded session knowledge)

### G1. Cached `fmt.o` fakes a green build

**Symptom**: `BUILD SUCCEEDED` on Xcode 26.4 even though `fmt@11.0.2` should fail.
**Cause**: Xcode reused a stale `fmt.o` from `Build/Intermediates.noindex/Pods.build/<config>/fmt.build/`. The pod's `.o` was compiled by an older toolchain (or pre-Xcode-26.4 install) and silently relinked.
**Fix**: Full DerivedData wipe, not just `Build/Products/.../Example.app` deletion.
**Verify after a build**: `grep -c "fmt/" <build.log>` — should be > 0 if `fmt` was actually compiled this run.

### G2. node_modules drift after `git checkout`

**Symptom**: `package.json` says one RN version, but `node_modules/react-native/package.json` shows another.
**Cause**: `git checkout` doesn't reinstall deps. A previous session's `npm install` against a different branch leaves the wrong tree on disk.
**Fix**: `rm -rf node_modules && npm install --legacy-peer-deps` whenever switching branches.
**Detect**: `python3 -c "import json; print(json.load(open('node_modules/react-native/package.json'))['version'])"` and compare to `grep '"react-native":' package.json`.

### G3. `/tmp` worktree breaks `build-ios-ci`

**Symptom**: `build-ios-ci` fails with `Error: Failed to get the SHA-1 for: /tmp/.../src/main.tsx`. Other Metro-based targets (bundle-ios, run-ios) work fine in the same worktree.
**Cause**: macOS `/tmp` is a symlink to `/private/tmp`. Xcode's PhaseScript invokes Metro with a `cwd` that resolves symlinks differently than `@nx/react-native:bundle` does. Metro's haste-map then doesn't watch the entry file.
**Fix**: Run verification from the canonical (non-`/tmp`) repo path. Worktrees under `~/work/...` are fine.

### G4. `MOENGAGE_ANDROID_APP_ID` env gap

**Symptom**: `nx build-android example` fails at `:app:compileDebugKotlin` with `MOENGAGE_ANDROID_APP_ID` unresolved BuildConfig reference.
**Cause**: `apps/example/android/app/src/main/java/com/example/MainApplication.kt:30` reads a BuildConfig field that requires the env var at build time. CI populates it via `apps/example/.env`; locally that field is empty.
**Fix**: Set `MOENGAGE_ANDROID_APP_ID=DUMMY_FOR_BUILD_VERIFICATION` in `apps/example/.env` for verification purposes. Don't commit it.

### G5. `use_frameworks!` is already broken in `apps/example` (pre-existing, not your bug)

**Symptom**: Enabling `use_frameworks!` in `apps/example/ios/Podfile` causes either fmt consteval errors (on pre-RN-0.83.5) or `Multiple commands produce ...ReactCommon/.../scrollview/*.h` errors (on RN 0.83.5+).
**Cause**: Long-running RN + CocoaPods frameworks-mode + new-arch codegen header-collision issue. Documented in [facebook/react-native#34472](https://github.com/facebook/react-native/issues/34472). The [#52977 reporter's fix](https://github.com/facebook/react-native/issues/52977) (`nx sync-deps` after a fresh install) does NOT resolve our variant — the duplicate registration originates inside a single `React-FabricComponents` target, not from Nx node_modules hoisting.
**Status**: Pre-existing in `apps/example`; commented-out by default. Don't assume your change regressed it without testing pre-bump baseline.
**Important**: This applies to `apps/example` only. If your change touches `*.podspec` or `libs/**/ios/**`, you DO still need to verify the 4-mode matrix — just do it in the fresh-app harness (Phase 6.5d), not `apps/example`. "G5 says skip" is not a free pass to skip framework-mode verification for podspec/native-iOS changes.

### G6. `nx affected` from a fresh single-branch clone

**Symptom**: `nx affected --base=origin/develop` errors with "unknown revision".
**Cause**: Fresh clones default to fetching only the current branch. `origin/develop` ref doesn't exist locally.
**Fix**: `git remote set-branches --add origin develop && git fetch origin develop`.

### G7. `xcpretty` ruby version mismatch

**Symptom**: `nx build-ios example` fails inside the xcpretty pipe with `xcpretty: command not found`.
**Cause**: `xcpretty` is installed in the system Ruby (e.g. 2.7.6) but `apps/example/ios/.ruby-version` pins Ruby 3.1.4 where xcpretty isn't installed.
**Fix**: `(cd apps/example/ios && gem install xcpretty)` under the pinned Ruby.

### G8. iOS example launches but renders blank (Firebase plist bundle-ID mismatch)

**Symptom**: `npx react-native run-ios` reports "Successfully launched the app", the process is alive, Metro serves the JS bundle successfully — but the simulator stays on the `LaunchScreen.storyboard` ("Example / Powered by React Native") or shows a blank white screen. No crash report in `~/Library/Logs/DiagnosticReports`.
**Cause**: `apps/example/ios/Example/GoogleService-Info.plist` has `BUNDLE_ID = com.rudderstack.integration.test.firebase.ios`, but the app's actual bundle ID at runtime is `org.reactjs.native.example.Example` (the default `$(PRODUCT_BUNDLE_IDENTIFIER)` value used for simulator Debug builds). When the Rudder Firebase factory triggers `+[FIRApp configure]` during JS init, Firebase 12.x detects the mismatch and aborts — React's root view fails to attach.
**How to confirm**:

```bash
/usr/libexec/PlistBuddy -c "Print CFBundleIdentifier" apps/example/ios/Example/Info.plist
# → $(PRODUCT_BUNDLE_IDENTIFIER)  ← resolves to org.reactjs.native.example.Example at build time

/usr/libexec/PlistBuddy -c "Print :BUNDLE_ID" apps/example/ios/Example/GoogleService-Info.plist
# → com.rudderstack.integration.test.firebase.ios  ← mismatch
```

**Workaround for verification only** (do NOT commit): comment out the `firebase,` entry in the `withFactories` array in `apps/example/src/app/App.tsx`. RN will then render past the launch screen. There may be a _second_ runtime issue downstream (Singular/AppCenter/Braze under x86_64-Rosetta sim per G5) that still blocks full UI render — fixing both is out of scope for any single feature PR.
**Status**: Pre-existing config rot. The plist hasn't been refreshed for the current bundle ID. **Do not treat a blank-screen iOS launch as a regression from your PR** unless you've confirmed `develop` itself launches cleanly on the same sim (it doesn't, at time of writing).

### G9. Consuming a Swift-backed pod from an Obj-C++ (`.mm`) shim

**Symptom**: An `.mm` file that needs to call into a Swift class from another pod (one exposed via `@objcMembers` or `@objc(Name)`) compiles in default static-libs mode but fails in one or more `use_frameworks!` modes — and every "obvious" `#import` fix shifts the failure to a different mode.

**The traps** (each looks like the right fix; each breaks somewhere):

| Attempt                                                                                                        | Works in                     | Fails in                                                                                                                                                                                                    |
| -------------------------------------------------------------------------------------------------------------- | ---------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `#import "X-Swift.h"` + `HEADER_SEARCH_PATHS = "${PODS_CONFIGURATION_BUILD_DIR}/X/Swift Compatibility Header"` | default (static libs)        | both framework modes — the file lives inside `X.framework/Headers/` there, not at that flat path                                                                                                            |
| `#import <X/X-Swift.h>` (angle-bracket / modular form)                                                         | framework modes              | default mode — no umbrella header exists in static-libs layout                                                                                                                                              |
| `@import X;` in a `.m` file                                                                                    | all 4 modes, if file is `.m` | fails in `.mm` without `-fcxx-modules`; enabling `-fcxx-modules` then breaks codegen TurboModule headers (`facebook::react::TurboModule` resolution, `jsi/jsi.h` semantics, `ObjCTurboModule` declarations) |

**The working pattern — runtime dispatch via `NSClassFromString` + `objc_msgSend`**:

```objc
#import <objc/message.h>

+ (id)factoryInstance {
    // Try the bare Obj-C class name first (set via @objc(Name) or @objcMembers in Swift).
    Class cls = NSClassFromString(@"<SwiftClassName>");
    // Fall back to the Swift-module-qualified runtime name.
    if (!cls) cls = NSClassFromString(@"<SwiftModuleName>.<SwiftClassName>");
    if (!cls) return nil;
    SEL initSel = NSSelectorFromString(@"instance");  // or your factory's entry point
    if (![cls respondsToSelector:initSel]) return nil;
    return ((id (*)(Class, SEL))objc_msgSend)(cls, initSel);
}

// Calling an instance method with arguments:
SEL sel = NSSelectorFromString(@"setViewController:");
((void (*)(id, SEL, UIViewController *))objc_msgSend)(factory, sel, vc);
```

**Why this works**: no compile-time dependency on the Swift pod's generated header layout, so the build is identical across all 4 `use_frameworks!` modes. The trade-off is loss of compile-time symbol checking — accept it; runtime dispatch is the only pattern that works across all modes without forcing consumer Podfile edits (which violates the 6.5a hygiene rule).

**When to reach for this**: any time an `.mm` shim in this repo needs to call into a Swift-backed third-party pod. The pattern generalizes — it's not specific to any one integration.

## Reporting format

After running, produce a Markdown table with per-phase status. Match this shape:

```markdown
## Verification result

| Phase | Check                                                            | Status | Notes                                                                                                                  |
| ----- | ---------------------------------------------------------------- | ------ | ---------------------------------------------------------------------------------------------------------------------- |
| 0     | Pre-flight (Xcode 26.4, JDK 17, Ruby 3.1.4)                      | ✅     |                                                                                                                        |
| 1     | Full wipe + reinstall                                            | ✅     |                                                                                                                        |
| 2     | nx lint                                                          | ✅     | 4 warnings, 0 errors                                                                                                   |
| 2     | nx type-check                                                    | ✅     |                                                                                                                        |
| 2     | nx test                                                          | ✅     | 1 test, 1 passed                                                                                                       |
| 2     | npm audit                                                        | ✅     | 0 high/critical                                                                                                        |
| 3     | Lib builds                                                       | ✅     |                                                                                                                        |
| 4     | nx pod-install / sync-deps / ensure-symlink / affected --dry-run | ✅     |                                                                                                                        |
| 5     | Bundle iOS + Android                                             | ✅     |                                                                                                                        |
| 6     | build-android                                                    | ✅     | BUILD SUCCESSFUL in 5m 43s                                                                                             |
| 6     | build-ios                                                        | ✅     | xcodebuild BUILD SUCCEEDED on Xcode 26.4                                                                               |
| 6     | build-ios-ci                                                     | ✅     | BUILD SUCCEEDED                                                                                                        |
| 6.5a  | Podspec hygiene lint                                             | ✅     | No new `HEADER_SEARCH_PATHS` / `modular_headers` requirements introduced                                               |
| 6.5b  | Pod-in-isolation build (`xcodebuild -scheme <PodName>`)          | ✅     | BUILD SUCCEEDED                                                                                                        |
| 6.5c  | 4-mode `use_frameworks!` matrix (in fresh-app harness)           | ✅     | default / static / dynamic all build, launch, render "Rudder init OK". Bare ≡ dynamic.                                 |
| 6.5d  | Fresh-app harness path                                           | ℹ️     | `~/work/RNVerifyHarness` — kept for reviewer reproduction                                                              |
| 7     | run-android (Pixel_9a) — UI render + RudderSDK init in logcat    | ✅     | "Hello there" rendered; `CloudModeManager` logs present                                                                |
| 7     | run-ios (iPhone 17 Pro / iOS 26.4) — UI render + SDK init        | ⚠️     | Blank screen; pre-existing G8 (Firebase plist BUNDLE_ID mismatch). Reproduces on `develop` baseline. Not a regression. |
| 8     | expo-example                                                     | ⏭️     | Skipped — change doesn't touch it                                                                                      |
| 9     | dry-run deploys                                                  | ⏭️     | Skipped — no deploy-flow files touched                                                                                 |

**Conclusion**: <ready-for-review | needs-fix | needs-manual-verification>
```

Use ✅ for pass, ❌ for fail, ⚠️ for environmental/known-issue fail (with reasoning), ⏭️ for intentionally skipped, ℹ️ for informational rows (paths, versions, etc.).

For Phase 6.5 rows specifically: if the diff doesn't touch `*.podspec` / `Podfile` / `libs/**/ios/**`, omit them entirely (or collapse to a single `6.5 | Native iOS sub-track | ⏭️ | Not triggered by diff scope` row).

When a check fails, classify it:

- **Real regression** — must fix before merge. Block PR.
- **Local-only environment failure** — CI on Xcode 26.3 expected to pass. Don't block; note in PR description.
- **Pre-existing condition** — broken on `develop` too. File a separate ticket, don't block.
- **Manual-only verification needed** — e.g. run-target failures that need devices. Note in PR description; reviewer can run manually.
- **`apps/example` rot masks the signal** — failure could be either your change or pre-existing example-app rot (G5 framework modes, G8 Firebase plist, G4 MoEngage env). Don't classify further until you re-run the failing check in the fresh-app harness (Phase 6.5d). If the harness passes, downgrade to "pre-existing condition" with a note pointing at the harness output. If it fails there too, escalate to "real regression."

## Restore the user's state at the end

```bash
git checkout -- <any-files-you-modified-for-verification>   # e.g. apps/example/.env
git stash pop                                               # if you stashed in Phase 0
```

If pod-install side-effects modified `apps/example/ios/Example.xcodeproj/project.pbxproj` or similar uncommitted files, tell the user — they may want to `git checkout --` those too.

## References

- [Migrate NX workspace to the latest versions (OG Notion checklist)](https://www.notion.so/rudderstacks/Migrate-NX-workspace-to-the-latest-versions-ec059c07a0124785ba01846e3fe6cd6a)
- [React Native Upgrade Helper](https://react-native-community.github.io/upgrade-helper/) — when bumping RN version itself
- [fmt#4740](https://github.com/fmtlib/fmt/issues/4740) — Xcode 26.4 consteval fix
- [react-native#55601](https://github.com/facebook/react-native/issues/55601) — RN's fmt bump to 12.1.0
