// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-facebook-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationFacebookReactNative.
let package = Package(
    name: "RudderIntegrationFacebookReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationFacebookReactNative", targets: ["RudderIntegrationFacebookReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-facebook-ios", "2.4.0"..<"3.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationFacebookReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Facebook", package: "rudder-integration-facebook-ios"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationFacebookReactNative.xcodeproj", "RudderIntegrationFacebookReactNative.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
