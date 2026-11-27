// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-sprig-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RNRudderSprigIntegrationModule.
let package = Package(
    name: "RNRudderSprigIntegrationModule",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RNRudderSprigIntegrationModule", targets: ["RNRudderSprigIntegrationModule"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "React-GeneratedCode", path: "../../../ios"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-sprig-ios", "1.1.0"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RNRudderSprigIntegrationModule",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                // App-side codegen output: <RNRudderSprigIntegrationModuleSpec/RNRudderSprigIntegrationModuleSpec.h>
                .product(name: "ReactAppHeaders", package: "React-GeneratedCode"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Sprig", package: "rudder-integration-sprig-ios"),
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
