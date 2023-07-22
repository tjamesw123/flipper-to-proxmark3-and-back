package flippertoproxmark3andback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import flippertoproxmark3andback.Constants.FileType;
import flippertoproxmark3andback.Constants.KeyType;
import flippertoproxmark3andback.Constants.NfcType;

public class NFCFactory {
    
    public static NFC createNFCFromFile(File file) throws FileNotFoundException, ParseException {
        String path = file.getAbsolutePath();
        FileType fileType = Constants.fileExtensionToFileType.get(path.substring(path.indexOf(".")+1));
        if (fileType == FileType.flipper) {
            Scanner scan = new Scanner(file);
            //Filetype: Flipper NFC Device
            //Version: 3
            // Nfc device type can be UID, Mifare Ultralight, Mifare CLassic or ISO15693 
            //createdBy = "FlipperZero";
            for (int i = 0; i < 3; i++) {
                //System.out.println(scan.nextLine());
                scan.nextLine();
            }
            // System.out.println();
            String deviceType = scan.nextLine().substring(13);//Device type: "inserthere"
            //System.out.println(deviceType);
            //System.out.println(scan.nextLine());// UID is common for all formats
            scan.nextLine();
            scan.nextLine();
            //String[] UID = scan.nextLine().substring(5).split(" ");//UID: "inserthere"
            if (deviceType.equals("Mifare Classic")) {
                return new MifareClassic(file);
            } else if (deviceType.equals("UID")) {
                if (scan.hasNextLine()) {
                    return new RFIDCard(file);
                } else {
                    return new NFC(file);
                }
            } else if (Constants.flipperDeviceToMfuType.containsKey(deviceType)) {
                return new MifareUltralight(file);
            } else {
                return new RFIDCard(file);
            }
            // for (String s : UID) {
            //     System.out.println(s);
            // }
            //this.UID = new int[]{Integer.decode("0x" + UID[0]), Integer.decode("0x" + UID[1]), Integer.decode("0x" + UID[2]), Integer.decode("0x" + UID[3])};
        } else {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(file))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                
                JSONObject nfcArray = (JSONObject) obj;
                //System.out.println(nfcArray);
                //this.createdBy = (String) nfcArray.get("Created");
                //System.out.println(this.createdBy);
                HashMap<String, String> hashMap = (HashMap) nfcArray.get("Card");
                //System.out.println(hashMap.toString());
                //this.UID = hexStrToIntArr(hashMap.get("UID"));
                String nfcStr = Constants.proxmarkFiletypeToFlipperDevice.get(nfcArray.get("FileType"));
                if (nfcStr.equals("Mifare Classic")) {
                    return new MifareClassic(file);
                } else if (nfcStr.equals("UID")) {//Any other type of nfc tag
                    return new RFIDCard(file);
                } else if (nfcStr.equals("Mifare Ultralight")) {
                    return new MifareUltralight(file);
                } else {
                    return new NFC(file);
                }

                

                

                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            

        }
        return new NFC(file);
    }
        
}

