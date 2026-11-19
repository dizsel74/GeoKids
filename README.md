# GeoKids 🔺🟦⚪

A small Android game that helps kids ages 8–9 learn geometric shapes by playing.

I built it for my daughter. She was learning shapes at school, and I wanted something she'd actually choose to play at home. Watching her use it changed a lot of the design: less text, bigger feedback, and shorter rounds so she doesn't lose interest.

## Screenshots

| Home | Game | Feedback |
|------|------|----------|
| ![Home](screenshots/home.png) | ![Game](screenshots/game.png) | ![Feedback](screenshots/feedback.png) |

## What kids do

- Identify shapes like triangles, squares, circles and rectangles
- Get instant visual feedback on every answer
- Play short rounds designed for a kid's attention span

## Built with

- Kotlin (native Android)
- Google AI Studio and the Gemini API

## What I learned

Kids don't read instructions, they tap everything. Every screen had to explain itself through visuals, and every action needed a clear, immediate response.

## Run locally

**Prerequisites:** [Android Studio](https://developer.android.com/studio)

1. Open Android Studio and select **Open**, then choose this project's folder
2. Let Android Studio fix any incompatibilities during import
3. Create a `.env` file in the project folder and set `GEMINI_API_KEY` to your Gemini API key (see `.env.example`)
4. Remove this line from `build.gradle.kts`: `signingConfig = signingConfigs.getByName("debugConfig")`
5. Run the app on an emulator or a physical device

---

Made by [Arturo Mendoza](https://iamuxui.dev), UX/UI designer.
