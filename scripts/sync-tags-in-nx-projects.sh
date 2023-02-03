#!/bin/bash
# List of package folders
projectFolderNames=("rudder-integration-amplitude-react-native" "rudder-integration-appcenter-react-native" "rudder-integration-appsflyer-react-native" "rudder-integration-braze-react-native" "rudder-integration-clevertap-react-native" "rudder-integration-firebase-react-native" "rudder-integration-moengage-react-native" "rudder-integration-singular-react-native" "sdk")

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
  echo "Sync version in project.json: ${packageName}, $package_version"
  # This will not work on MAC system
  sed -i "s/$packageName@.*/$packageName@$package_version\",/" project.json
  cd ..
  cd ..
done
