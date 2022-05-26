Pod::Spec.new do |s|
  s.name         = "RNRudderSdk"
  s.version      = "1.0.0"
  s.summary      = "RNRudderSdk"
  s.description  = <<-DESC
                  RNRudderSdk
                   DESC
  s.homepage     = "https://rudderstack.com/"
  s.license      = "MIT"
  s.author       = { "RudderStack" => "dhawal@rudderlabs.com" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/rudderlabs/rudder-sdk-react-native.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "Rudder", "~> 1.6.0"
end


