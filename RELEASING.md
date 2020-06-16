# Releasing

 1. Make sure you're on the main branch.
 2. Change `ext.libraryVersion` in the root build.gradle to a non-SNAPSHOT version.
 3. Update CHANGELOG.md for the impending release.
 4. If necessary, update README.md.
 5. Commit (don't push) the changes with message "Release x.y.z", where x.y.z is the new version.
 6. Tag the commit `x.y.z`, where x.y.z is the new version.
 7. Change `ext.libraryVersion` in the root build.gradle to the next SNAPSHOT version.
 8. Commit the SNAPSHOT change.
 9. Push the 2 commits + 1 tag to origin/main.
10. Wait for the "release" Action to complete.
11. Visit [Sonatype Nexus](https://oss.sonatype.org/#stagingRepositories). Verify the artifacts,
    close the staging repository, and release the closed staging repository.
12. Create the release on GitHub with release notes copied from the changelog.

If step 9 fails, drop the Sonatype repo, fix the problem, commit, and start again at step 6.
