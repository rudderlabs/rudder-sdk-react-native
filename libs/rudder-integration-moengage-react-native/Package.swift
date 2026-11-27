// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-moengage-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationMoengageReactNative.
let package = Package(
    name: "RudderIntegrationMoengageReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationMoengageReactNative", targets: ["RudderIntegrationMoengageReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-moengage-ios", "2.5.0"..<"3.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationMoengageReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Moengage", package: "rudder-integration-moengage-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationMoengage.xcodeproj", "RudderIntegrationMoengage.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
