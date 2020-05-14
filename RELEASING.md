# Releasing

 1. Make sure you're on the master branch.
 2. Change `ext.libraryVersion` in the root build.gradle to a non-SNAPSHOT version.
 3. Update CHANGELOG.md for the impending release.
 4. If necessary, update README.md.
 5. Commit (don't push) the changes with message "Release x.y.z" and tag the commit `x.y.z`, where
    `x.y.z` is the new version.
 6. Change `ext.libraryVersion` in the root build.gradle to the next SNAPSHOT version.
 7. Commit the SNAPSHOT change.
 8. Push the 2 commits + 1 tag to origin/master.
 9. Wait for the "release" Action to complete.
10. Visit [Sonatype Nexus](https://oss.sonatype.org/#stagingRepositories). Verify the artifacts,
    close the staging repository, and release the closed staging repository.
11. Create the release on GitHub with release notes copied from the changelog.
