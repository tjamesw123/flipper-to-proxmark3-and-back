package flippertoproxmark3andback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

import netscape.javascript.JSObject;
import flippertoproxmark3andback.Constants.FileType;
import flippertoproxmark3andback.Constants.KeyType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import flippertoproxmark3andback.Constants.NfcType;
public class Nfc {
    
    private String createdBy;
    private NfcType nfcType;
    private int[] UID;

    //ISO14443(ID cards rfid) Specific fields//TODO: potentially split nfc into different classes and make a detector method that returns the type of nfc object required
    private int[] ATQA;//Saved in Proxmark format (Note: remember to switch to flipper format for flipper stuff) 
    private int SAK;

    private int[][] blocks;//Saved in Proxmark format (Note: remember to switch to flipper format for flipper stuff)
    private SectorKey[] sectorKeys;
    //30 60 20 6F 5B 0A 78 77 88 C1 F1 B9 F5 66 9C C8
    //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
    private int[] flipperToProxmarkFormatBlockLine(int[] line) {
        int[] newLine = new int[]{line[10], line[11], line[12], line[13], line[14], line[15], line[6], line[7], line[8], line[9], line[0], line[1], line[2], line[3], line[4], line[5]};
        return newLine;
    }
    private int[] proxmarkToFlipperFormatBlockLine(int[] line) {
        int[] newLine = new int[]{line[10], line[11], line[12], line[13], line[14], line[15], line[6], line[7], line[8], line[9], line[0], line[1], line[2], line[3], line[4], line[5]};
        return newLine;
    }
    public Nfc() {

    }

