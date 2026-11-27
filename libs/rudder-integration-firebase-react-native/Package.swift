// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-firebase-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RNRudderFirebaseIntegrationModule.
let package = Package(
    name: "RNRudderFirebaseIntegrationModule",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RNRudderFirebaseIntegrationModule", targets: ["RNRudderFirebaseIntegrationModule"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "React-GeneratedCode", path: "../../../ios"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-firebase-ios", "3.9.0"..<"4.0.0"),
    ],
    targets: [
        .target(
            name: "RNRudderFirebaseIntegrationModule",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                // App-side codegen output: <RNRudderFirebaseIntegrationModuleSpec/RNRudderFirebaseIntegrationModuleSpec.h>
                .product(name: "ReactAppHeaders", package: "React-GeneratedCode"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Firebase", package: "rudder-integration-firebase-ios"),
            ],
            path: "ios",
            publicHeadersPath: "include",
            cxxSettings: [
                .define("DEBUG", .when(configuration: .debug)),
                .define("NDEBUG", .when(configuration: .release)),
            ]
        ),
    ],
    cxxLanguageStandard: .cxx20
)
