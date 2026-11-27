// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-clevertap-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationCleverTapReactNative.
let package = Package(
    name: "RudderIntegrationCleverTapReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationCleverTapReactNative", targets: ["RudderIntegrationCleverTapReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-clevertap-ios", "1.4.1"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationCleverTapReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-CleverTap", package: "rudder-integration-clevertap-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationCleverTapReactNative.xcodeproj", "RudderIntegrationCleverTapReactNative.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
