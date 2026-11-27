// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see RNRudderSdk.podspec.
// The two React Native packages are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RNRudderSdk.
let package = Package(
    name: "RNRudderSdk",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RNRudderSdk", targets: ["RNRudderSdk"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "React-GeneratedCode", path: "../../../ios"),
        .package(url: "https://github.com/rudderlabs/rudder-sdk-ios", "1.32.1"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RNRudderSdk",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                // App-side codegen output: <RNRudderSdkModuleSpec/RNRudderSdkModuleSpec.h>
                .product(name: "ReactAppHeaders", package: "React-GeneratedCode"),
                .product(name: "Rudder", package: "rudder-sdk-ios"),
            ],
            path: "ios",
            // include/RNRudderSdk/ holds forwarding headers so dependents
            // (integrations) can #import <RNRudderSdk/RNRudderAnalytics.h>.
            publicHeadersPath: "include",
            cSettings: [.headerSearchPath(".")],
            cxxSettings: [
                .headerSearchPath("."),
                .define("DEBUG", .when(configuration: .debug)),
                .define("NDEBUG", .when(configuration: .release)),
            ],
            linkerSettings: [.linkedFramework("UIKit"), .linkedFramework("Foundation")]
        ),
    ],
    cxxLanguageStandard: .cxx20
)