    public Nfc(FileType fileType, File file) throws FileNotFoundException, org.json.simple.parser.ParseException {
        Scanner scan = new Scanner(file);
        if (FileType.flipper == fileType) {
            //Filetype: Flipper NFC Device
            //Version: 3
            // Nfc device type can be UID, Mifare Ultralight, Mifare CLassic or ISO15693 
            createdBy = "FlipperZero";
            for (int i = 0; i < 3; i++) {
                //System.out.println(scan.nextLine());
                scan.nextLine();
            }
            // System.out.println();
            String deviceType = scan.nextLine().substring(13);//Device type: "inserthere"
            //System.out.println(deviceType);
            //System.out.println(scan.nextLine());// UID is common for all formats
            scan.nextLine();
            String[] UID = scan.nextLine().substring(5).split(" ");//UID: "inserthere"
            // for (String s : UID) {
            //     System.out.println(s);
            // }
            this.UID = new int[]{Integer.decode("0x" + UID[0]), Integer.decode("0x" + UID[1]), Integer.decode("0x" + UID[2]), Integer.decode("0x" + UID[3])};
            
            if (deviceType.equals("Mifare Classic") || deviceType.equals("Mifare Ultralight")) {
                //System.out.println(scan.nextLine());// ISO14443 specific fields
                scan.nextLine();

                String[] ATQA = scan.nextLine().substring(6).split(" ");//ATQA: inserthere
                // for (String s : ATQA) {
                //     System.out.println(s);
                // }
                this.ATQA = new int[]{Integer.decode("0x" + ATQA[1]), Integer.decode("0x" + ATQA[0])};//again formatted proxmark3 style
                this.SAK = Integer.decode("0x"+scan.nextLine().substring(5));//SAK: inserthere
                // System.out.println(String.format("%2s", Integer.toHexString(SAK)).replaceAll(" ", "0"));
                if (deviceType.equals("Mifare Classic")) {
                    //System.out.println(scan.nextLine());// Mifare Classic specific data
                    scan.nextLine();
                    String mifareClassicType = scan.nextLine().substring(21);
                    //System.out.println(mifareClassicType);
                    //System.out.println(scan.nextLine());//Data format version: 2 TODO: keep up with the data format versions
                    scan.nextLine();
                    if (mifareClassicType.equals("1K")) {
                        //System.out.println(scan.nextLine());// Mifare Classic blocks, '??' means unknown data //gotta have all the keys I guess as a note
                        scan.nextLine();
                        blocks = new int[64][16];
                        sectorKeys = new SectorKey[16];
                        int sectorKeySector = 0;
                        for (int i = 0; i < blocks.length; i++) {
                            String temp = scan.nextLine();
                            String[] blocksStr = temp.substring(temp.indexOf(":")+2).split(" ");
                            //System.out.println();
                            // for (String str : blocksStr) {
                            //     System.out.print(str + " ");
                            // }
                            
                            int[] line = new int[]{     Integer.decode("0x" + blocksStr[0]), 
                                                        Integer.decode("0x" + blocksStr[1]), 
                                                        Integer.decode("0x" + blocksStr[2]), 
                                                        Integer.decode("0x" + blocksStr[3]), 
                                                        Integer.decode("0x" + blocksStr[4]), 
                                                        Integer.decode("0x" + blocksStr[5]), 
                                                        Integer.decode("0x" + blocksStr[6]), 
                                                        Integer.decode("0x" + blocksStr[7]), 
                                                        Integer.decode("0x" + blocksStr[8]), 
                                                        Integer.decode("0x" + blocksStr[9]), 
                                                        Integer.decode("0x" + blocksStr[10]), 
                                                        Integer.decode("0x" + blocksStr[11]), 
                                                        Integer.decode("0x" + blocksStr[12]),
                                                        Integer.decode("0x" + blocksStr[13]),
                                                        Integer.decode("0x" + blocksStr[14]),
                                                        Integer.decode("0x" + blocksStr[15])};
                                if (i % 4 == 3) {
                                    this.blocks[i] = flipperToProxmarkFormatBlockLine(line);
                                    sectorKeys[sectorKeySector] = new SectorKey(this.blocks[i], sectorKeySector);
                                    sectorKeySector++;
                                } else {
                                    this.blocks[i] = line;
                                }
                        }
                        this.nfcType = NfcType.mifareclassic1k;
                    } else if (mifareClassicType.equals("4K")) {

                    }
                } else if (deviceType.equals("Mifare Ultralight")) {

                }
            } else if (deviceType.equals("UID")){

            } else if (deviceType.equals("ISO15693")){

            }

        } else {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(file))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                
                JSONObject nfcArray = (JSONObject) obj;
                //System.out.println(nfcArray);
                this.createdBy = (String) nfcArray.get("Created");
                //System.out.println(this.createdBy);
                HashMap<String, String> hashMap = (HashMap) nfcArray.get("Card");
                //System.out.println(hashMap.toString());
                this.UID = hexStrToIntArr(hashMap.get("UID"));
                String nfcStr = Constants.proxmarkFiletypeToFlipperDevice.get(nfcArray.get("FileType"));
                if (nfcStr.equals("Mifare Classic")) {
                    this.nfcType = Constants.nfcStrToNfcType.get(nfcStr + " " + Constants.sakToMifareClassicType.get(hashMap.get("SAK")));
                    this.SAK = hexStrToInt(hashMap.get("SAK"));
                    this.ATQA = hexStrToIntArr(hashMap.get("ATQA"));
                    HashMap<String, String> hashMapBlocks = (HashMap) nfcArray.get("blocks");
                    if (NfcType.mifareclassic1k == nfcType) {
                        blocks = new int[64][16];
                        for (int i = 0; i < 64; i++) {
                            this.blocks[i] = hexStrToIntArr((String) hashMapBlocks.get("" + i));
                        }
                        JSONObject sectorKeysObject = (JSONObject) nfcArray.get("SectorKeys");
                        sectorKeys = new SectorKey[16];
                        for (int i = 0; i < 16; i++) {
                            JSONObject sectorKeyFinal = (JSONObject) sectorKeysObject.get(""+i);
                            sectorKeys[i] = new SectorKey(  new Key(hexStrToIntArr((String) sectorKeyFinal.get("KeyA")), KeyType.A), //Kind of avoids reading though the whole json by just reusing the id system used for importing flipper nfcs
                                                            new Key(hexStrToIntArr((String)sectorKeyFinal.get("KeyB")), KeyType.B), 
                                                            hexStrToIntArr((String)sectorKeyFinal.get("AccessConditions")), i);

                        }
                    } else if (NfcType.mifareclassic4k == nfcType) {//Also a work in progress

                    }
                } else {//Any other type of nfc tag
                    this.nfcType = Constants.nfcStrToNfcType.get(nfcStr);//Doesn't work yet - Only Mifare Classic 1K as of time of writing
                }

                

                

                
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
            flipperNfcFile = new File(createdBy + "-" + arrToHexString(UID, false, true) + ".nfc");
        } else {
            flipperNfcFile = new File(customName + ".nfc");
        }
        PrintStream fileStream = new PrintStream(flipperNfcFile);
        fileStream.println("Filetype: Flipper NFC Device");//Placeholders maybe for future verisons
        fileStream.println("Version: 3");
        fileStream.println("# Nfc device type can be UID, Mifare Ultralight, Mifare Classic or ISO15693");
        fileStream.println("Device type: " + Constants.nfcTypeToFlipperDevice.get(nfcType));
        fileStream.println("# UID is common for all formats");
        fileStream.println("UID: " + arrToHexString(UID, true, true));
        if (nfcType == NfcType.mifareclassic1k || nfcType == NfcType.mifareclassic4k) {
            fileStream.println("# ISO14443 specific fields");
            fileStream.println("ATQA: " + intToHexString(ATQA[1], true) + " " + intToHexString(ATQA[0], true));
            fileStream.println("SAK: " + intToHexString(SAK, true));
            fileStream.println("# Mifare Classic specific data");
            if (nfcType == NfcType.mifareclassic1k) {
                fileStream.println("Mifare Classic type: 1K");
            } else if (nfcType == NfcType.mifareclassic4k) {
                fileStream.println("Mifare Classic type: 4K");
            }
            fileStream.println("Data format version: 2");
            fileStream.println("# Mifare Classic blocks, \'??\' means unknown data");
            for (int i = 0; i < blocks.length; i++) {
                if (i % 4 == 3) {
                    fileStream.println("Block " + i + ": " + arrToHexString(proxmarkToFlipperFormatBlockLine(blocks[i]), true, true));
                } else {
                    fileStream.println("Block " + i + ": " + arrToHexString(blocks[i], true, true));
                }
            }
        }
        fileStream.close();


        
        
    }
    public void exportAsProxmark3Dump(String customName) throws IOException {
        JSONObject proxmarkJson = new JSONObject();

        proxmarkJson.put("Created", createdBy);
        proxmarkJson.put("FileType", Constants.nfcTypeToProxmarkFiletype.get(nfcType));

        if (nfcType == NfcType.mifareclassic1k || nfcType == NfcType.mifareclassic4k) {
            HashMap<String, String> card = new HashMap<String, String>();
            /*card.put("UID", "" + String.format("%2s", Integer.toHexString(UID[0])).replaceAll(" ", "0")
             + String.format("%2s", Integer.toHexString(UID[1])).replaceAll(" ", "0")
             + String.format("%2s", Integer.toHexString(UID[2])).replaceAll(" ", "0") 
             + String.format("%2s", Integer.toHexString(UID[3])).replaceAll(" ", "0"));*/
            card.put("UID", arrToHexString(UID, false, true));
            //card.put("ATQA", String.format("%2s", Integer.toHexString(ATQA[0])).replaceAll(" ", "0") + String.format("%2s", Integer.toHexString(ATQA[1])).replaceAll(" ", "0"));
            card.put("ATQA", arrToHexString(ATQA, false, true));
            //card.put("SAK", String.format("%2s", Integer.toHexString(SAK)).replaceAll(" ", "0"));
            card.put("SAK", intToHexString(SAK, true));

            proxmarkJson.put("Card", card);

            HashMap<String, String> blockHashMap = new HashMap<String, String>();
            for (int i = 0; i < blocks.length; i++) {
                // blockHashMap.put(""+i, "" 
                // + String.format("%2s", Integer.toHexString(blocks[i][0])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][1])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][2])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][3])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][4])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][5])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][6])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][7])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][8])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][9])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][10])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][11])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][12])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][13])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][14])).replaceAll(" ", "0").toUpperCase()
                // + String.format("%2s", Integer.toHexString(blocks[i][15])).replaceAll(" ", "0").toUpperCase());
                blockHashMap.put(""+i, "" 
                + String.format("%2s", arrToHexString(blocks[i], false, true)));
            }

            proxmarkJson.put("blocks", blockHashMap);
            HashMap<String, JSONObject> sectorKeysHashMap = new HashMap<String, JSONObject>();//TODO: Basically in the right order
            for (int i = 0; i < sectorKeys.length; i++) {
                JSONObject jsonSectorKey = new JSONObject();
                jsonSectorKey.put("KeyA", sectorKeys[i].a.toString());
                jsonSectorKey.put("KeyB", sectorKeys[i].b.toString());
                jsonSectorKey.put("AccessConditions", sectorKeys[i].accessConditionsHexString());

                HashMap<String, String> temp = new HashMap<String, String>();//Maybe change later? It's sorta none sense in some ways
                for (int g = 0; g < sectorKeys[i].accessConditionText.length; g++) {
                    temp.put(sectorKeys[i].accessConditionText[g][0], sectorKeys[i].accessConditionText[g][1]);
                }
                jsonSectorKey.put("AccessConditionText", temp);
                sectorKeysHashMap.put(""+i, jsonSectorKey);
            }
            proxmarkJson.put("SectorKeys", sectorKeysHashMap);
            File proxmarkJsonFile;
            if (customName.equals("")) {
                proxmarkJsonFile = new File(createdBy + "-" + card.get("UID").toUpperCase() + "-" + "dump.json");
            } else {
                proxmarkJsonFile = new File(customName + ".json");
            }
            
            FileWriter fileWriter = new FileWriter(proxmarkJsonFile);
            //System.out.println(proxmarkJson.toJSONString());
            fileWriter.write(proxmarkJson.toJSONString());
            fileWriter.close();


        }

        
        


    }
        private String arrToHexString(int[] arr, boolean spacing, boolean toUpper) {// turns a list of ints into 2 character strings with the int in hexadecimal format
            String result = "";
            for (int i = 0; i < arr.length; i++) {
                if (spacing) {
                    if (i == arr.length-1) {
                        result += String.format("%2s", Integer.toHexString(arr[i])).replaceAll(" ", "0");
                    } else {
                        result += String.format("%2s", Integer.toHexString(arr[i])).replaceAll(" ", "0") + " ";
                    }
                } else {
                    result += String.format("%2s", Integer.toHexString(arr[i])).replaceAll(" ", "0");
                }
            }
            if (toUpper) {
                result = result.toUpperCase();
            }
            return result;
        }
        private String intToHexString(int num, boolean toUpper) {// turns a single int into a 2 character string with the int in hexadecimal format
            String temp = String.format("%2s", Integer.toHexString(num)).replaceAll(" ", "0");
            if (toUpper) {
                temp = temp.toUpperCase();
            }
            return temp;
        }
        private int hexStrToInt(String hexString) {
            return Integer.decode("0x" + hexString);
        }
        private int[] hexStrToIntArr(String hex) {
            int[] result = new int[hex.length()/2];
            for (int i = 0; i < hex.length(); i = i+2) {
                result[i/2] = Integer.decode("0x" + hex.substring(i, i+2));
            }
            return result;
        }
}
