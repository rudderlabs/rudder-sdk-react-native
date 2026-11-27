// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-amplitude-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationAmplitudeReactNative.
let package = Package(
    name: "RudderIntegrationAmplitudeReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationAmplitudeReactNative", targets: ["RudderIntegrationAmplitudeReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-amplitude-ios", "1.3.1"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationAmplitudeReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Amplitude", package: "rudder-integration-amplitude-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationAmplitude.xcodeproj", "RudderIntegrationAmplitude.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
