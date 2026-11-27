// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-braze-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationBrazeReactNative.
let package = Package(
    name: "RudderIntegrationBrazeReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationBrazeReactNative", targets: ["RudderIntegrationBrazeReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-braze-ios", "4.5.0"..<"5.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationBrazeReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Braze", package: "rudder-integration-braze-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationBraze.xcodeproj", "RudderIntegrationBraze.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
