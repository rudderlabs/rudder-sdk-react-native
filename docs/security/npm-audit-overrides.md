# npm audit overrides

The repository root is a private development workspace. Its dependency tree includes
build, test, lint, release, and example-app tooling that is not declared by the
published packages under `libs/`.

## Why these overrides exist

On 2026-07-31, `npm audit --audit-level=high` began failing for the lockfile used by
PR #668 even though the same lockfile passed on 2026-07-16. The difference was newly
published vulnerability data, not a dependency change. `npm audit` checks the current
registry advisory database each time it runs.

The compatible upgrades below remove all high and critical findings without using
`npm audit fix --force` or upgrading Nx across major versions:

| Package | Pinned version | Advisory |
| --- | --- | --- |
| `adm-zip` | `0.6.0` | [GHSA-xcpc-8h2w-3j85](https://github.com/advisories/GHSA-xcpc-8h2w-3j85) |
| `axios` | `1.19.0` | [GHSA-gcfj-64vw-6mp9](https://github.com/advisories/GHSA-gcfj-64vw-6mp9) and related Axios advisories fixed in 1.18.0+ |
| `brace-expansion` | `5.0.9` | [GHSA-3jxr-9vmj-r5cp](https://github.com/advisories/GHSA-3jxr-9vmj-r5cp), [GHSA-mh99-v99m-4gvg](https://github.com/advisories/GHSA-mh99-v99m-4gvg) |
| `fast-uri` | `3.1.5` | [GHSA-v2hh-gcrm-f6hx](https://github.com/advisories/GHSA-v2hh-gcrm-f6hx), [GHSA-4c8g-83qw-93j6](https://github.com/advisories/GHSA-4c8g-83qw-93j6) |
| `immutable` | `5.1.9` | [GHSA-v56q-mh7h-f735](https://github.com/advisories/GHSA-v56q-mh7h-f735), [GHSA-xvcm-6775-5m9r](https://github.com/advisories/GHSA-xvcm-6775-5m9r) |
| `js-yaml` | `4.3.1` | [GHSA-52cp-r559-cp3m](https://github.com/advisories/GHSA-52cp-r559-cp3m) |
| `postcss` | `8.5.25` | [GHSA-r28c-9q8g-f849](https://github.com/advisories/GHSA-r28c-9q8g-f849) |
| `shell-quote` | `1.10.0` | [GHSA-395f-4hp3-45gv](https://github.com/advisories/GHSA-395f-4hp3-45gv) |
| `svgo` | `2.8.3` and `3.3.4` | [GHSA-2p49-hgcm-8545](https://github.com/advisories/GHSA-2p49-hgcm-8545) |

SVGO is intentionally overridden by vulnerable version range. The dependency tree
contains both SVGO 2 and SVGO 3 consumers, so forcing one major version globally
would be less safe than applying the patched release within each existing major.

## Impact and maintenance

- These packages remain in the private workspace toolchain; published SDK package
  manifests and public APIs are unchanged.
- Overrides can expose compatibility assumptions hidden in upstream dependency
  ranges. Run the lint, type-check, test, library-build, bundle, and native CI checks
  after changing them.
- `npm audit` still reports moderate findings in Nx and development servers. They do
  not fail the repository's configured `high` threshold. Fixing the Nx advisory
  requires a separately verified major migration; do not use `npm audit fix --force`
  as a shortcut.
- Remove an override when every upstream consumer accepts a patched version on its
  own, then regenerate `package-lock.json` and rerun the verification matrix.

## References

- [npm audit](https://docs.npmjs.com/cli/v11/commands/npm-audit)
- [npm package overrides](https://docs.npmjs.com/cli/v11/configuring-npm/package-json#overrides)
- [Nx migration guidance](https://nx.dev/features/automate-updating-dependencies)
- [SDK-5236](https://linear.app/rudderstack/issue/SDK-5236/remediate-npm-audit-vulnerabilities-in-rudder-sdk-react-native-build)
- [PR #668 failing job](https://github.com/rudderlabs/rudder-sdk-react-native/actions/runs/30624636741/job/91136926577)
