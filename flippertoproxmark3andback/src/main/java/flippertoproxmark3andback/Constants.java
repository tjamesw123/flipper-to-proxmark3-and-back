package flippertoproxmark3andback;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public enum NfcType {
        mifareclassic1k, mifareclassic4k, mifareultralight, UID, ISO15693
    }
    public enum MifareClassicType {
        MFC1k, MFC4k, MFCMini
    }
    public static HashMap<String, MifareClassicType> mfcStrToMfcType = new HashMap<String, MifareClassicType>() {{
    put("Mifare Classic 1K", MifareClassicType.MFC1k);
    put("Mifare Classic 4K", MifareClassicType.MFC4k);
    put("MINI", MifareClassicType.MFCMini);
    }};
    public static HashMap<MifareClassicType, String> mfcTypeToFlipperDevice = new HashMap<MifareClassicType, String>() {{
    put(MifareClassicType.MFC1k, "Mifare Classic");
    put(MifareClassicType.MFC4k, "Mifare Classic");
    put(MifareClassicType.MFCMini, "Mifare Classic");
    }};
    public static HashMap<MifareClassicType, String> mfcTypeToMifareCap = new HashMap<MifareClassicType, String>() {{
        put(MifareClassicType.MFC1k, "1K");
        put(MifareClassicType.MFC4k, "4K");
        put(MifareClassicType.MFCMini, "MINI");
    }};
    public static HashMap<String, MifareClassicType> sakToMfcType = new HashMap<String, MifareClassicType>() {{
    put("18", MifareClassicType.MFC4k);
    put("08", MifareClassicType.MFC1k);
    put("09", MifareClassicType.MFCMini);
    }};
    public enum MifareUltralightType {
        MFU, MFU11, MFUH11, MFU21, MFUH21, NTAG203, NTAG213, NTAG215, NTAG216, NTAGI2C1K, NTAGI2C2K, NTAGI2CPLUS1K, NTAGI2CPLUS2K
    }

    public static HashMap<String, MifareUltralightType> flipperDeviceToMfuType = new HashMap<String, MifareUltralightType>() {{
    put("Mifare Ultralight", MifareUltralightType.MFU);
    put("Mifare Ultralight 11", MifareUltralightType.MFU11);
    put("Mifare Ultralight 21", MifareUltralightType.MFU21);
    put("Mifare Ultralight H11", MifareUltralightType.MFUH11);//Both this
    put("Mifare Ultralight H21", MifareUltralightType.MFUH21);//and this are sorta unused but I'm leaving them here for now
    put("NTAG203", MifareUltralightType.NTAG203);
    put("NTAG213", MifareUltralightType.NTAG213);
    put("NTAG215", MifareUltralightType.NTAG215);
    put("NTAG216", MifareUltralightType.NTAG216);
    put("NTAG I2C 1k", MifareUltralightType.NTAGI2C1K);
    put("NTAG I2C 2k", MifareUltralightType.NTAGI2C2K);
    put("NTAG I2C Plus 1k", MifareUltralightType.NTAGI2CPLUS1K);
    put("NTAG I2C Plus 2k", MifareUltralightType.NTAGI2CPLUS2K);
}};
    public static HashMap<MifareUltralightType, String> mfuTypeToFlipperDevice = new HashMap<MifareUltralightType, String>() {{
    put(MifareUltralightType.MFU, "Mifare Ultralight");
    put(MifareUltralightType.MFU11, "Mifare Ultralight 11");
    put(MifareUltralightType.MFU21, "Mifare Ultralight 21");
    put(MifareUltralightType.MFUH11, "Mifare Ultralight 11");
    put(MifareUltralightType.MFUH21, "Mifare Ultralight 21");
    put(MifareUltralightType.NTAG203, "NTAG203");
    put(MifareUltralightType.NTAG213, "NTAG213");
    put(MifareUltralightType.NTAG215, "NTAG215");
    put(MifareUltralightType.NTAG216, "NTAG216");
    put(MifareUltralightType.NTAGI2C1K, "NTAG I2C 1K");
    put(MifareUltralightType.NTAGI2C2K, "NTAG I2C 2K");
    put(MifareUltralightType.NTAGI2CPLUS1K, "NTAG I2C Plus 1K");
    put(MifareUltralightType.NTAGI2CPLUS2K, "NTAG I2C Plus 2K");
}};
public static HashMap<String, MifareUltralightType> mfuVersionToMfuType = new HashMap<String, MifareUltralightType>() {{
    put("0000000000000000", MifareUltralightType.MFU);
    put("0004030101000B03", MifareUltralightType.MFU11);
    put("0004030101000E03", MifareUltralightType.MFU21);
    put("0004030201000B03", MifareUltralightType.MFUH11);
    put("0004030201000E03", MifareUltralightType.MFUH21);
    put("0000000000000000", MifareUltralightType.NTAG203);
    put("0004040201000F03", MifareUltralightType.NTAG213);
    put("0004040201001103", MifareUltralightType.NTAG215);
    put("0004040201001303", MifareUltralightType.NTAG216);
    put("0004040502011303", MifareUltralightType.NTAGI2C1K);
    put("0004040502011503", MifareUltralightType.NTAGI2C2K);
    put("0004040502021303", MifareUltralightType.NTAGI2CPLUS1K);
    put("0004040502021503", MifareUltralightType.NTAGI2CPLUS2K);
}};
public static HashMap<String, MifareUltralightType> mfuVersionPlusPageTotalToMfuType = new HashMap<String, MifareUltralightType>() {{
    put("0000000000000000 16", MifareUltralightType.MFU);
    put("0004030101000B03 20", MifareUltralightType.MFU11);
    put("0004030101000E03 41", MifareUltralightType.MFU21);
    put("0004030201000B03 20", MifareUltralightType.MFUH11);
    put("0004030201000E03 41", MifareUltralightType.MFUH21);
    put("0000000000000000 42", MifareUltralightType.NTAG203);
    put("0004040201000F03 45", MifareUltralightType.NTAG213);
    put("0004040201001103 135", MifareUltralightType.NTAG215);
    put("0004040201001303 231", MifareUltralightType.NTAG216);
    put("0004040502011303 231", MifareUltralightType.NTAGI2C1K);
    put("0004040502011503 485", MifareUltralightType.NTAGI2C2K);
    put("0004040502021303 236", MifareUltralightType.NTAGI2CPLUS1K);
    put("0004040502021503 492", MifareUltralightType.NTAGI2CPLUS2K);
}};
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
    
    public HashMap<String, String> flipperDeviceToProxmarkFiletype = new HashMap<String, String>() {{
    put("ISO15693", null);//Don't know what the null types are yet
    put("UID", null);
    put("Mifare Classic", "mfcard");
    put("Mifare Ultralight", "mfu");
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


//public static HashMap<>



    
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
    return intToHexString(num, toUpper, 2);
}
public static String intToHexString(int num, boolean toUpper, int leadingZeros) {// turns a single int into a (leadingZeros) character string with the int in hexadecimal format
    String temp = String.format("%" + leadingZeros + "s", Integer.toHexString(num)).replaceAll(" ", "0");
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
