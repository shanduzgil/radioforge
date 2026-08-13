# RADIOFORGE Architecture

```text
Android Activity
      |
      +---- Permission Gate
      |
      +---- Dashboard
      |
      +---- CaptureService (foreground)
                |
                +---- Collector
                |       +---- TelephonyManager
                |       +---- ConnectivityManager
                |       +---- WifiManager
                |
                +---- CellParser
                |
                +---- app-private JSONL session
                |
                +---- Offline ZIP export
```

The key design choice is to talk to Android Telephony APIs directly instead of depending on a shell-only bridge. Android's current TelephonyManager API provides `requestCellInfoUpdate()` for observed, serving and neighboring cells, subject to platform permissions and modem support.
