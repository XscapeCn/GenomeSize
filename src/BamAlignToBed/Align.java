package BamAlignToBed;

public class Align {
    public static void main(String[] args) {

        new StreamAlignToBed(args[0], args[1], Integer.parseInt(args[2]));

        new StreamAlignWithCon(args[0],  args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));

    }
}
