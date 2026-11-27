# Changelog

## [2.47.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/v2.46.0...v2.47.0) (2026-11-27)


### ⚠ BREAKING CHANGES

* **clevertap:** add swift package manager support ([#759](https://github.com/rudderlabs/rudder-sdk-react-native/issues/759))

### Features

* **amplitude:** add swift package manager support ([#755](https://github.com/rudderlabs/rudder-sdk-react-native/issues/755)) ([91a5ba9](https://github.com/rudderlabs/rudder-sdk-react-native/commit/91a5ba9b677a02937fb15362c824be75c1b50657))
* **appcenter:** add swift package manager support ([#756](https://github.com/rudderlabs/rudder-sdk-react-native/issues/756)) ([4c2cf94](https://github.com/rudderlabs/rudder-sdk-react-native/commit/4c2cf94073152fd549d4ff546e19afb6363b6faf))
* **appsflyer:** add swift package manager support ([#757](https://github.com/rudderlabs/rudder-sdk-react-native/issues/757)) ([1b82eb7](https://github.com/rudderlabs/rudder-sdk-react-native/commit/1b82eb70076d8b97149ebe64140eda6f02c5e202))
* **braze:** add swift package manager support ([#758](https://github.com/rudderlabs/rudder-sdk-react-native/issues/758)) ([97bcb1d](https://github.com/rudderlabs/rudder-sdk-react-native/commit/97bcb1d953119357c4630698c258d9eb8df50551))
* **clevertap:** add swift package manager support ([#759](https://github.com/rudderlabs/rudder-sdk-react-native/issues/759)) ([ceb2f17](https://github.com/rudderlabs/rudder-sdk-react-native/commit/ceb2f17d24c9f311a09eebe31b0cf0ea6b26335d))
* **facebook:** add swift package manager support ([#760](https://github.com/rudderlabs/rudder-sdk-react-native/issues/760)) ([6bd2005](https://github.com/rudderlabs/rudder-sdk-react-native/commit/6bd200552cca01ecc68234961d6444b108ecfe2b))
* **firebase:** add swift package manager support ([#761](https://github.com/rudderlabs/rudder-sdk-react-native/issues/761)) ([44a7196](https://github.com/rudderlabs/rudder-sdk-react-native/commit/44a7196af82f436246c20f65393564af3fa1a859))
* **moengage:** add swift package manager support ([#762](https://github.com/rudderlabs/rudder-sdk-react-native/issues/762)) ([b28a4cd](https://github.com/rudderlabs/rudder-sdk-react-native/commit/b28a4cd4dc974f329ba6c68db9c9eed4fcc01f3d))
* **sdk:** add swift package manager support ([#754](https://github.com/rudderlabs/rudder-sdk-react-native/issues/754)) ([ff12e72](https://github.com/rudderlabs/rudder-sdk-react-native/commit/ff12e72c40f51323b70537865c0c61169aebd214))
* **singular:** add swift package manager support ([#763](https://github.com/rudderlabs/rudder-sdk-react-native/issues/763)) ([9d3cb74](https://github.com/rudderlabs/rudder-sdk-react-native/commit/9d3cb74160b62505ecd45c08e07f8f86542ea571))
* **sprig:** add swift package manager support ([#764](https://github.com/rudderlabs/rudder-sdk-react-native/issues/764)) ([6c25353](https://github.com/rudderlabs/rudder-sdk-react-native/commit/6c25353452c2e22212298cf37ab7e0e1d202f3d3))


### Bug Fixes

* **android:** remove legacy AGP classpaths from integration libraries ([#750](https://github.com/rudderlabs/rudder-sdk-react-native/issues/750)) ([b46d204](https://github.com/rudderlabs/rudder-sdk-react-native/commit/b46d2049e5383d48339955f07e0ffafffd1e5a87))
* **android:** skip explicit Kotlin plugin when AGP registers the kotlin extension ([#696](https://github.com/rudderlabs/rudder-sdk-react-native/issues/696)) ([e0af869](https://github.com/rudderlabs/rudder-sdk-react-native/commit/e0af869c062303983988a9ed46dfe9f18f8e6788))
* **deps:** resolve vulnerable glob in nx jest tooling ([d5e0dc9](https://github.com/rudderlabs/rudder-sdk-react-native/commit/d5e0dc999826209a0403faaf4bde6409a73f5991))
* **example:** override shell-quote to patched 1.11.0 ([#743](https://github.com/rudderlabs/rudder-sdk-react-native/issues/743)) ([7fb6ca9](https://github.com/rudderlabs/rudder-sdk-react-native/commit/7fb6ca961e97179a5fe24afd154125fbfe406301))
* remove the react-native peer upper bound from integrations ([#751](https://github.com/rudderlabs/rudder-sdk-react-native/issues/751)) ([6c8df76](https://github.com/rudderlabs/rudder-sdk-react-native/commit/6c8df76a3f4d347d29b256ef832d462a89353ce4))
* sdk-7055 remove the react-native peer upper bound from integrations ([6c8df76](https://github.com/rudderlabs/rudder-sdk-react-native/commit/6c8df76a3f4d347d29b256ef832d462a89353ce4))


### Dependencies

* The following workspace dependencies were updated
  * dependencies
    * @rudderstack/rudder-integration-amplitude-react-native bumped from file:libs/rudder-integration-amplitude-react-native to 1.5.0
    * @rudderstack/rudder-integration-appcenter-react-native bumped from file:libs/rudder-integration-appcenter-react-native to 1.5.0
    * @rudderstack/rudder-integration-appsflyer-react-native bumped from file:libs/rudder-integration-appsflyer-react-native to 1.10.0
    * @rudderstack/rudder-integration-braze-react-native bumped from file:libs/rudder-integration-braze-react-native to 2.7.0
    * @rudderstack/rudder-integration-clevertap-react-native bumped from file:libs/rudder-integration-clevertap-react-native to 2.0.0
    * @rudderstack/rudder-integration-facebook-react-native bumped from file:libs/rudder-integration-facebook-react-native to 1.4.0
    * @rudderstack/rudder-integration-firebase-react-native bumped from file:libs/rudder-integration-firebase-react-native to 2.4.0
    * @rudderstack/rudder-integration-moengage-react-native bumped from file:libs/rudder-integration-moengage-react-native to 1.5.0
    * @rudderstack/rudder-integration-singular-react-native bumped from file:libs/rudder-integration-singular-react-native to 1.5.0
    * @rudderstack/rudder-integration-sprig-react-native bumped from file:libs/rudder-integration-sprig-react-native to 1.1.0
    * @rudderstack/rudder-plugin-db-encryption-react-native bumped from file:libs/plugins/rudder-plugin-db-encryption-react-native to 1.6.3
    * @rudderstack/rudder-sdk-react-native bumped from file:./libs/sdk to 3.3.0

## [2.46.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/v2.45.0...v2.46.0) (2026-09-21)


### Bug Fixes

* sdk-413 resolve react native eslint warnings ([#699](https://github.com/rudderlabs/rudder-sdk-react-native/issues/699)) ([1c61949](https://github.com/rudderlabs/rudder-sdk-react-native/commit/1c61949366860bb0312d1b55e8ebfda1e8fa12fe))


### Dependencies

* The following workspace dependencies were updated
  * dependencies
    * @rudderstack/rudder-sdk-react-native bumped from file:./libs/sdk to 3.2.1

## [2.45.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/v2.44.0...v2.45.0) (2026-08-27)


### Features

* sdk-5301 expose android deep link tracking config ([#685](https://github.com/rudderlabs/rudder-sdk-react-native/issues/685)) ([2092dc2](https://github.com/rudderlabs/rudder-sdk-react-native/commit/2092dc26a2e7bb3daa3ac9d5694fbd4a4992869c))


### Bug Fixes

* **build:** restore packaged assets in libs after nx 22 migration ([#691](https://github.com/rudderlabs/rudder-sdk-react-native/issues/691)) ([cfb510a](https://github.com/rudderlabs/rudder-sdk-react-native/commit/cfb510a37213c89f72bc3ecb8302c04a5dec05dd))


### Dependencies

* The following workspace dependencies were updated
  * dependencies
    * @rudderstack/rudder-integration-amplitude-react-native bumped from file:libs/rudder-integration-amplitude-react-native to 1.4.1
    * @rudderstack/rudder-integration-appcenter-react-native bumped from file:libs/rudder-integration-appcenter-react-native to 1.4.1
    * @rudderstack/rudder-integration-appsflyer-react-native bumped from file:libs/rudder-integration-appsflyer-react-native to 1.9.1
    * @rudderstack/rudder-integration-braze-react-native bumped from file:libs/rudder-integration-braze-react-native to 2.6.1
    * @rudderstack/rudder-integration-clevertap-react-native bumped from file:libs/rudder-integration-clevertap-react-native to 1.4.1
    * @rudderstack/rudder-integration-facebook-react-native bumped from file:libs/rudder-integration-facebook-react-native to 1.3.1
    * @rudderstack/rudder-integration-firebase-react-native bumped from file:libs/rudder-integration-firebase-react-native to 2.3.1
    * @rudderstack/rudder-integration-moengage-react-native bumped from file:libs/rudder-integration-moengage-react-native to 1.4.1
    * @rudderstack/rudder-integration-singular-react-native bumped from file:libs/rudder-integration-singular-react-native to 1.4.1
    * @rudderstack/rudder-integration-sprig-react-native bumped from file:libs/rudder-integration-sprig-react-native to 1.0.2
    * @rudderstack/rudder-plugin-db-encryption-react-native bumped from file:libs/plugins/rudder-plugin-db-encryption-react-native to 1.6.2
    * @rudderstack/rudder-sdk-react-native bumped from file:./libs/sdk to 3.2.0

## [2.44.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/v2.43.0...v2.44.0) (2026-08-03)


### Features

* **braze:** raise native braze integration minimum versions ([#641](https://github.com/rudderlabs/rudder-sdk-react-native/issues/641)) ([4270b49](https://github.com/rudderlabs/rudder-sdk-react-native/commit/4270b494ea80a9d5f40d5f300452fa9d3eb9a931))


### Dependencies

* The following workspace dependencies were updated
  * dependencies
    * @rudderstack/rudder-integration-braze-react-native bumped from file:libs/rudder-integration-braze-react-native to 2.6.0
