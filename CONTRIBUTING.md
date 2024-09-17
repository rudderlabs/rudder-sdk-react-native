# Contributing to RudderStack

Thanks for taking the time and for your help in improving this project!

## Table of contents

- [**RudderStack Contributor Agreement**](#rudderstack-contributor-agreement)
- [**Contribute to this project**](#contribute-to-this-project)
- [**Committing**](#committing)
- [**Conventional commit messages format for React-Native monorepo**](#conventional-commit-messages-format-for-react-native-monorepo)
- [**Installing and setting up RudderStack**](#installing-and-setting-up-rudderstack)
- [**Getting help**](#getting-help)

## RudderStack Contributor Agreement

To contribute to this project, we need you to sign the [**Contributor License Agreement (“CLA”)**][CLA] for the first commit you make. By agreeing to the [**CLA**][CLA]
we can add you to list of approved contributors and review the changes proposed by you.

## Contribute to this project

If you encounter a bug or have any suggestion for improving this project, you can [**submit an issue**][issue] describing your proposed change. Alternatively, you can propose a change by making a pull request and tagging our team members.

For more information on the different ways in which you can contribute to RudderStack, you can chat with us on our [**Slack**][Slack] channel.

## Committing

We prefer squash or rebase commits so that all changes from a branch are committed to master as a single commit. All pull requests are squashed when merged, but rebasing prior to merge gives you better control over the commit message.

## Conventional commit messages format for React-Native monorepo

To maintain consistency and provide clear information about the nature of commits, it is essential to follow conventional commit messages. The commit message format should adhere to `<type>(scope): <description>`, and you can find more details about it [here](https://www.conventionalcommits.org/en/v1.0.0/). By following this format, you ensure that commit messages are structured and convey the necessary information effectively.

List of React-Native packages and their corresponding commit message format:

| React Native packages                           | Fix commit message format                                            | Feature commit message format                                               |
| ----------------------------------------------- | -------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| rudder-sdk-react-native                         | fix(rudder-sdk-react-native): fix some issue                         | feat(rudder-sdk-react-native): add some new feature                         |
| rudder-integration-amplitude-react-native       | fix(rudder-integration-amplitude-react-native): fix some issue       | feat(rudder-integration-amplitude-react-native): add some new feature       |
| rudder-integration-appcenter-react-native       | fix(rudder-integration-appcenter-react-native): fix some issue       | feat(rudder-integration-appcenter-react-native): add some new feature       |
| rudder-integration-appsflyer-react-native       | fix(rudder-integration-appsflyer-react-native): fix some issue       | feat(rudder-integration-appsflyer-react-native): add some new feature       |
| rudder-integration-braze-react-native           | fix(rudder-integration-braze-react-native): fix some issue           | feat(rudder-integration-braze-react-native): add some new feature           |
| rudder-integration-clevertap-react-native       | fix(rudder-integration-clevertap-react-native): fix some issue       | feat(rudder-integration-clevertap-react-native): add some new feature       |
| rudder-integration-firebase-react-native        | fix(rudder-integration-firebase-react-native): fix some issue        | feat(rudder-integration-firebase-react-native): add some new feature        |
| rudder-integration-moengage-react-native        | fix(rudder-integration-moengage-react-native): fix some issue        | feat(rudder-integration-moengage-react-native): add some new feature        |
| rudder-integration-singular-react-native        | fix(rudder-integration-singular-react-native): fix some issue        | feat(rudder-integration-singular-react-native): add some new feature        |
| example                                         | fix(example): fix some issue                                         | feat(example): add some new feature                                         |
| rudder-sdk-react-native-monorepo                | fix(rudder-sdk-react-native-monorepo): fix some issue                | feat(rudder-sdk-react-native-monorepo): add some new feature                |
| rudder-plugin-db-encryption-react-native        | fix(rudder-plugin-db-encryption-react-native): fix some issue        | feat(rudder-plugin-db-encryption-react-native): add some new feature        |
| rudder-plugin-ketch-consent-filter-react-native | fix(rudder-plugin-ketch-consent-filter-react-native): fix some issue | feat(rudder-plugin-ketch-consent-filter-react-native): add some new feature |

You may also use `chore` and other commit types as described in the [**Conventional Commit**](https://www.conventionalcommits.org/en/v1.0.0/) documentation. But only `fix` and `feat` commits will be considered for changelog generation.

## Installing and setting up RudderStack

To contribute to this project, you may need to install RudderStack on your machine. You can do so by following our [**docs**](https://www.rudderstack.com/docs/sources/event-streams/sdks/rudderstack-react-native-sdk/) and set up RudderStack in no time.

## Getting help

For any questions, concerns, or queries, you can start by asking a question on our [**Slack**][Slack] channel.
<br><br>

### We look forward to your feedback on improving this project!

<!----variables---->

[issue]: https://github.com/rudderlabs/rudder-sdk-react-native/issues/new/choose
[CLA]: https://rudderlabs.wufoo.com/forms/rudderlabs-contributor-license-agreement
[Slack]: https://rudderstack.com/join-rudderstack-slack-community/
