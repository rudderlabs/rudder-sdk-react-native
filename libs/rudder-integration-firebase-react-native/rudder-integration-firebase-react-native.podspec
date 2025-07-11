require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "rudder-integration-firebase-react-native"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  rudder-integration-firebase-react-native
                   DESC
  s.homepage     = "https://rudderstack.com/"
  s.license      = "MIT"
  s.authors      = { "RudderStack" => "sdk@rudderstack.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/rudderlabs/rudder-sdk-react-native.git", :tag => "master" }

  s.source_files = "ios/**/*.{h,m,mm,swift}"
  s.requires_arc = true
  s.static_framework = true

  s.dependency "React"
  s.dependency "Rudder-Firebase", "~> 3.6"
  s.dependency 'RNRudderSdk'

  install_modules_dependencies(s)
end

