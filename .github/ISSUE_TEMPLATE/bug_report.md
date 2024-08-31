---
name: Bug report
about: Create a report to help us improve
title: "BUG : <Title>"
labels: bug, open source
assignees: "@rudderlabs/sdk-rn"
---

**Describe the bug**
Please provide the following information:

1. A clear and concise description of what the bug is
2. Share the event payload
3. Offer a minimal viable example to reproduce the issue
4. Specify if the issue is specific to a particular device model or OS version
5. Include the error's stack trace
6. Mention the date when the issue began

**Which platform is the issue occurring on**
Is the error occurring on:

- [ ] Android
- [ ] iOS
- [ ] Both Android and iOS

**To Reproduce**
Steps to reproduce the behaviour:

1. Initialise React-Native SDK
2. Make events '....'
3. See the error

**Expected behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Version of the _React-Native_ SDK**
Please provide the following information:

1. The version of the Rudder React-Native SDK you are using (e.g., Rudder React-Native SDK v1.0.0)
2. The versions of the Rudder Android and iOS SDKs utilized within React-Native (e.g., Rudder Android SDK v1.0.0 and Rudder iOS SDK v1.0.0)
3. If you are utilizing React-Native device mode integration, kindly provide:
   - The name and version of the device mode integration (e.g., Rudder React-Native Amplitude SDK v1.0.0)
   - Indicate if you are using the native SDK directly and specify its version (e.g., React-Native Amplitude SDK v2.0.0)

**SDK initialisation snippet**
Share the code snippet used for initializing the React-Native SDK.

**Framework version of the SDK**
Please indicate the version of the React-Native framework used (e.g., “react-native”: v0.70.6).

**Check for Correct Usage of _writeKey_ and _dataPlaneUrl_**
Confirm that the correct `writeKey` and `dataPlaneUrl` are utilized during SDK initialization.
