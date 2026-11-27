// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-appcenter-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationAppcenterReactNative.
let package = Package(
    name: "RudderIntegrationAppcenterReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationAppcenterReactNative", targets: ["RudderIntegrationAppcenterReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-appcenter-ios", "1.2.0"..<"2.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationAppcenterReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-AppCenter", package: "rudder-integration-appcenter-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationAppcenterReactNative.xcodeproj", "RudderIntegrationAppcenterReactNative.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
