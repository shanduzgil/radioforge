from pathlib import Path
import re

root=Path(__file__).resolve().parents[1]
required=[
    'settings.gradle','build.gradle','gradle.properties','app/build.gradle',
    'app/src/main/AndroidManifest.xml',
    'app/src/main/java/com/radioforge/observatory/MainActivity.java',
    'app/src/main/java/com/radioforge/observatory/CaptureService.java',
    'app/src/main/java/com/radioforge/observatory/Collector.java',
    'app/src/main/java/com/radioforge/observatory/CellParser.java',
]
missing=[x for x in required if not (root/x).exists()]
if missing: raise SystemExit('Missing: '+', '.join(missing))
text=(root/'app/src/main/AndroidManifest.xml').read_text()
for perm in ['ACCESS_FINE_LOCATION','READ_PHONE_STATE','FOREGROUND_SERVICE','FOREGROUND_SERVICE_LOCATION','POST_NOTIFICATIONS']:
    assert perm in text, perm
java=(root/'app/src/main/java/com/radioforge/observatory/CellParser.java').read_text()
for needle in ['CellInfoLte','CellInfoNr','getRsrp','getRsrq','getRssnr','getSsSinr']:
    assert needle in java, needle
print('RADIOFORGE structure checks: PASS')
