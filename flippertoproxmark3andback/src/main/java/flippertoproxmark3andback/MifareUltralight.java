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
import flippertoproxmark3andback.Constants.MifareClassicType;
import flippertoproxmark3andback.Constants.MifareUltralightType;

public class MifareUltralight extends RFIDCard {//TODO: NEEDS TO BE WORKED ON
    private int[][] blocks;//Saved in Proxmark format (Note: remember to switch to flipper format for flipper stuff)
    private int[] signature;
    private int[] version;
    private int[] counter;
    private int[] tearing;
    private MifareUltralightType mifareUltralightType;
    // Second to last block (password) changes based on if the tag is flipper zero or proxmark3 in origin
    // FF FF FF FF (Flipper Zero) vs. 00 00 00 00 (proxmark3)
    // My guess is it has something to do with the password block being changed by flipper zero when saved to the file?
    // it's unclear. According to NFC tools it's 00 00 00 00 so this is flipper being a bit weird. 
    // should I change it back during the conversion is the question

    public MifareUltralight(File file) throws FileNotFoundException, org.json.simple.parser.ParseException {
        super(file);
        System.out.println("MifareUltralight!");
        if (FileType.flipper == this.getImportFileType()) {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < 9; i++) {
                scan.nextLine();
            }
            //System.out.println(scan.nextLine());// Mifare Ultralight specific data
            scan.nextLine();
            //System.out.println(mifareClassicType);
            //System.out.println(scan.nextLine());//Data format version: 1 TODO: keep up with the data format versions
            scan.nextLine();
            signature = Constants.hexStrArrtoIntArr(scan.nextLine().substring(11).split(" "));//Signature: 
            version = Constants.hexStrArrtoIntArr(scan.nextLine().substring(16).split(" "));//Mifare version:
            counter = new int[3];
            tearing = new int[3];
            counter[0] = Integer.parseInt(scan.nextLine().substring(11));//Counter 0:
            tearing[0] = Constants.hexStrToInt(scan.nextLine().substring(11));//Tearing 0:
            counter[1] = Integer.parseInt(scan.nextLine().substring(11));//Counter 1:
            tearing[1] = Constants.hexStrToInt(scan.nextLine().substring(11));//Tearing 1:
            counter[2] = Integer.parseInt(scan.nextLine().substring(11));//Counter 2:
            tearing[2] = Constants.hexStrToInt(scan.nextLine().substring(11));//Tearing 2:
            int pagesTotal = Integer.parseInt(scan.nextLine().substring(13));//Pages total:
            scan.nextLine();//Pages read:
            blocks = new int[pagesTotal][4];
            for (int i = 0; i < blocks.length; i++) {
                String line = scan.nextLine();
                blocks[i] = Constants.hexStrArrtoIntArr(line.substring(line.indexOf(":")+2).split(" "));
            }
            mifareUltralightType = Constants.mfuVersionPlusPageTotalToMfuType.get(Constants.arrToHexString(version, false, true) + " " + pagesTotal);
            System.out.println(Constants.arrToHexString(version, false, true));
            System.out.println(pagesTotal);
            System.out.println(mifareUltralightType);



            
        } else {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(file))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                
                JSONObject nfcArray = (JSONObject) obj;
                HashMap<String, String> hashMap = (HashMap) nfcArray.get("Card");
                HashMap<String, String> hashMapBlocks = (HashMap) nfcArray.get("blocks");
                signature = Constants.hexStrToIntArr(hashMap.get("Signature")); 
                version = Constants.hexStrToIntArr(hashMap.get("Version"));
                counter = new int[3];
                tearing = new int[3];
                counter[0] = Constants.hexStrToInt(hashMap.get("Counter0"));//Could be binary could be hex, hard to tell (Assuming it's hex at the minute) // it's hex
                tearing[0] = Constants.hexStrToInt(hashMap.get("Tearing0"));
                counter[1] = Constants.hexStrToInt(hashMap.get("Counter1"));
                tearing[1] = Constants.hexStrToInt(hashMap.get("Tearing1"));
                counter[2] = Constants.hexStrToInt(hashMap.get("Counter2"));
                tearing[2] = Constants.hexStrToInt(hashMap.get("Tearing2"));
                int pagesTotal = 0;
                for (int i = 0; hashMapBlocks.containsKey(""+i); i++) {
                    pagesTotal = i;
                }
                pagesTotal++;
                blocks = new int[pagesTotal][4];
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i] = Constants.hexStrToIntArr(hashMapBlocks.get(""+i));
                }
                mifareUltralightType = Constants.mfuVersionPlusPageTotalToMfuType.get(Constants.arrToHexString(version, false, true) + " " + pagesTotal);
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
        fileStream.println("Filetype: Flipper NFC device");//Placeholders maybe for future verisons
        fileStream.println("Version: 3");
        fileStream.println("# Nfc device type can be UID, Mifare Ultralight, Mifare Classic, FeliCa or ISO15693");
        fileStream.println("Device type: " + Constants.mfuTypeToFlipperDevice.get(mifareUltralightType));
        fileStream.println("# UID is common for all formats");
        fileStream.println("UID: " + Constants.arrToHexString(this.getUID(), true, true));
        fileStream.println("# ISO14443 specific fields");
        fileStream.println("ATQA: " + Constants.intToHexString(this.getATQA()[1], true, 2) + " " + Constants.intToHexString(this.getATQA()[0], true, 2));
        fileStream.println("SAK: " + Constants.intToHexString(this.getSAK(), true, 2));
        fileStream.println("# Mifare Ultralight specific data");
        fileStream.println("Data format version: 1");
        fileStream.println("Signature: " + Constants.arrToHexString(signature, true, true));
        fileStream.println("Mifare version: " + Constants.arrToHexString(version, true, true));
        fileStream.println("Counter 0: " + counter[0]);
        fileStream.println("Tearing 0: " + Constants.intToHexString(tearing[0], true));
        fileStream.println("Counter 1: " + counter[1]);
        fileStream.println("Tearing 1: " + Constants.intToHexString(tearing[1], true));
        fileStream.println("Counter 2: " + counter[2]);
        fileStream.println("Tearing 2: " + Constants.intToHexString(tearing[2], true));
        fileStream.println("Pages total: " + blocks.length);
        fileStream.println("Pages read: " + blocks.length);

        for (int i = 0; i < blocks.length; i++) {
            fileStream.println("Page " + i + ": " + Constants.arrToHexString(blocks[i], true, true));
        }
        fileStream.println("Failed authentication attempts: " + "0");
        //Placeholder as at the moment I'm figuring out how the auth attempts works 
        //sorta figured it out counter 0 (and sometimes the others) is 
        //incremented upwards when there is a failed attempt to auth
        //I think it might just start at zero no matter what tag you scan possibly after looking at the code of the flipper firmware 
        fileStream.close();
    }
    public void exportAsProxmark3Dump(String customName) throws IOException {//Keep an eye on TBO_0 and TBO_1
        JSONObject proxmarkJson = new JSONObject();

        proxmarkJson.put("Created", this.getCreatedBy());
        proxmarkJson.put("FileType", "mfu");
        HashMap<String, String> card = new HashMap<String, String>();
        card.put("UID", Constants.arrToHexString(this.getUID(), false, true));
        card.put("Version", Constants.arrToHexString(version, false, true));
        card.put("TBO_0", "0000");//Placeholder
        card.put("TBO_1", "00");
        card.put("Signature", Constants.arrToHexString(signature, false, true));
        card.put("Counter0", Constants.intToHexString(counter[0], false, 6));
        card.put("Tearing0", Constants.intToHexString(tearing[0], true, 2));
        card.put("Counter1", Constants.intToHexString(counter[1], false, 6));
        card.put("Tearing1", Constants.intToHexString(tearing[1], true, 2));
        card.put("Counter2", Constants.intToHexString(counter[2], false, 6));
        card.put("Tearing2", Constants.intToHexString(tearing[2], true, 2));
        proxmarkJson.put("Card", card);

        HashMap<String, String> blockHashMap = new HashMap<String, String>();
        for (int i = 0; i < blocks.length; i++) {
            blockHashMap.put(""+i, "" + Constants.arrToHexString(blocks[i], false, true));
        }

        proxmarkJson.put("blocks", blockHashMap);

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
