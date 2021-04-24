package CRC;

public class CRCStarter {
    public static void main(String[] args) {
        System.out.println(args[0]);
        if(args[0].contains("CRC")){
//            System.out.println("CRC");

            new CRC(args);
        }else if (args[0].contains("CalTE")){
//            System.out.println("CalTE");

            new CalTE(args);
        }
    }
}
