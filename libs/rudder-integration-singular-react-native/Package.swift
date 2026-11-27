// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-singular-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationSingularReactNative.
let package = Package(
    name: "RudderIntegrationSingularReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationSingularReactNative", targets: ["RudderIntegrationSingularReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-singular-ios", "1.3.1"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationSingularReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Singular", package: "rudder-integration-singular-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationSingular.xcodeproj", "RudderIntegrationSingular.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
