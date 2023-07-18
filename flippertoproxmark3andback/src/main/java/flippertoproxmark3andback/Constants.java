package flippertoproxmark3andback;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public enum NfcType {
        mifareclassic1k, mifareclassic4k, mifareultralight, UID, ISO15693
    }
    public enum MifareClassicType {
        MFC1k, MFC4k
    }
    public enum FileType {
        flipper,
        proxmark3
    }
    public enum KeyType {
        A,
        B
    }
    public static HashMap<String, FileType> fileExtensionToFileType = new HashMap<String, FileType>(){{
        put("nfc", FileType.flipper);
        put("json", FileType.proxmark3);
    }};
    public static HashMap<String, String> sakToMifareClassicType= new HashMap<String, String>() {{
    put("18", "4K");
    put("08", "1K");
}};
    public HashMap<String, String> flipperDeviceToProxmarkFiletype = new HashMap<String, String>() {{
    put("ISO15693", null);//Don't know what the null types are yet
    put("UID", null);
    put("Mifare Classic", "mfcard");
    put("Mifare Ultralight", "nfu");
}};
    public static HashMap<String, String> proxmarkFiletypeToFlipperDevice = new HashMap<String, String>() {{
    //put("null", ISO15693);//Don't know what the null types are yet
    //put("null", UID);
    put("mfcard", "Mifare Classic");
    put("mfu", "Mifare Ultralight");
}};
    public static HashMap<String, NfcType> nfcStrToNfcType = new HashMap<String, NfcType>() {{
    //put("null", ISO15693);//Don't know what the null types are yet
    //put("null", UID);
    put("Mifare Classic 1K", NfcType.mifareclassic1k);
    put("Mifare Classic 4K", NfcType.mifareclassic4k);
    //put("null", Mifare Ultralight);
}};
public static HashMap<String, MifareClassicType> mfcStrToMfcType = new HashMap<String, MifareClassicType>() {{
    put("Mifare Classic 1K", MifareClassicType.MFC1k);
    put("Mifare Classic 4K", MifareClassicType.MFC4k);
}};
public static HashMap<MifareClassicType, String> mfcTypeToProxmark3FileType = new HashMap<MifareClassicType, String>() {{
    put(MifareClassicType.MFC1k, "mfcard");
    put(MifareClassicType.MFC4k, "mfcard");
}};

public static HashMap<MifareClassicType, String> mfcTypeToFlipperDevice = new HashMap<MifareClassicType, String>() {{
    put(MifareClassicType.MFC1k, "Mifare Classic 1K");
    put(MifareClassicType.MFC4k, "Mifare Classic 4K");
}};

    
    public static HashMap<NfcType, String> nfcTypeToProxmarkFiletype = new HashMap<NfcType, String>() {{
    put(NfcType.ISO15693, null);//Don't know what the null types are yet
    put(NfcType.UID, null);
    put(NfcType.mifareclassic1k, "mfcard");
    put(NfcType.mifareclassic4k, "mfcard");
    put(NfcType.mifareultralight, "");
}};
    public static HashMap<NfcType, String> nfcTypeToFlipperDevice = new HashMap<NfcType, String>() {{
    put(NfcType.ISO15693, "ISO15693");//Don't know what the null types are yet
    put(NfcType.UID, "UID");
    put(NfcType.mifareclassic1k, "Mifare Classic");
    put(NfcType.mifareclassic4k, "Mifare Classic");
    put(NfcType.mifareultralight, "Mifare Ultralight");
}};
    public HashMap<NfcType, String> nfcTypeToMifareCap = new HashMap<NfcType, String>() {{
    put(NfcType.mifareclassic1k, "1K");
    put(NfcType.mifareclassic4k, "4K");
}};
    
    public static HashMap<String, String> accessBitsToConditions = new HashMap<String, String>() {{
    put("000", "read A; write A");
    put("010", "read A");
    put("100", "read AB; write B");
    put("110", "read AB");
    put("001", "read A; write A");
    put("011", "read AB; write B");
    put("101", "read AB; write B");
    put("111", "read AB");
}};

public static HashMap<String, String> trailBitsToCondition = new HashMap<String, String>() {{
    put("000", "write A by A; read ACCESS by A; read B by A; write B by A");
    put("010", "read ACCESS by A; read B by A");
    put("100", "write A by B; read ACCESS by AB; write B by B");
    put("110", "read ACCESS by AB");
    put("001", "write A by A; read ACCESS by A; write ACCESS by A; read B by A; write B by A");
    put("011", "write A by B; read ACCESS by AB; write ACCESS by B; write B by B");
    put("101", "read ACCESS by AB; write ACCESS by B");
    put("111", "read ACCESS by AB");
}};

public static String arrToHexString(int[] arr, boolean spacing, boolean toUpper) {// turns a list of ints into 2 character strings with the int in hexadecimal format
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
public static String intToHexString(int num, boolean toUpper) {// turns a single int into a 2 character string with the int in hexadecimal format
    String temp = String.format("%2s", Integer.toHexString(num)).replaceAll(" ", "0");
    if (toUpper) {
        temp = temp.toUpperCase();
    }
    return temp;
}
public static int hexStrToInt(String hexString) {
    return Integer.decode("0x" + hexString);
}
public static int[] hexStrToIntArr(String hex) {
    int[] result = new int[hex.length()/2];
    for (int i = 0; i < hex.length(); i = i+2) {
        result[i/2] = Integer.decode("0x" + hex.substring(i, i+2));
    }
    return result;
}
public static int[] hexStrArrtoIntArr(String[] hexStrs) {
    String tempString = "";
    for (String s : hexStrs) {
        tempString += s;
    }
    return hexStrToIntArr(tempString);
}
public static int[] flipperToProxmarkFormatBlockLine(int[] line) {
    int[] newLine = new int[]{line[10], line[11], line[12], line[13], line[14], line[15], line[6], line[7], line[8], line[9], line[0], line[1], line[2], line[3], line[4], line[5]};
    return newLine;
}
public static int[] proxmarkToFlipperFormatBlockLine(int[] line) {
    int[] newLine = new int[]{line[10], line[11], line[12], line[13], line[14], line[15], line[6], line[7], line[8], line[9], line[0], line[1], line[2], line[3], line[4], line[5]};
    return newLine;
}

}
