#!/bin/bash
# List of package folders
projectFolderNames=("rudder-integration-amplitude-react-native" "rudder-integration-appcenter-react-native" "rudder-integration-appsflyer-react-native" "rudder-integration-braze-react-native" "rudder-integration-clevertap-react-native" "rudder-integration-facebook-react-native" "rudder-integration-firebase-react-native" "rudder-integration-moengage-react-native" "rudder-integration-singular-react-native" "sdk" "plugins/rudder-plugin-db-encryption-react-native" "plugins/rudder-plugin-ketch-consent-filter-react-native")
for projectFolder in ${projectFolderNames[@]}; do
    # Set of package project name

    # Navigate to folder and perform the string replacement in project.json
    packageName=$projectFolder
    if [ "$packageName" = "sdk" ]; then
        packageName="rudder-sdk-react-native"
    fi
    cd libs
    cd $projectFolder
    package_version=$(jq -r .version package.json)
    echo "Generate github release notes file: ${packageName}, $package_version"
    awk -v ver="$package_version" '
   /^(##|###) \[?[0-9]+.[0-9]+.[0-9]+/ {
      if (p) { exit };
      if (index($2, "[")) {
          split($2, a, "[");
          split(a[2], a, "]");
          if (a[1] == ver) {
              p = 1
          }
      } else {
          if ($2 == ver) {
              p = 1
          }
      }
  } p
  ' './CHANGELOG.md' >'./CHANGELOG_LATEST.md'
    cd ..
    cd ..
done
