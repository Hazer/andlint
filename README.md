# lewis
Rock your Android

## Purpose

This is an extension for Android Lint, adding new rules:
* _RootPackage_ : Every .java file must be inside a custom package, not inside the root package. `ERROR`
* _MissingLauncher_ : Every application must have at least one launcher activity. `WARNING`
* _MoreThanOneLauncher_ : The application must have only one launcher activity. `WARNING`
* _LauncherActivityInLibrary_ : A library must not have a launcher activity. `ERROR`
* _IconInLibrary_ : A library must not have icons. `ERROR`
* _PermissionUsageInLibrary_ : A library must not use permissions. `ERROR`
* _InstanceVariableName_ : Every instance variable must begin with 'm' and be written using camelCase (except in model classes). `WARNING`
* _StaticFinalVariableName_ : A static final variable should be named using UPPER_SNAKE_CASE. `WARNING`

## Getting started

Clone this repository
```bash
git clone https://github.com/inaka/lewis.git
```

Enter to the project
```bash
cd lewis/
```

Build it
```bash
./gradlew build
```

Copy the .jar file to the lint folder
```bash
cp ./build/libs/lewis.jar ~/.android/lint/
```

Verify whether the issues are registered with lint
```bash
lint --show RootPackage
```

Run lint
```bash
./gradlew lint
```
>   Note: If you can't run `lint` directly, you may want to include android tools `PATH` in your `~/.bash_profile`. (i.e. `PATH=$PATH:~/Library/Android/sdk/tools`)
>
>    Then run `source ~/.bash_profile`.

#### How to disable an issue
 Add on your `build.gradle` file
```groovy
android {
    lintOptions {
        disable 'RootPackage','MoreThanOneLauncher'
        ...
    }
}
```