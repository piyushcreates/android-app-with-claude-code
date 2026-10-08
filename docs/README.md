# Documentation

| Folder | What's in it |
|---|---|
| [`design/`](design/) | Google Stitch mockups and design system, the logo source files, and how the built app differs from the mockups ([design/README.md](design/README.md)). |
| [`release/`](release/) | Shipping to Google Play: [building-and-signing.md](release/building-and-signing.md) (upload key, signed bundle, versioning, release testing), [play-console.md](release/play-console.md) (every Console form with answers) and [privacy-policy.md](release/privacy-policy.md) (the app policy text). |

Outside `docs/`:
- [`../store/`](../store/): Play listing text (`listing.md`, checked by `check_listing.py`), app icon, feature graphic and screenshots.
- [`../scripts/create-upload-key.sh`](../scripts/create-upload-key.sh): creates a Play upload key and the matching `keystore.properties`.
