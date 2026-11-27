// swift-tools-version: 6.0
import PackageDescription

// SwiftPM support for React Native's SwiftPM autolinking (react-native >= 0.87).
// CocoaPods users are unaffected; see rudder-integration-appsflyer-react-native.podspec.
// Local paths are resolved by the autolinker relative to
// <app>/ios/build/generated/autolinking/libs/RudderIntegrationAppsflyerReactNative.
let package = Package(
    name: "RudderIntegrationAppsflyerReactNative",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "RudderIntegrationAppsflyerReactNative", targets: ["RudderIntegrationAppsflyerReactNative"]),
    ],
    dependencies: [
        .package(name: "ReactNative", path: "../../../../xcframeworks"),
        .package(name: "RNRudderSdk", path: "../RNRudderSdk"),
        .package(url: "https://github.com/rudderlabs/rudder-integration-appsflyer-ios", "3.1.0"..<"4.0.0"),
        // The wrapper imports <AppsFlyerLib/AppsFlyerLib.h> directly; same package as the native integration.
        // Capped below AppsFlyer 7 / Rudder-Appsflyer 4: the wrapper uses setAppsFlyerDevKey:/setAppleAppID:, removed in AppsFlyer 7.
        .package(url: "https://github.com/AppsFlyerSDK/AppsFlyerFramework-Static", "6.17.0"..<"7.0.0"),
    ],
    targets: [
        .target(
            name: "RudderIntegrationAppsflyerReactNative",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "RNRudderSdk", package: "RNRudderSdk"),
                .product(name: "Rudder-Appsflyer", package: "rudder-integration-appsflyer-ios"),
                .product(name: "AppsFlyerLib-Static", package: "AppsFlyerFramework-Static"),
            ],
            path: "ios",
            exclude: ["RudderIntegrationAppsflyerReactNative.xcodeproj", "RudderIntegrationAppsflyerReactNative.xcworkspace"],
            publicHeadersPath: "include"
        ),
    ]
)
