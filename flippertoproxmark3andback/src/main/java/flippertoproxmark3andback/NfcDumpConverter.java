package flippertoproxmark3andback;

import flippertoproxmark3andback.Constants.FileType;
import flippertoproxmark3andback.Constants.KeyType;
import flippertoproxmark3andback.Constants.NfcType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import flippertoproxmark3andback.Key;
public class NfcDumpConverter {
    
    public static void main(String[] args) throws IOException, ParseException {//TODO: maybe add an import and export option for binary files?
        // for (String s : args) {
        //     System.out.println(s);
        // }
        //args = new String[]{"convert", "hf-mf-894F163F-dump.json", "export", "default", "nfc"};
        //args = new String[]{"convert", "FlipperZero-B4CE3F1B-dump.json", "export", "default", "nfc"};//FlipperZero-894F163F-dump.json
        //args = new String[]{"convert", "FlipperZero-894F163F-dump.json", "export", "default", "nfc"};
        //proxmark3-894F163F.nfc
        //args = new String[]{"convert", "hf-mf-894F163F-dump.json", "export", "default", "nfc"};
        //args = new String[]{"convert", "hf-mf-894F163F-dump.nfc", "export", "default", "json"};
        //args = new String[]{"convert", "proxmark3-894F163F.nfc", "export", "default", "json"};
        //args = new String[]{"convert", "Mifare_4k.nfc", "export", "default", "json"};
        //args = new String[]{"convert", "FlipperZero-046E0D85F0E3E4-dump.json", "export", "default", "nfc"};//Mini.nfc
        //args = new String[]{"convert", "Mini.nfc", "export", "default", "json"};
        //args = new String[]{"convert", "Mini.nfc", "export", "default", "nfc"};
        //args = new String[]{"convert", "Test04u.nfc", "export", "default", "nfc"};
        //args = new String[]{"convert", "Test04u.nfc", "export", "default", "json"};//FlipperZero-04548352951190-dump.json
        //args = new String[]{"convert", "FlipperZero-04548352951190-dump.json", "export", "default", "nfc"};//N203.nfc
        //args = new String[]{"convert", "N203.nfc", "export", "default", "json"};
        //args = new String[]{"convert", "N215.nfc", "export", "default", "json"};

        //System.out.println(Constants.flipperFileToCleanedListOfLines(new File("proxmark3-894F163F.nfc")));
        // ArrayList<String> arr1 = Constants.flipperFileToCleanedListOfLines(new File("N203.nfc"));
        // for (String s : arr1) {
        //     System.out.println(s);
        // }
        runProgramWithArguments(args);

       

        // java -jar flippertoproxmark3andback.jar convert "flipper.nfc" | "proxmark3-dump.json" export "enter-file-name-here-with-extension-you-want-to-convert-to" (Either .nfc or .json)

    }
    private static void runProgramWithArguments(String[] args) throws ParseException, IOException {
         if (args.length <= 0 || args[0].equals("help")) {//Note "" in command line just don't exist after inputing them
            help();
        } else if (args[0].equals("convert") ) {
            System.out.println("Convert!");
            
            System.out.println(args[1] + "!");
            String filePathInput = args[1].replaceAll("\"", "");
            NFC nfc = new NFC();
            if (new File(filePathInput).exists()) {
                nfc = NFCFactory.createNFCFromFile(new File(filePathInput));
                //nfc = new MifareClassic(new File(filePathInput));
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
                        System.out.println(nfc.exportAsFlipperNfc(filePathOutput.substring(0, filePathOutput.indexOf("."))).getName() + "!");
                    } else if (Constants.fileExtensionToFileType.get(filePathOutput.substring(filePathOutput.indexOf(".")+1)) == FileType.proxmark3) {
                        System.out.println(nfc.exportAsProxmark3Dump(filePathOutput.substring(0, filePathOutput.indexOf("."))).getName() + "!");
                    }
                } else if (args[3].equals("default")) {
                    if (args[4].equals("nfc")) {
                        System.out.println(nfc.exportAsFlipperNfc("").getName() + "!");
                    } else if (args[4].equals("json")) {
                        System.out.println(nfc.exportAsProxmark3Dump("").getName() + "!");
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
    }

    private static void help() {
        System.out.println("This tool is for switching nfc file formats between .nfc (Flipper NFC Format) and .json (Proxmark3 NFC Dump Format)");
        System.out.println("Currently only works for MIFARE 1k, 4k, Mini cards and Mifare Ultralight/NTAGS");
        System.out.println("convert \"flipper.nfc\" | \"proxmark3-dump.json\" export \"enter-file-name-here-with-extension-you-want-to-convert-to\" (Either .nfc or .json)");
        System.out.println("Also\nconvert \"flipper.nfc\" | \"proxmark3-dump.json\" export \"default\" nfc(.nfc) | json(.json)");
    }
}