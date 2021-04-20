package CRC;

public class CRCStarter {
    public static void main(String[] args) {
        if(args[0] == "CRC"){
            new CRC(args);
        }else if (args[0] == "CalTE"){
            new CalTE(args);
        }
    }
}
