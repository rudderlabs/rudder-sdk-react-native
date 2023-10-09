require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "rudder-plugin-db-encryption-react-native"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  rudder-plugin-db-encryption-react-native
                   DESC
  s.homepage     = "https://rudderstack.com/"
  s.license      = "MIT"
  s.authors      = { "RudderStack" => "sdk@rudderstack.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/rudderlabs/rudder-sdk-react-native.git", :tag => "master" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency 'RudderDatabaseEncryption'
  s.dependency 'RNRudderSdk'

end

