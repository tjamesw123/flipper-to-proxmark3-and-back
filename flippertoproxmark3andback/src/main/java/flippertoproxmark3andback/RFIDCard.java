package flippertoproxmark3andback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import flippertoproxmark3andback.Constants.FileType;
import flippertoproxmark3andback.Constants.KeyType;
import flippertoproxmark3andback.Constants.NfcType;

public class RFIDCard extends NFC {

    //ISO14443(ID cards rfid) Specific fields//TODO: potentially split nfc into different classes and make a detector method that returns the type of nfc object required
    private int[] ATQA;//Saved in Proxmark format (Note: remember to switch to flipper format for flipper stuff) 
    private int SAK;

    public int[] getATQA() {
        return ATQA;
    }
    public int getSAK() {
        return SAK;
    }

    public RFIDCard(File file) throws FileNotFoundException, org.json.simple.parser.ParseException {
        super(file);
        System.out.println("RFIDCard!");
        if (FileType.flipper == this.getImportFileType()) {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < 6; i++) {
                scan.nextLine();
            }
            //System.out.println(scan.nextLine());// ISO14443 specific fields
            scan.nextLine();

            String[] ATQA = scan.nextLine().substring(6).split(" ");//ATQA: inserthere
            // for (String s : ATQA) {
            //     System.out.println(s);
            // }
            this.ATQA = new int[]{Integer.decode("0x" + ATQA[1]), Integer.decode("0x" + ATQA[0])};//again formatted proxmark3 style
            this.SAK = Integer.decode("0x"+scan.nextLine().substring(5));//SAK: inserthere
            scan.close();
        } else {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(file))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                
                JSONObject nfcArray = (JSONObject) obj;
                HashMap<String, String> hashMap = (HashMap) nfcArray.get("Card");
                if (hashMap.containsKey("SAK")) {
                    this.SAK = Constants.hexStrToInt(hashMap.get("SAK"));
                    this.ATQA = Constants.hexStrToIntArr(hashMap.get("ATQA"));
                } else {//Ultralight
                    this.SAK = Constants.hexStrToInt("00");
                    this.ATQA = new int[]{Constants.hexStrToInt("44"), Constants.hexStrToInt("00")};
                }
                
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public File exportAsFlipperNfc(String customName) throws IOException {
        File flipperNfcFile;
        if (customName.equals("")) {
            flipperNfcFile = new File(this.getCreatedBy() + "-" + Constants.arrToHexString(this.getUID(), false, true) + ".nfc");
        } else {
            flipperNfcFile = new File(customName + ".nfc");
        }
        PrintStream fileStream = new PrintStream(flipperNfcFile);
        fileStream.println("Filetype: Flipper NFC device");//Placeholders maybe for future verisons
        fileStream.println("Version: 3");
        fileStream.println("# Nfc device type can be UID, Mifare Ultralight, Mifare Classic or ISO15693");
        fileStream.println("Device type: " + "ISO14443");
        fileStream.println("# UID is common for all formats");
        fileStream.println("UID: " + Constants.arrToHexString(this.getUID(), true, true));
        fileStream.println("# ISO14443 specific fields");
        fileStream.println("ATQA: " + Constants.intToHexString(ATQA[1], true, 2) + " " + Constants.intToHexString(ATQA[0], true, 2));
        fileStream.println("SAK: " + Constants.intToHexString(SAK, true, 2));
        fileStream.close();
        return flipperNfcFile;


        
        
    }
    public File exportAsProxmark3Dump(String customName) throws IOException {
        File proxmarkJsonFile;
        if (customName.equals("")) {
            proxmarkJsonFile = new File(this.getCreatedBy() + "-" + Constants.arrToHexString(this.getUID(), false, true) + "-" + "dump.json");
        } else {
            proxmarkJsonFile = new File(customName + ".json");
        }
        PrintStream fileStream = new PrintStream(proxmarkJsonFile);
        fileStream.println("{");
        fileStream.println(" \"Created\": \""+this.getCreatedBy()+"\",");
        fileStream.println(" \"FileType\": \"" + "rfidcard" + "\",");
        fileStream.println(" \"Card\": {");
        fileStream.println("   \"UID\": \"" + Constants.arrToHexString(this.getUID(), false, true) + "\",");
        fileStream.println("   \"ATQA\": \"" + Constants.arrToHexString(ATQA, false, true) + "\",");
        fileStream.println("   \"SAK\": \"" + Constants.intToHexString(SAK, true, 2) + "\"");
        fileStream.println(" }");
        fileStream.print("}");
        fileStream.close();
        return proxmarkJsonFile;

        


    }

}
