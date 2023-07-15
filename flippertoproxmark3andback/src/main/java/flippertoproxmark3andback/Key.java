package flippertoproxmark3andback;

import flippertoproxmark3andback.Constants.KeyType;

public class Key {
    
    private int[] keyHex;
    private KeyType keyType;
    public Key(String keyStr, KeyType keyType) {
        String[] tempHexStrings = new String[]{keyStr.substring(0, 2), 
            keyStr.substring(2, 4), keyStr.substring(4, 6), 
            keyStr.substring(6, 8), keyStr.substring(8, 10), 
            keyStr.substring(10, 12)};//Splits the hex string every 2 chars ! no spacing

        this.keyHex = new int[tempHexStrings.length];
        for (int i = 0; i < tempHexStrings.length; i++) {
            keyHex[i] = Integer.decode("0x"+tempHexStrings[i]);
        }
        this.keyType = keyType;
    }
    public Key(int[] keyHex, KeyType keyType) {
        this.keyHex = keyHex;
        this.keyType = keyType;

    }
    @Override
    public String toString() {
        return String.format("%2s", Integer.toHexString(keyHex[0])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(keyHex[1])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(keyHex[2])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(keyHex[3])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(keyHex[4])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(keyHex[5])).replaceAll(" ", "0").toUpperCase();
        
    }
}
