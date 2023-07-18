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
                this.SAK = Constants.hexStrToInt(hashMap.get("SAK"));
                this.ATQA = Constants.hexStrToIntArr(hashMap.get("ATQA"));
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void exportAsFlipperNfc(String customName) throws IOException {
        File flipperNfcFile;
        if (customName.equals("")) {
            flipperNfcFile = new File(this.getCreatedBy() + "-" + Constants.arrToHexString(this.getUID(), false, true) + ".nfc");
        } else {
            flipperNfcFile = new File(customName + ".nfc");
        }
        PrintStream fileStream = new PrintStream(flipperNfcFile);
        fileStream.println("Filetype: Flipper NFC Device");//Placeholders maybe for future verisons
        fileStream.println("Version: 3");
        fileStream.println("# Nfc device type can be UID, Mifare Ultralight, Mifare Classic or ISO15693");
        fileStream.println("Device type: " + "ISO14443");
        fileStream.println("# UID is common for all formats");
        fileStream.println("UID: " + Constants.arrToHexString(this.getUID(), true, true));
        fileStream.println("# ISO14443 specific fields");
        fileStream.println("ATQA: " + Constants.intToHexString(ATQA[1], true) + " " + Constants.intToHexString(ATQA[0], true));
        fileStream.println("SAK: " + Constants.intToHexString(SAK, true));
        fileStream.close();


        
        
    }
    public void exportAsProxmark3Dump(String customName) throws IOException {
        JSONObject proxmarkJson = new JSONObject();

        proxmarkJson.put("Created", this.getCreatedBy());
        proxmarkJson.put("FileType", "rfidcard");
        HashMap<String, String> card = new HashMap<String, String>();
        card.put("UID", Constants.arrToHexString(this.getUID(), false, true));
        card.put("ATQA", Constants.arrToHexString(ATQA, false, true));
        card.put("SAK", Constants.intToHexString(SAK, true));
        proxmarkJson.put("Card", card);

        File proxmarkJsonFile;
        if (customName.equals("")) {
            proxmarkJsonFile = new File(this.getCreatedBy() + "-" + card.get("UID").toUpperCase() + "-" + "dump.json");
        } else {
            proxmarkJsonFile = new File(customName + ".json");
        }
            
        FileWriter fileWriter = new FileWriter(proxmarkJsonFile);
        fileWriter.write(proxmarkJson.toJSONString());
        fileWriter.close();

        


    }

}
