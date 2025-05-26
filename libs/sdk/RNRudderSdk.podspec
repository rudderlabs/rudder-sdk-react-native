require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNRudderSdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  RNRudderSdk
                   DESC
  s.homepage     = "https://rudderstack.com/"
  s.license      = "MIT"
  s.author       = { "RudderStack" => "sdk@rudderstack.com" }

  s.platforms    = { :ios => "7.0" }
  s.source       = { :git => "https://github.com/rudderlabs/rudder-sdk-react-native.git", :tag => "master" }

  s.source_files = "ios/**/*.{h,m,mm,cpp}"
  s.private_header_files = "ios/**/*.h"

  s.ios.deployment_target = '12.0'
  s.tvos.deployment_target = '11.0'
  
  s.dependency "Rudder", '>= 1.31.0', '< 2.0.0'

 install_modules_dependencies(s)
end
