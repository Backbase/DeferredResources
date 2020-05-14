# Releasing

1. Make sure you're on the master branch.
2. Change `ext.libraryVersion` in root build.gradle.
3. Update CHANGELOG.md for the impending release.
4. If necessary, update README.md.
5. `./gradlew clean assembleRelease && ./gradlew publishReleasePublicationToMavenCentralRepository`
6. Visit [Sonatype Nexus](https://oss.sonatype.org/#stagingRepositories). Verify the artifacts,
   close the staging repository, and release the closed staging repository.
7. Commit and push the release changes to master.
8. Create the release tag on GitHub with release notes copied from the changelog.
