require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "rudder-integration-appsflyer-react-native"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  rudder-integration-appsflyer-react-native
                   DESC
  s.homepage     = "https://github.com/dhawal1248/rudder-integration-appsflyer-react-native"
  s.license      = "MIT"
  s.authors      = { "RudderStack" => "dhawal@rudderlabs.com" }
  s.platforms    = { :ios => "9.0" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "Rudder-Appsflyer"
end

