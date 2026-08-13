<p align="center">

  <img src="assets/radioforge-logo.png" alt="RADIOFORGE" width="360">
</p>

# 📡 RADIOFORGE

**Private Network Observatory — Collect → Understand → Replay → Compare**

RADIOFORGE turns an Android phone into an offline-first network telemetry recorder. It collects the radio/cell information that Android exposes to ordinary applications, records sessions locally, and exports an offline archive containing raw JSONL telemetry plus an HTML report.

## What it does

- LTE / 5G NR / legacy cellular telemetry when the device and Android expose it.
- Cell identity and signal fields such as CI/NCI, PCI, TAC, EARFCN/NRARFCN, RSRP, RSRQ, RSSI/RSSNR and SS-SINR where available.
- Active transport, local address, optional Wi-Fi SSID, and public IP.
- Five-second polling with timestamped JSONL session storage.
- Foreground capture service for long sessions.
- Offline export to ZIP with JSONL and HTML report.
- No backend, account, analytics SDK, or automatic cloud upload.
- App-private storage by default.

## Android permissions

RADIOFORGE asks for fine location because Android protects modern CellInfo access behind location permission. The app also requests phone state, network, Wi-Fi, foreground-service and notification permissions as needed by the platform version.

Exact location is not included in the telemetry payload in this build. The project intentionally separates radio telemetry from precise coordinates.

## Build

This project is pinned to Android Gradle Plugin 8.13.2 and Gradle 8.13. Open the project in a current Android Studio or run `./gradlew assembleDebug` on a machine with Android SDK 36 and JDK 17+.

## Install

After building:

`adb install -r app/build/outputs/apk/debug/app-debug.apk`

On the phone, open RADIOFORGE, grant the requested permissions, then press **START**.

## Export

Press **EXPORT** and save `radioforge-session.zip`. The ZIP contains:

- `session.jsonl` — raw local telemetry
- `report.html` — an offline summary
- `README.txt` — archive notes

## Important limitation

This project does **not** record raw RF/IQ samples. Standard Android application APIs expose interpreted cell/measurement data rather than arbitrary modem baseband samples. Raw RF capture requires specialized radio hardware and is outside the scope of a normal Android app.

## Privacy & security

All captures are stored under the app-private `files/sessions/` directory. No third-party analytics library is used. Release builds enable R8 shrinking/obfuscation. This does not make public source impossible to copy: repository visibility and copyright/license terms are separate concepts.

## License

Copyright (c) 2026 RADIOFORGE contributors. All Rights Reserved. See `LICENSE` for the repository's restrictive source-availability terms.
