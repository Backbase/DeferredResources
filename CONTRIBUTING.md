# Contributing

If you find a bug or would like to request a feature, please file an
[issue](https://github.com/Backbase/DeferredResources/issues). If you would like to contribute a
documentation or code change, you can do so through GitHub by forking the repository and sending a
pull request.

## Code contributions

Get working code on a personal branch with tests passing before you submit a PR. You must have an
Android device connected to run tests.
```shell script
./gradlew clean build connectedCheck
```

If you change the public API by breaking it _or_ adding to it, the build will fail. Breaking public
API changes will generally not be accepted, but additions to the public API may be. If the API
change is intentional, run the following command and include the `.api` file changes in the PR:
```shell script
./gradlew apiDump
```

When submitting code, please make every effort to follow existing conventions and style in order to
keep the code as readable as possible. If you're using Android Studio or IntelliJ IDEA, the project
code style definitions are included in the project.

We squash all pull requests on merge.
