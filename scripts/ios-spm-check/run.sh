#!/usr/bin/env bash
# Builds a fresh React Native app that installs the SDK and every integration
# through Swift Package Manager, from packed npm tarballs.
#
# Usage: scripts/ios-spm-check/run.sh <tarball-dir> <work-dir>
#   <tarball-dir>  holds the `npm pack` output of each package in dist/libs
#   <work-dir>     an empty directory for the app (not under /tmp on macOS)
#
# RN_VERSION and RN_CLI_VERSION override the React Native and CLI versions.
set -euo pipefail

TARBALLS="$(cd "$1" && pwd)"
WORK="$2"
RN_VERSION="${RN_VERSION:-0.88.1}"
RN_CLI_VERSION="${RN_CLI_VERSION:-21.0.0}"
APP=SpmCheck
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

mkdir -p "$WORK"
cd "$WORK"
npx --yes "@react-native-community/cli@${RN_CLI_VERSION}" init "$APP" \
  --version "$RN_VERSION" --directory "$APP" \
  --skip-install --install-pods false --skip-git-init

cd "$APP"
npm install --no-audit --no-fund --ignore-scripts
# The DB encryption plugin is CocoaPods-only, so it is not installed here.
find "$TARBALLS" -name '*.tgz' ! -name '*db-encryption*' -print0 \
  | xargs -0 npm install --no-audit --no-fund --ignore-scripts
cp "$HERE/App.tsx" App.tsx

npx --no-install react-native spm add --deintegrate

xcodebuild -project "ios/${APP}.xcodeproj" -scheme "$APP" \
  -configuration Release -sdk iphonesimulator \
  -destination 'generic/platform=iOS Simulator' \
  CODE_SIGNING_ALLOWED=NO build
