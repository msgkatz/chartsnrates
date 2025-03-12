# cnr-repos

Kotlin Multiplatform Library

### Publish to MavenCentral

1) Registering a Sonatype account as described here: 
   https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k
2) Add developer id, name, email and the project url to
   `/convention-plugins/src/main/kotlin/convention.publication.gradle.kts`
3) Add the secrets to `local.properties`:

```
signing.keyId=...
signing.password=...
signing.secretKeyRingFile=...
ossrhUsername=...
ossrhPassword=...
```

4) Run `./gradlew :cnr-repos:publishAllPublicationsToSonatypeRepository`

### Build platform artifacts

#### Android aar

- Run `./gradlew :cnr-repos:assembleRelease`
- Output: `/cnr-repos/build/outputs/aar/cnr-repos-release.aar`

#### JVM jar

- Run `./gradlew :cnr-repos:jvmJar`
- Output: `/cnr-repos/build/libs/cnr-repos-jvm-1.0.jar`

#### iOS Framework

- Run `./gradlew :cnr-repos:linkReleaseFrameworkIosArm64`
- Output: `/cnr-repos/build/bin/iosArm64/releaseFramework/cnr-repos.framework`

#### JS file

- Run `./gradlew :cnr-repos:jsBrowserProductionWebpack`
- Output: `/cnr-repos/build/dist/js/productionExecutable/cnr-repos.js`

#### Wasm binary file

- Run `./gradlew :cnr-repos:wasmJsBrowserDistribution`
- Output: `/cnr-repos/build/dist/wasmJs/productionExecutable/cnr-repos-wasm-js.wasm`
