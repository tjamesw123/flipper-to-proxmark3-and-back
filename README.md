# Flipper To Proxmark3 And Back

This tool is for switching nfc file formats between .nfc (Flipper NFC Format) and .json (Proxmark3 NFC Dump Format)

**Works for MIFARE 1k, 4k, Mini cards and Mifare Ultralight/NTAGS**

## How to use?

1. Download the latest jar file from the latest github release
2. Move the jar file to the file you wish to convert
3. Run the jar file in the command line in the following format
```
java -jar enter-jar-name-here.jar convert "flipper.nfc" | "proxmark3-dump.json" export "enter-file-name-here-with-extension-you-want-to-convert-to"
or
java -jar enter-jar-name-here.jar convert "flipper.nfc" | "proxmark3-dump.json" export default json | nfc
```
(2nd example is default mode)
Default mode allows for the corresponding automatic name generation format:

for .json: Proxmark3 | FlipperZero-(insert-uid-here)-dump.json

for .nfc: Proxmark3 | FlipperZero-(insert-uid-here).nfc

Import and export files have to have either .nfc or .json as the file extension otherwise the tool will not work

```
Some examples would be:
java -jar flippertoproxmark3andback.jar convert "flipper.nfc" export "proxmark3-dump.json"
java -jar flippertoproxmark3andback.jar convert "proxmark3-dump.json" export default nfc
```
<img width="743" alt="How to screenshot for program" src="https://github.com/tjamesw123/flipper-to-proxmark3-and-back/assets/94910672/3de79b31-d810-4bfe-be45-ab05a7574e92">
