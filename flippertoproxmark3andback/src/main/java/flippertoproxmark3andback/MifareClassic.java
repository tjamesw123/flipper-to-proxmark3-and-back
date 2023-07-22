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
import flippertoproxmark3andback.Constants.NfcType;

public class MifareClassic extends RFIDCard {//Can be 1k or 4k
    private int[][] blocks;//Saved in Proxmark format (Note: remember to switch to flipper format for flipper stuff)
    private SectorKey[] sectorKeys;
    private MifareClassicType mifareClassicType;
    //30 60 20 6F 5B 0A 78 77 88 C1 F1 B9 F5 66 9C C8
    //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15

    public MifareClassic(File file) throws FileNotFoundException, org.json.simple.parser.ParseException {
        super(file);
        System.out.println("MifareClassic!");
        if (FileType.flipper == this.getImportFileType()) {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < 9; i++) {
                scan.nextLine();
            }
            //System.out.println(scan.nextLine());// Mifare Classic specific data
            scan.nextLine();
            String mifareClassicTypeStr = scan.nextLine().substring(21);
            //System.out.println(mifareClassicTypeStr);
            //System.out.println(mifareClassicType);
            //System.out.println(scan.nextLine());//Data format version: 2 TODO: keep up with the data format versions
            scan.nextLine();
            //System.out.println(scan.nextLine());// Mifare Classic blocks, '??' means unknown data //gotta have all the keys I guess as a note
            scan.nextLine();
            if (mifareClassicTypeStr.equals("1K")) {
                int sectorKeySector = 0;
                this.blocks = new int[64][16];
                //System.out.println(blocks.length);
                this.sectorKeys = new SectorKey[16];
                this.mifareClassicType = MifareClassicType.MFC1k;
                for (int i = 0; i < blocks.length; i++) {
                    String temp = scan.nextLine();
                    String[] blocksStr = temp.substring(temp.indexOf(":")+2).split(" ");
                    //System.out.println();
                    // for (String str : blocksStr) {
                    //     System.out.print(str + " ");
                    // }
                    
                    int[] line = Constants.hexStrArrtoIntArr(blocksStr);//Trusting that this function works
                    if (i % 4 == 3) {
                        this.blocks[i] = Constants.flipperToProxmarkFormatBlockLine(line);
                        sectorKeys[sectorKeySector] = new SectorKey(this.blocks[i], sectorKeySector);
                        sectorKeySector++;
                    } else {
                        this.blocks[i] = line;
                    }
                } 
            } else if (mifareClassicTypeStr.equals("4K")) {
                int sectorKeySector = 0;
                this.blocks = new int[256][16];
                //System.out.println(blocks.length);
                this.sectorKeys = new SectorKey[40];
                this.mifareClassicType = MifareClassicType.MFC4k;
                for (int i = 0; i < blocks.length; i++) {
                    String temp = scan.nextLine();
                    String[] blocksStr = temp.substring(temp.indexOf(":")+2).split(" ");
                    //System.out.println();
                    // for (String str : blocksStr) {
                    //     System.out.print(str + " ");
                    // }
                    
                    int[] line = Constants.hexStrArrtoIntArr(blocksStr);//Trusting that this function works
                    if (sectorKeySector > 31) {
                        if (i % 16 == 15) {
                            this.blocks[i] = Constants.flipperToProxmarkFormatBlockLine(line);
                            sectorKeys[sectorKeySector] = new SectorKey(this.blocks[i], sectorKeySector);
                            sectorKeySector++;
                        } else {
                            this.blocks[i] = line;
                        }
                    } else {
                        if (i % 4 == 3) {
                            this.blocks[i] = Constants.flipperToProxmarkFormatBlockLine(line);
                            sectorKeys[sectorKeySector] = new SectorKey(this.blocks[i], sectorKeySector);
                            sectorKeySector++;
                        } else {
                            this.blocks[i] = line;
                        }
                    }
                }
            } else {
                int sectorKeySector = 0;
                this.blocks = new int[20][16];
                //System.out.println(blocks.length);
                this.sectorKeys = new SectorKey[5];
                this.mifareClassicType = MifareClassicType.MFCMini;
                for (int i = 0; i < blocks.length; i++) {
                    String temp = scan.nextLine();
                    String[] blocksStr = temp.substring(temp.indexOf(":")+2).split(" ");
                    //System.out.println();
                    // for (String str : blocksStr) {
                    //     System.out.print(str + " ");
                    // }
                    
                    int[] line = Constants.hexStrArrtoIntArr(blocksStr);//Trusting that this function works
                    if (i % 4 == 3) {
                        this.blocks[i] = Constants.flipperToProxmarkFormatBlockLine(line);
                        sectorKeys[sectorKeySector] = new SectorKey(this.blocks[i], sectorKeySector);
                        sectorKeySector++;
                    } else {
                        this.blocks[i] = line;
                    }
                } 
            }
        } else {
            JSONParser jsonParser = new JSONParser();
                try (FileReader reader = new FileReader(file))
                {
                    //Read JSON file
                    Object obj = jsonParser.parse(reader);
                    //System.out.println("Work");
                    JSONObject nfcArray = (JSONObject) obj;
                    HashMap<String, String> hashMap = (HashMap) nfcArray.get("Card");
                    HashMap<String, String> hashMapBlocks = (HashMap) nfcArray.get("blocks");
                    //System.out.println(Constants.sakToMifareClassicType.get(hashMap.get("SAK")));
                    this.mifareClassicType = Constants.sakToMfcType.get((String)nfcArray.get("SAK"));
                    if (MifareClassicType.MFC1k == this.mifareClassicType) {//Here
                        System.out.println("1K!");
                        blocks = new int[64][16];
                        sectorKeys = new SectorKey[16];
                    } else if (MifareClassicType.MFC4k == this.mifareClassicType) {//Also a work in progress
                        System.out.println("4K!");
                        blocks = new int[256][16];
                        sectorKeys = new SectorKey[40];
                    } else {
                        System.out.println("Mini!");
                        blocks = new int[20][16];
                        sectorKeys = new SectorKey[5];
                    }
                    for (int i = 0; i < blocks.length; i++) {
                        this.blocks[i] = Constants.hexStrToIntArr((String) hashMapBlocks.get("" + i));
                    }
                    JSONObject sectorKeysObject = (JSONObject) nfcArray.get("SectorKeys");
                    
                    for (int i = 0; i < sectorKeys.length; i++) {
                        JSONObject sectorKeyFinal = (JSONObject) sectorKeysObject.get(""+i);
                        sectorKeys[i] = new SectorKey(  new Key(Constants.hexStrToIntArr((String) sectorKeyFinal.get("KeyA")), KeyType.A), //Kind of avoids reading though the whole json by just reusing the id system used for importing flipper nfcs
                                                        new Key(Constants.hexStrToIntArr((String)sectorKeyFinal.get("KeyB")), KeyType.B), 
                                                        Constants.hexStrToIntArr((String)sectorKeyFinal.get("AccessConditions")), i);

                    }
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
        fileStream.println("Device type: " + "Mifare Classic");
        fileStream.println("# UID is common for all formats");
        fileStream.println("UID: " + Constants.arrToHexString(this.getUID(), true, true));
        fileStream.println("# ISO14443 specific fields");
        fileStream.println("ATQA: " + Constants.intToHexString(this.getATQA()[1], true, 2) + " " + Constants.intToHexString(this.getATQA()[0], true, 2));
        fileStream.println("SAK: " + Constants.intToHexString(this.getSAK(), true, 2));
        fileStream.println("# Mifare Classic specific data");
        fileStream.println("Mifare Classic type: " + Constants.mfcTypeToMifareCap.get(mifareClassicType));
        fileStream.println("Data format version: 2");
        fileStream.println("# Mifare Classic blocks, \'??\' means unknown data");
        int sectors = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            if (i > 143 || sectors > 31) {
                if (i % 16 == 15) {//Mifare 4k Blocks past sector 31
                    fileStream.println("Block " + i + ": " + Constants.arrToHexString(Constants.proxmarkToFlipperFormatBlockLine(blocks[i]), true, true));
                    sectors++;
                } else {
                    fileStream.println("Block " + i + ": " + Constants.arrToHexString(blocks[i], true, true));
                }
            } else {
                if (i % 4 == 3) {//Mifare 1k, 4k, Mini blocks before sector 32
                    fileStream.println("Block " + i + ": " + Constants.arrToHexString(Constants.proxmarkToFlipperFormatBlockLine(blocks[i]), true, true));
                    sectors++;
                } else {
                    fileStream.println("Block " + i + ": " + Constants.arrToHexString(blocks[i], true, true));
                }
            }
            
        }
        fileStream.close();
    }
    public void exportAsProxmark3Dump(String customName) throws IOException {
        JSONObject proxmarkJson = new JSONObject();

        proxmarkJson.put("Created", this.getCreatedBy());
        proxmarkJson.put("FileType", "mfcard");
        HashMap<String, String> card = new HashMap<String, String>();
        card.put("UID", Constants.arrToHexString(this.getUID(), false, true));
        card.put("ATQA", Constants.arrToHexString(this.getATQA(), false, true));
        card.put("SAK", Constants.intToHexString(this.getSAK(), true, 2));
        proxmarkJson.put("Card", card);

        HashMap<String, String> blockHashMap = new HashMap<String, String>();
        for (int i = 0; i < blocks.length; i++) {
            blockHashMap.put(""+i, "" + Constants.arrToHexString(blocks[i], false, true));
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
            proxmarkJsonFile = new File(this.getCreatedBy() + "-" + card.get("UID").toUpperCase() + "-" + "dump.json");
        } else {
            proxmarkJsonFile = new File(customName + ".json");
        }
            
        FileWriter fileWriter = new FileWriter(proxmarkJsonFile);
        fileWriter.write(proxmarkJson.toJSONString());
        fileWriter.close();

        
        


    }
        
}
