//
//  FBSDKInit.swift
//  Example
//
//  Created by Abhishek Pandey on 19/09/24.
//

import Foundation
import FBSDKCoreKit


@objc class FBSDKInitializer: NSObject {
  
  @objc static func setupFBSDK(application: UIApplication, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
    /**
     * This code initializes the SDK when your app launches, and allows the SDK handle logins and sharing from the native Facebook app when you perform a Login or Share action. Otherwise, the user must be logged into Facebook to use the in-app browser to login. Refer Facebook App Event doc for more info: https://developers.facebook.com/docs/app-events/getting-started-app-events-ios
     */
    ApplicationDelegate.shared.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
