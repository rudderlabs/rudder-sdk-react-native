#!/bin/bash

# Read the version from package.json and store it in a variable
latest_version=$(jq -r '.version' libs/sdk/package.json)

# Set the path to the Constant.ts file
file_path="./libs/sdk/src/Constants.ts"

# Replace the SDK_VERSION with the previous version
# For Mac
# sed -i '' -e "s/const SDK_VERSION = '.*';/const SDK_VERSION = '$latest_version';/" "$file_path"
# For Linux
sed -i "s/const SDK_VERSION = '.*';/const SDK_VERSION = '$latest_version';/" "$file_path"
