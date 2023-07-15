package flippertoproxmark3andback;

import flippertoproxmark3andback.Constants.FileType;
import flippertoproxmark3andback.Constants.KeyType;
import flippertoproxmark3andback.Constants.NfcType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

//import java.util.ArrayList;

import flippertoproxmark3andback.Key;
public class NfcDumpConverter {
    
    public static void main(String[] args) throws IOException, ParseException {
        for (String s : args) {
            System.out.println(s);
        }
        if (args.length <= 0 || args[0].equals("help")) {//Note "" in command line just don't exist after inputing them
            help();
        } else if (args[0].equals("convert") ) {
            System.out.println("Convert!");
            
            System.out.println(args[1] + "!");
            String filePathInput = args[1].replaceAll("\"", "");
            Nfc nfc = new Nfc();
            if (new File(filePathInput).exists()) {
                nfc = new Nfc(Constants.fileExtensionToFileType.get(filePathInput.substring(filePathInput.indexOf(".")+1)), new File(filePathInput));
            } else {
                System.out.println("Invalid input file path");
                System.exit(0);
            }
            
            
            if (args[2].equals("export")) {
                System.out.println("Export!");
                if (!args[3].equals("default")) {
                    System.out.println(args[3] + "!");
                    String filePathOutput = args[3].replaceAll("\"", "");
                    if (Constants.fileExtensionToFileType.get(filePathOutput.substring(filePathOutput.indexOf(".")+1)) == FileType.flipper) {//uses file extension to id what type of file format to export to
                        nfc.exportAsFlipperNfc(filePathOutput.substring(0, filePathOutput.indexOf(".")));
                    } else if (Constants.fileExtensionToFileType.get(filePathOutput.substring(filePathOutput.indexOf(".")+1)) == FileType.proxmark3) {
                        nfc.exportAsProxmark3Dump(filePathOutput.substring(0, filePathOutput.indexOf(".")));
                    }
                } else if (args[3].equals("default")) {
                    if (args[4].equals("nfc")) {
                        nfc.exportAsFlipperNfc("");
                    } else if (args[4].equals("json")) {
                        nfc.exportAsProxmark3Dump("");
                    } else {
                        help();
                    }
                } else {
                    help();
                }
                    
            } else {
                help();
            }
            
        }

        // java -jar flippertoproxmark3andback.jar convert "flipper.nfc" | "proxmark3-dump.json" export "enter-file-name-here-with-extension-you-want-to-convert-to" (Either .nfc or .json)

    }

    private static void help() {
        System.out.println("This tool is for switching nfc file formats between .nfc (Flipper NFC Format) and .json (Proxmark3 NFC Dump Format)");
        System.out.println("Currently only works for MIFARE 1k cards");
        System.out.println("convert \"flipper.nfc\" | \"proxmark3-dump.json\" export \"enter-file-name-here-with-extension-you-want-to-convert-to\" (Either .nfc or .json)");
        System.out.println("Also\nconvert \"flipper.nfc\" | \"proxmark3-dump.json\" export \"default\" nfc(.nfc) | json(.json)");
    }
}