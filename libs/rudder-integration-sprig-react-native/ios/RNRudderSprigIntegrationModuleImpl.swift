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
                            rejecter _: @escaping RCTPromiseRejectBlock) {
        let factory = RudderSprigFactory.instance

        // Hop to main: UIScene APIs are main-thread-only, and we want the
        // register-once guard plus the VC-wire to be observed in the same
        // serial step (mirrors the Android `callbacksRegistered` guard).
        DispatchQueue.main.async { [weak self] in
            guard let self else {
                resolve(nil)
                return
            }

            if !self.integrationRegistered {
                RNRudderAnalytics.addIntegration(factory)
                self.integrationRegistered = true
            }

            if let rootVC = self.currentRootViewController() {
                factory.setViewController(rootVC)
            } else {
                RSLogger.logWarn("Sprig: no foreground key window at setup; surveys will not present until a view controller is wired.")
            }
            resolve(nil)
        }
    }

    private func currentRootViewController() -> UIViewController? {
        let keyWindow = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .filter { $0.activationState == .foregroundActive }
            .flatMap(\.windows)
            .first(where: \.isKeyWindow)

        // Walk the presentation chain so we hand Sprig the topmost VC. If the
        // host app already has a modal up (login sheet, paywall, etc.),
        // presenting from rootViewController would throw "already presenting".
        var vc = keyWindow?.rootViewController
        while let presented = vc?.presentedViewController {
            vc = presented
        }
        return vc
    }
}
