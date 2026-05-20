import Foundation
import UIKit
import React
import RNRudderSdk
import Rudder
import Rudder_Sprig

@objc(RNRudderSprigIntegrationModuleImpl)
public final class RNRudderSprigIntegrationModuleImpl: NSObject {
    private var integrationRegistered = false

    @objc public func setup(_ resolve: @escaping RCTPromiseResolveBlock,
                            rejecter reject: @escaping RCTPromiseRejectBlock) {
        let factory = RudderSprigFactory.instance

        // Guard against re-registration on hot reload / repeated setup() calls
        // (mirrors the Android `callbacksRegistered` guard in the Java impl).
        if !integrationRegistered {
            RNRudderAnalytics.addIntegration(factory)
            integrationRegistered = true
        }

        // Provide the host app's root view controller so trackAndPresent surveys can show.
        // Resolve only after the VC is wired, so JS callers awaiting setup() can safely
        // fire survey-triggering events on the next tick.
        DispatchQueue.main.async {
            if let rootVC = self.currentRootViewController() {
                factory.setViewController(rootVC)
            }
            resolve(nil)
        }
    }

    private func currentRootViewController() -> UIViewController? {
        var keyWindow: UIWindow?
        for scene in UIApplication.shared.connectedScenes {
            guard scene.activationState == .foregroundActive,
                  let windowScene = scene as? UIWindowScene else { continue }
            if let found = windowScene.windows.first(where: { $0.isKeyWindow }) {
                keyWindow = found
                break
            }
        }

        // Walk the presentation chain so we hand Sprig the topmost VC. If the host
        // app already has a modal up (login sheet, paywall, etc.), presenting the
        // survey from rootViewController would throw "already presenting".
        var vc = keyWindow?.rootViewController
        while let presented = vc?.presentedViewController {
            vc = presented
        }
        return vc
    }
}
