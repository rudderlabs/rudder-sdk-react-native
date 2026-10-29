# Native dependency submission

The workflow resolves two independent Gradle contexts with JDK 17 and each consumer's wrapper:

- `apps/example/android`: root `npm ci`, Nx synchronization and symlinks select local source libraries. A guard checks every native library under `libs`, including Sprig and database encryption, against React Native autolinking output and real source paths.
- `apps/expo-example/android`: its own `npm ci` selects published SDK/integration versions. This context does not replace local-source coverage.

Dependency changes on `master`, weekly refreshes, manual runs and pull requests generate artifacts with read permission. Only successful default-branch runs submit, using a separate write-permission job. Each matrix context has a distinct Gradle action identity. Runtime and development dependencies remain present; no severity or test dependency filters are added. A resolution check rejects unresolved dependencies before submission. It permits known Kotlin metadata variant ambiguities for local Android projects and React/Hermes artifacts, whose runtime and test configurations are resolved separately. It also permits the included React Native Gradle plugin's formatting-only `ktfmt` configuration when it has no repository. Other unresolved entries fail the run.

Runtime scope is limited to the `:app` project's non-test runtime configurations. These configurations include the linked native libraries. Dependencies used only by included React Native and Expo Gradle plugin builds remain development-scoped.

The pinned Gradle action requires organization allowlist approval. After merge, inspect both artifacts and verify actual Maven ingestion at the source revision. Confirm that local native modules remain linked and that published package versions match the Expo lock. Build dependencies resolved during configuration are included; test dependencies of external included build tooling are not separately certified by this workflow.

Submission does not prove Gradle updater traversal into npm-linked library manifests. No speculative Gradle updater roots or CocoaPods changes are included. Existing npm and Actions configuration remains unchanged.

Rollback: remove this workflow and its local-consumer guard. Preserve native alerts and existing updater configuration. Previously submitted snapshots may remain until superseded or removed.
