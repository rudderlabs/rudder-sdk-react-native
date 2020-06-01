
Pod::Spec.new do |s|
  s.name         = "RNRudderSdk"
  s.version      = "1.0.0"
  s.summary      = "RNRudderSdk"
  s.description  = <<-DESC
                  RNRudderSdk
                   DESC
  s.homepage     = "https://rudderstack.com/"
  s.license      = "MIT"
  s.author       = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNRudderSdk.git", :tag => "master" }
  s.source_files  = "**.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "Rudder"
end

  