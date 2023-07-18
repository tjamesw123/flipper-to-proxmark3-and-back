package flippertoproxmark3andback;
import java.util.HashMap;

import flippertoproxmark3andback.Constants.KeyType;

public class SectorKey {//Currently tuned for mifare 1k cards 
    public Key a;
    public Key b;
    public int[] accessConditions;
    public String[][] accessConditionText = new String[5][2];
    private int sector;
    public SectorKey(Key a, Key b, int[] accessConditions, int sector) {//Access conditions is an array of ints that should be read in hex
        this.a = a;
        this.b = b;
        this.accessConditions = accessConditions;
        this.sector = sector;
        this.accessConditionText = accessConditionsToAccessConditionText(accessConditions);
    }
    public SectorKey(int[] line, int sector) {//Access conditions is an array of ints that should be read in hex
        this.a = new Key(new int[]{line[0],line[1],line[2],line[3],line[4],line[5]}, KeyType.A);
        this.b = new Key(new int[]{line[10],line[11],line[12],line[13],line[14],line[15]}, KeyType.B);
        this.accessConditions = new int[]{line[6],line[7],line[8],line[9]};
        this.sector = sector;
        this.accessConditionText = accessConditionsToAccessConditionText(accessConditions);
    }
    public String accessConditionsHexString() {
        return String.format("%2s", Integer.toHexString(accessConditions[0])).replaceAll(" ", "0").toUpperCase()
        + String.format("%2s", Integer.toHexString(accessConditions[1])).replaceAll(" ", "0").toUpperCase() 
        + String.format("%2s", Integer.toHexString(accessConditions[2])).replaceAll(" ", "0").toUpperCase() 
        + String.format("%2s", Integer.toHexString(accessConditions[3])).replaceAll(" ", "0").toUpperCase();
    }

    private String[][] accessConditionsToAccessConditionText(int[] access) {
        String[] binaryStrings = new String[3];

        binaryStrings[0] = String.format("%8s", Integer.toBinaryString(access[0])).replaceAll(" ", "0");
        binaryStrings[1] = String.format("%8s", Integer.toBinaryString(access[1])).replaceAll(" ", "0");
        binaryStrings[2] = String.format("%8s", Integer.toBinaryString(access[2])).replaceAll(" ", "0");

        // for (String s : binaryStrings) {
        //     System.out.println(s);
        // }

        String[] invertedBinaryStrings = new String[4];
        String[] finalBinaryStrings = new String[4];

        
        invertedBinaryStrings[0] = "" + binaryStrings[0].charAt(7) + binaryStrings[0].charAt(3) + binaryStrings[1].charAt(7);//inverted
        invertedBinaryStrings[1] = "" + binaryStrings[0].charAt(6) + binaryStrings[0].charAt(2) + binaryStrings[1].charAt(6);
        invertedBinaryStrings[2] = "" + binaryStrings[0].charAt(5) + binaryStrings[0].charAt(1) + binaryStrings[1].charAt(5);

        invertedBinaryStrings[3] = "" + binaryStrings[0].charAt(4) + binaryStrings[0].charAt(0) + binaryStrings[1].charAt(4);//trailer


        finalBinaryStrings[0] = "" + binaryStrings[1].charAt(3) + binaryStrings[2].charAt(7) + binaryStrings[2].charAt(3);//actual sequence
        finalBinaryStrings[1] = "" + binaryStrings[1].charAt(2) + binaryStrings[2].charAt(6) + binaryStrings[2].charAt(2);
        finalBinaryStrings[2] = "" + binaryStrings[1].charAt(1) + binaryStrings[2].charAt(5) + binaryStrings[2].charAt(1);

        finalBinaryStrings[3] = "" + binaryStrings[1].charAt(0) + binaryStrings[2].charAt(4) + binaryStrings[2].charAt(0);//trailer

        //int trailer = Integer.reverse(Integer.parseInt(invertedBinaryStrings[3], 2));

        // for (String s : invertedBinaryStrings) {
        //     System.out.println(s);
        // }

        // for (String s : finalBinaryStrings) {
        //     System.out.println(s);
        // }


        //Integer.parseInt("0x", sector);



        String[][] accessConditionTextTemp = new String[5][2];
        for (int i = 0; i < 4; i++) {
            if (sector > 31) {
                if (i != 3) {
                    accessConditionTextTemp[i][0] = "block" + ((31*4)+((sector-31)*16)+i);
                    accessConditionTextTemp[i][1] = Constants.accessBitsToConditions.get(finalBinaryStrings[i]);
                } else {
                    accessConditionTextTemp[i][0] = "block" + ((31*4)+((sector-31)*16)+i);
                    accessConditionTextTemp[i][1] = Constants.trailBitsToCondition.get(finalBinaryStrings[i]);
                }
            } else {
                if (i != 3) {
                    accessConditionTextTemp[i][0] = "block" + ((sector*4)+i);
                    accessConditionTextTemp[i][1] = Constants.accessBitsToConditions.get(finalBinaryStrings[i]);
                } else {
                    accessConditionTextTemp[i][0] = "block" + ((sector*4)+i);
                    accessConditionTextTemp[i][1] = Constants.trailBitsToCondition.get(finalBinaryStrings[i]);
                }
            }
            
            
        }

        accessConditionTextTemp[4][0] = "UserData";
        accessConditionTextTemp[4][1] = String.format("%2s", Integer.toHexString(access[3])).replaceAll(" ", "0");
        
        // for (int i = 0; i < accessConditionTextTemp.length; i++) {
        //     System.out.println(i + ": ");
        //     for (int g = 0; g < accessConditionTextTemp[0].length; g++) {
        //         System.out.println(accessConditionTextTemp[i][g] + " ");
        //     }
        // }

        //int y = Integer.
        return accessConditionTextTemp;
        
    }
}
