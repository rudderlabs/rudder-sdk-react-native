# Changelog

This file was generated using [@jscutlery/semver](https://github.com/jscutlery/semver).

## [3.0.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@3.0.0...rudder-sdk-react-native@3.0.1) (2026-01-14)


### Bug Fixes

* improve TypeScript compatibility with isolatedModules by splitting value and type exports ([#526](https://github.com/rudderlabs/rudder-sdk-react-native/issues/526)) ([c3cf3fd](https://github.com/rudderlabs/rudder-sdk-react-native/commit/c3cf3fdf62f035c36d62ab5dda67c01f11e1a0f5))

## [3.0.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@2.1.0...rudder-sdk-react-native@3.0.0) (2025-06-05)


### ⚠ BREAKING CHANGES

* **sdk:** update peerDependencies for react and react-native versions (#451)
* add support for new architecture (#446)

### Features

* add support for new architecture ([#446](https://github.com/rudderlabs/rudder-sdk-react-native/issues/446)) ([17f381f](https://github.com/rudderlabs/rudder-sdk-react-native/commit/17f381f161f16549815c955bae85d8a10da6d130)), closes [#448](https://github.com/rudderlabs/rudder-sdk-react-native/issues/448)
* **sdk:** update peerDependencies for react and react-native versions ([#451](https://github.com/rudderlabs/rudder-sdk-react-native/issues/451)) ([2a6f55d](https://github.com/rudderlabs/rudder-sdk-react-native/commit/2a6f55de4d9e3bc2c99410b26774d1d01ca88d31))

## [2.1.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@2.0.0...rudder-sdk-react-native@2.1.0) (2025-04-29)


### Features

* add React 19 support and resolve Snyk vulnerability ([#418](https://github.com/rudderlabs/rudder-sdk-react-native/issues/418)) ([793ae17](https://github.com/rudderlabs/rudder-sdk-react-native/commit/793ae17076d8f69404877eec07fea1b49c3ce304))

## [2.0.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.15.0...rudder-sdk-react-native@2.0.0) (2024-12-12)


### ⚠ BREAKING CHANGES

* add support for previousId in alias API (#394)

### Features

* add support for previousId in alias API ([#394](https://github.com/rudderlabs/rudder-sdk-react-native/issues/394)) ([f28efc0](https://github.com/rudderlabs/rudder-sdk-react-native/commit/f28efc099e6a28d1cfdc8f8ca0a092ff32928ac8))

## [1.15.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.14.1...rudder-sdk-react-native@1.15.0) (2024-10-29)


### Features

* add gzip configuration support ([#382](https://github.com/rudderlabs/rudder-sdk-react-native/issues/382)) ([abf983b](https://github.com/rudderlabs/rudder-sdk-react-native/commit/abf983b8d50d28810a9a741cf9e3344df878fc0c))

## [1.14.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.14.0...rudder-sdk-react-native@1.14.1) (2024-09-30)


### Bug Fixes

* alias api ([#377](https://github.com/rudderlabs/rudder-sdk-react-native/issues/377)) ([b9168cf](https://github.com/rudderlabs/rudder-sdk-react-native/commit/b9168cfe1bcdfdf696b0f7c534f0841b15050a19))

## [1.14.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.13.1...rudder-sdk-react-native@1.14.0) (2024-05-28)


### Features

* provide putCustomContext API support ([#347](https://github.com/rudderlabs/rudder-sdk-react-native/issues/347)) ([0c28fa4](https://github.com/rudderlabs/rudder-sdk-react-native/commit/0c28fa4d2b25515de2862dc273a448cc14388cec))

## [1.13.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.13.0...rudder-sdk-react-native@1.13.1) (2024-04-24)


### Bug Fixes

* update the version of iOS SDK to address the Privacy Manifest issue ([#342](https://github.com/rudderlabs/rudder-sdk-react-native/issues/342)) ([d81214c](https://github.com/rudderlabs/rudder-sdk-react-native/commit/d81214cf64c0ee94c5cb2e86c1950dad79f823e2))

## [1.13.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.12.1...rudder-sdk-react-native@1.13.0) (2024-04-09)


### Features

* **rudder-sdk-react-native:** add clearAdvertisingId API support ([cfd2ca0](https://github.com/rudderlabs/rudder-sdk-react-native/commit/cfd2ca041231db43997026e494d299466fe290ae))
* **rudder-sdk-react-native:** change linter to eslint ([c7d1096](https://github.com/rudderlabs/rudder-sdk-react-native/commit/c7d10964caf618f6ba643b92a5614a42a81b13ca))
* **rudder-sdk-react-native:** change metro-react-native-babel-preset to @react-native/babel-preset ([35d7e3d](https://github.com/rudderlabs/rudder-sdk-react-native/commit/35d7e3df46e3433fe72327777f05a6ae5809ef49))
* **rudder-sdk-react-native:** enable setting of advertisement ID in iOS before SDK gets initialised ([aed372a](https://github.com/rudderlabs/rudder-sdk-react-native/commit/aed372af566b5d1a9e8a1491b8ee965d5f7a34dd))

## [1.12.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.12.0...rudder-sdk-react-native@1.12.1) (2024-03-26)


### Bug Fixes

* **rudder-sdk-react-native:** filter out NaN params from traits, options and properties ([3c52816](https://github.com/rudderlabs/rudder-sdk-react-native/commit/3c5281600e0a0748d61751ccb94095922570f33b))
* **rudder-sdk-react-native:** prevent crash while handling option object in android module ([3505db0](https://github.com/rudderlabs/rudder-sdk-react-native/commit/3505db061c9dc003ca578cd4f5702d2928f130e5))
* **rudder-sdk-react-native:** prevent crash while handling option object in ios module ([a306e53](https://github.com/rudderlabs/rudder-sdk-react-native/commit/a306e53dd42ee2770ef39dbf0335745c77b7e146))

## [1.12.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.11.3...rudder-sdk-react-native@1.12.0) (2024-02-16)


### Features

* **rudder-sdk-react-native:** provide tvOS support for the react native iOS sdk ([#316](https://github.com/rudderlabs/rudder-sdk-react-native/issues/316)) ([98a3f39](https://github.com/rudderlabs/rudder-sdk-react-native/commit/98a3f391cd7f55976fe38fb815b4fa3b24e09e77))

## [1.11.3](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.11.2...rudder-sdk-react-native@1.11.3) (2023-12-21)


### Bug Fixes

* compatibility issue of the rudder react native ios sdk with bugsnag ([#301](https://github.com/rudderlabs/rudder-sdk-react-native/issues/301)) ([5940331](https://github.com/rudderlabs/rudder-sdk-react-native/commit/594033133a8982af1f241c4019e7d44b42c77053))

## [1.11.2](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.11.1...rudder-sdk-react-native@1.11.2) (2023-12-11)


### Bug Fixes

* incorrect tracking of application lifecycle events ([#292](https://github.com/rudderlabs/rudder-sdk-react-native/issues/292)) ([cd77bfe](https://github.com/rudderlabs/rudder-sdk-react-native/commit/cd77bfe91a70c1dfe70e1f27d6b5d301146f4343))

## [1.11.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.11.0...rudder-sdk-react-native@1.11.1) (2023-10-31)


### Bug Fixes

* improve getRudderContext api return type  ([#273](https://github.com/rudderlabs/rudder-sdk-react-native/issues/273)) ([e013dd6](https://github.com/rudderlabs/rudder-sdk-react-native/commit/e013dd695bafbf4604aa637213c24f3390b8f23b))
* issue with the live reload ([#278](https://github.com/rudderlabs/rudder-sdk-react-native/issues/278)) ([3ac2dec](https://github.com/rudderlabs/rudder-sdk-react-native/commit/3ac2dec853c18f301db4afb156c1cff007ab06e3))

## [1.11.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.10.0...rudder-sdk-react-native@1.11.0) (2023-10-16)


### Features

* integrate database encryption as a plugin ([#261](https://github.com/rudderlabs/rudder-sdk-react-native/issues/261)) ([d798eee](https://github.com/rudderlabs/rudder-sdk-react-native/commit/d798eeeb2ae9dd1ed750e96fe19d8ba80051b34e))

## [1.10.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.9.1...rudder-sdk-react-native@1.10.0) (2023-10-03)


### Features

* add getSessionId support ([#253](https://github.com/rudderlabs/rudder-sdk-react-native/issues/253)) ([1dca51e](https://github.com/rudderlabs/rudder-sdk-react-native/commit/1dca51e4a5c571dac20ddfe2a1c3cf4dc3ac1716))


### Bug Fixes

* **rudder-sdk-react-native:** update depolyment target to 12 in ios module ([50998bc](https://github.com/rudderlabs/rudder-sdk-react-native/commit/50998bc7bd0d6eda97bf3b6ffcc383220e55e798))

## [1.9.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.9.0...rudder-sdk-react-native@1.9.1) (2023-09-21)


### Bug Fixes

* iOS build issue ([#247](https://github.com/rudderlabs/rudder-sdk-react-native/issues/247)) ([4353dee](https://github.com/rudderlabs/rudder-sdk-react-native/commit/4353dee6a7164a5c1be4cccac62a3df8f647e7eb))
* **rudder-sdk-react-native:** fix invalid json issue ([#249](https://github.com/rudderlabs/rudder-sdk-react-native/issues/249)) ([7f1bcca](https://github.com/rudderlabs/rudder-sdk-react-native/commit/7f1bccaf162425b6c02791acf57e8a9b3ae3ae22))

## [1.9.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.8.1...rudder-sdk-react-native@1.9.0) (2023-09-01)


### Features

* add encrypt database api support ([#236](https://github.com/rudderlabs/rudder-sdk-react-native/issues/236)) ([34644df](https://github.com/rudderlabs/rudder-sdk-react-native/commit/34644dfdf1cf8fdb3f15cb5088af1b6d6c23824b))
* made deviceId collection configurable and de-coupled anonymousid and deviceId ([#232](https://github.com/rudderlabs/rudder-sdk-react-native/issues/232)) ([c600d20](https://github.com/rudderlabs/rudder-sdk-react-native/commit/c600d2096d34d8f696de892467446c60ab664e38))

## [1.8.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.8.0...rudder-sdk-react-native@1.8.1) (2023-08-24)


### Bug Fixes

* ios module compilation error ([#233](https://github.com/rudderlabs/rudder-sdk-react-native/issues/233)) ([5ff512f](https://github.com/rudderlabs/rudder-sdk-react-native/commit/5ff512fff9c260936542f92571859b83367af3d6))

## [1.8.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.7.1...rudder-sdk-react-native@1.8.0) (2023-08-09)


### Features

* implement session tracking feature ([#227](https://github.com/rudderlabs/rudder-sdk-react-native/issues/227)) ([007a120](https://github.com/rudderlabs/rudder-sdk-react-native/commit/007a12036b7870cff6b8f732b7e60dae45d6a6e8))

## [1.7.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.7.0...rudder-sdk-react-native@1.7.1) (2023-05-23)


### Bug Fixes

* **rudder-sdk-react-native:** allow rudder android sdk version upto 2.0 ([2f8fb79](https://github.com/rudderlabs/rudder-sdk-react-native/commit/2f8fb796b2393ef52c4d685cc4ac57925ba03b0e))

## [1.7.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.6.3...rudder-sdk-react-native@1.7.0) (2023-03-30)


### Features

* bumped the version of native ios sdk to atleast 1.13.0 ([#194](https://github.com/rudderlabs/rudder-sdk-react-native/issues/194)) ([3fddef5](https://github.com/rudderlabs/rudder-sdk-react-native/commit/3fddef5d180ddd0774da7d3cd476090d0b3973a4))


### Bug Fixes

* update the minimum version of CleverTap and native iOS SDK and fix Firebase version ([#196](https://github.com/rudderlabs/rudder-sdk-react-native/issues/196)) ([d75047e](https://github.com/rudderlabs/rudder-sdk-react-native/commit/d75047e170ab7d74fea6b2f7a32bead23a15bace))

## [1.6.3](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.6.2...rudder-sdk-react-native@1.6.3) (2023-03-13)


### Bug Fixes

* peer dependency issue in sdks and remove typescript warning in alias api ([#184](https://github.com/rudderlabs/rudder-sdk-react-native/issues/184)) ([fd6cab2](https://github.com/rudderlabs/rudder-sdk-react-native/commit/fd6cab262d1cba21dfd7129caa1a53d614cb7783))

## [1.6.2](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.6.1...rudder-sdk-react-native@1.6.2) (2023-02-22)


### Bug Fixes

* unhandled promise rejection in the rudder react-native sdk ([#175](https://github.com/rudderlabs/rudder-sdk-react-native/issues/175)) ([1c9c866](https://github.com/rudderlabs/rudder-sdk-react-native/commit/1c9c866dfd59ef751075ccbcbece36efd891d50b))

## [1.6.1](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.6.0...rudder-sdk-react-native@1.6.1) (2023-02-03)


### Bug Fixes

* **rudder-sdk-react-native:** add option to automatically detect dependencies ([961acda](https://github.com/rudderlabs/rudder-sdk-react-native/commit/961acda5e6995cdab4ffab7d108ec9ab0ec023b3))

## [1.6.0](https://github.com/rudderlabs/rudder-sdk-react-native/compare/rudder-sdk-react-native@1.5.2...rudder-sdk-react-native@1.6.0) (2023-01-19)


### Features

* add support for externalId key ([b02658b](https://github.com/rudderlabs/rudder-sdk-react-native/commit/b02658be45bdff13a892e01a58dd1535b0443bd0))


### Bug Fixes

* add .plist file and enhance build and quality check action ([#156](https://github.com/rudderlabs/rudder-sdk-react-native/issues/156)) ([09cd597](https://github.com/rudderlabs/rudder-sdk-react-native/commit/09cd5978597466e157b251642a2e9e1dfdb6c124))

## [1.5.2](https://github.com/rudderlabs/rudder-sdk-react-native/compare/sdk-1.5.1...sdk-1.5.2) (2023-01-11)
