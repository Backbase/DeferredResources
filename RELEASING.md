# Releasing

 1. Make sure you're on the main branch.
 2. Change `ext.libraryVersion` in the root build.gradle to a non-SNAPSHOT version.
 3. Update CHANGELOG.md for the impending release.
 4. If necessary, update README.md and docs/index.md.
 5. Commit (don't push) the changes with message "Release x.y.z", where x.y.z is the new version.
 6. Tag the commit `x.y.z`, where x.y.z is the new version.
 7. Change `ext.libraryVersion` in the root build.gradle to the next SNAPSHOT version.
 8. Commit the SNAPSHOT change.
 9. Push the 2 commits + 1 tag to origin/main.
10. Wait for the "release" Action to complete.
11. `startship release -u backbase -p backbaseOssSonatypePassword -c com.backbase.oss.deferredresources:deferred-resources,deferred-resources-view-extensions:x.y.z`
12. Create the release on GitHub with release notes copied from the changelog.

If step 10 fails, drop the Sonatype repo, fix the problem, commit, and start again at step 6.

[`startship`](https://github.com/saket/startship) can be installed via Homebrew. You must have
`backbaseOssSonatypePassword` defined in your machine's gradle.properties file to release.
