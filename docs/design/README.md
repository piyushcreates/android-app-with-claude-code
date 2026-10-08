# Design

| File | What it is |
|---|---|
| `stitch/` | Google Stitch output: Today, Log, History and Insights mockups (`*.png`) and the "Serene Clinical" design system (`DESIGN.md`). |
| `logo/` | Brand logo sources: `logo.svg` (master), `logo-512.png`, `icon-192.png`, `apple-touch-icon.png`, `favicon.svg`. |

## How the app uses these
- **Palette and type** come from the Stitch design system: `app/src/main/java/com/gutelements/app/ui/theme/`.
- **Logo:** `logo.svg` was converted to Android vector drawables: `app/src/main/res/drawable/ic_logo.xml` (in-app) and `ic_launcher_*.xml` (launcher icon and splash). The Play icon `store/graphics/play-icon-512.png` is a full-square render of the same SVG.

## Where the app differs from the Stitch mockups
Stitch added things the briefs explicitly exclude, so the app keeps the Stitch *look* but follows the briefs for *content and structure*:
- Removed the extra fields: "Transit Experience" (Normal / Straining / Urgent), "Quick Observation" notes and entry tags.
- Removed judgment labels ("Optimal", "Clinical Grade", "Hard/Loose spectrum").
- No traffic-light colours: every Bristol type uses the same neutral clay illustration, and the selected state is sage.
- Removed the "Synchronized" badge and profile icon, since there are no accounts or sync.
- Today follows the brief: "How's your gut today?", a today card, one primary Log button, a two-metric "This week" card and today's entries.
- History uses a Calendar | List segmented control and a minimal bottom-sheet editor.
- Onboarding, Settings and the edit sheet weren't in the Stitch output; they were designed in the same style.
