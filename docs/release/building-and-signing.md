# Building and signing release versions

How to produce the signed bundle you upload to Play. The Play Console forms are covered in [play-console.md](play-console.md).

## 1. Create the upload key (once)

Google Play signs the app you ship to users with its own **app signing key** (Play App Signing). You sign each upload with your **upload key**. If the upload key is ever lost, Google can reset it, but that takes time, so back it up.

Create it yourself, so only you know the password:

```bash
./scripts/create-upload-key.sh
```

The script asks for a password twice to confirm it, then your name or organisation (press Enter to skip any). It creates `keystore/gutelements-upload.jks`, checks the password opens it, and writes `keystore.properties` for the build. Both files are git-ignored.

**Back up** the `.jks` file and its passwords in a password manager or another secure place outside this machine.

## 2. Build the release bundle

```bash
./gradlew :app:testDebugUnitTest :app:lintRelease :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`. Without `keystore.properties` the bundle is built **unsigned** and Play Console will reject it.

The release build is shrunk and obfuscated with R8. Its deobfuscation map is packed into the bundle automatically (`BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map`), so Play can decode crash reports without a separate upload. A copy is also written to `app/build/outputs/mapping/release/mapping.txt`.

## 3. Every new version

In `app/build.gradle.kts`:
- `versionCode`: increase by at least 1. Play rejects a repeat.
- `versionName`: the version users see, e.g. `1.0.1`.

## 4. Before each upload
- [ ] Unit tests and lint pass (command above)
- [ ] Install a release build on a real phone (see "Testing a release build locally" below) and check onboarding, logging, history, edit/delete, insights and Settings links
- [ ] If the database schema changed, add a Room migration and commit the new file in `app/schemas/`. Never ship a destructive migration: users' only copy of their data is on the device.

## Testing a release build locally
Minification can break code paths that only show up at runtime, so test the release build itself, not just debug. To install it on a phone without the upload key, sign a scratch copy with the local debug key:

```bash
BT=~/Library/Android/sdk/build-tools/36.1.0
./gradlew :app:assembleRelease
$BT/zipalign -f -p 4 app/build/outputs/apk/release/app-release-unsigned.apk /tmp/aligned.apk
$BT/apksigner sign --ks ~/.android/debug.keystore --ks-pass pass:android --ks-key-alias androiddebugkey --key-pass pass:android --out /tmp/release-test.apk /tmp/aligned.apk
adb install -r /tmp/release-test.apk
```

## Notes
- `applicationId` is `com.gutelements.app`. **It can never change once published.**
- Builds target API 36 and need a minimum of API 26 (Android 8.0).
- Installing from Play over a locally installed debug build fails because the signatures differ. Uninstall the local build first.
