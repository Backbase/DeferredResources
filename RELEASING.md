# Releasing

In all steps, replace "x.y.z" with the actual version being released.

 1. From the latest main branch commit, create a `release/x.y.z` branch.
 2. Change `libraryVersion` in gradle.properties to non-SNAPSHOT version x.y.z.
 3. Update CHANGELOG.md for the impending release. Describe all consumer-facing changes since the
    previous release.
 4. If necessary, update README.md and docs/index.md.
 5. Commit the changes with message "Release x.y.z".
 6. Open a PR to the main branch titled "Release x.y.z". Obtain approval and squash/merge.
 7. Tag the merged PR commit `x.y.z` and push the tag.
 8. Wait for the "release" Action to complete.
 9. Log in to Sonatype Nexus and close and release the staging repository.
10. Create the release on GitHub with release notes copied from the changelog.

If step 10 fails; drop the Sonatype repo, delete the tag, fix the problem, and start again.

[`startship`](https://github.com/saket/startship) can be installed via Homebrew. You must have
`backbaseOssSonatypeUsername` and `backbaseOssSonatypePassword` defined in your machine's
gradle.properties file to release. Alternatively, you could visit [Sonatype Nexus OSS](https://oss.sonatype.org/#stagingRepositories)
while logged in as Backbase's username to manually drop/promote/release repositories.
