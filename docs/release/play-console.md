# Play Console setup — Gut Elements

Step-by-step guide for publishing to Google Play. Play Console changes its wording now and then. If a question doesn't match these notes, answer from the facts below.

## What the app does (answer every form from these facts)
- Stores Bristol type plus date/time logs **only on the device** (Room/SQLite). Android cloud backup and device transfer are disabled.
- **No `INTERNET` permission.** The merged release manifest requests no permissions apart from an internal AndroidX one, so the app cannot send data anywhere.
- No account or login, no payments, no ads, no advertising ID, and no analytics, crash reporting or other third-party SDKs.
- Links out to gutelements.com and the privacy policy in the browser.
- Not a medical device. Makes no diagnosis or treatment claims.

## Key values
| | |
|---|---|
| Developer account | Your Play developer account (organisation accounts skip the 12-tester / 14-day closed test that new personal accounts need) |
| App name | `Gut Elements: Bowel Tracker` |
| Package name | `com.gutelements.app`. Permanent after the first upload. |
| Privacy policy | https://gutelements.com/app-privacy (text in [privacy-policy.md](privacy-policy.md)) |
| Support email | hello@gutelements.com |
| Website | https://gutelements.com |
| Upload file | `app/build/outputs/bundle/release/app-release.aab` (build it with [building-and-signing.md](building-and-signing.md)) |

## 1. Create the app
Play Console → your developer account → **Create app**:
- App name: `Gut Elements: Bowel Tracker`
- Default language: **English (United States)**. The listing copy uses US spelling ("fiber").
- App or game: **App**. Free or paid: **Free**. A free app can't become paid later.
- Tick both declarations → **Create app**

The app's **Dashboard** shows a "Set up your app" checklist. Each item links to its form.

## 2. App content (Policy and programs → App content)
| Form | Answer |
|---|---|
| **Privacy policy** | `https://gutelements.com/app-privacy` |
| **Sign-in details** (previously "App access") | "Is any part of your app restricted?" → **No** |
| **Ads** | **No**, my app does not contain ads |
| **Content rating** | Email hello@gutelements.com, choose the non-game / "all other app types" category, and answer **No** to every question (violence, sexual content, gambling, drugs, user interaction, location sharing, purchases). Expect the lowest rating. |
| **Target audience and content** | Tick **18 and over** only. Leave **"Restrict users that Google has determined to be minors"** *unticked*. It's optional and would hide the app from under-18s. If asked whether the app appeals to children → **No**. |
| **News app** | No |
| **Data safety** | "Does your app collect or share any required user data types?" → **No** (see below) |
| **Government app** | No |
| **Financial features** | None |
| **Health apps** | See below |
| **Advertising ID** | **No**, the app doesn't use it |

### Data safety
- Answer **No**: data that stays on the device and is never sent off it doesn't count as "collected", and Android enforces this because the app has no internet permission.
- If asked how users delete data: in the app via **Settings → Delete all local data**, or by uninstalling.
- **Keep it in sync.** If a later version adds analytics, crash reporting, cloud sync or anything that sends data off the device, this form, the privacy policy and the listing's "Private by design" claims must change *before* that version ships.

### Health apps
- **Step 1, Health features:** tick **Other** only, with this description:
  > Personal bowel movement tracking: users log stool type (Bristol Stool Scale 1–7) with date and time, review their history and see simple statistics. Data stays on the device. Not a medical device; no diagnosis or treatment.
- Don't tick **Diseases and conditions management**, **Medical device apps** or **Medical reference and education**. They describe the app as a medical tool and bring stricter review or regulatory questions.
- Don't tick **My app does not have any health features**. That's not true for this app.
- **Step 2, Regional requirements:** if asked whether the app is a regulated medical device or needs clearance anywhere → **No / Not applicable**.

## 3. Store settings and store listing
**Store settings** (App category and contact details):
- Category: **Health & Fitness**
- Email: hello@gutelements.com. Website: https://gutelements.com

**Main store listing** (Grow users → Store presence → Store listings):
- Text: copy from [`store/listing.md`](../../store/listing.md). Run `python3 store/check_listing.py` after any edit to check Play's character limits.
- Graphics are in the project's `store/` folder. In the Mac file picker, press **Cmd + Shift + G** and paste the full path to it.

| Slot | File | Size |
|---|---|---|
| App icon | `store/graphics/play-icon-512.png` | 512×512 |
| Feature graphic | `store/graphics/feature-graphic-1024x500.png` | 1024×500 |
| Phone screenshots | `store/screenshots/01-today.png` … `05-scale.png` (in order) | 1080×1920 each |

Tablet and Chromebook screenshots are optional, so skip them.

## 4. Upload and release
1. **Test and release → Production → Countries/regions**: add the countries you want.
2. **Production → Create new release**. Accept **Play App Signing** if asked.
3. Upload `app/build/outputs/bundle/release/app-release.aab`.
4. Release name: leave the default (`1 (1.0.0)`).
5. Release notes:
   ```
   First release of Gut Elements: log bowel movements with the Bristol Stool Scale in seconds, review your history, and see simple insights. No account needed — your data stays on your device.
   ```
6. **Next**, then review the warnings. No separate deobfuscation file is needed: the R8 mapping is already inside the bundle (`BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map`) and Play reads it automatically. A "deobfuscation file" reminder won't block publishing.
7. **Save**, then **Send for review** (or **Send changes for review** on the Publishing overview page).

Review usually takes a few days, sometimes longer for a new app or a health app. The app goes live automatically once approved, unless managed publishing is on.

**Where things are:** releases and bundles are under **Test and release → Latest releases and bundles** (previously "App bundle explorer"). Check the **pre-launch report** on every upload; it runs the app on real devices and flags crashes and accessibility issues.

## After it's live
- **Your test phone:** uninstall any locally installed build before installing from Play, because the signatures differ. That deletes the entries on the phone.
- **Updates:** see [building-and-signing.md](building-and-signing.md). Bump `versionCode`, rebuild and upload a new release. Use a staged rollout (e.g. 20% → 50% → 100%) and watch Android vitals.
