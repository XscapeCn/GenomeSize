import java.io.*;

public class Bind {
    public static void main(String[] args) {
        readFile(args[0]);
    }

    public static void readFile(String filename){
        String outName = filename.split("\\.")[0] + ".1.depth";
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outName));
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = br.readLine()) != null){
                String[] temp = str.split("\t");
                bw.write(temp[0] + "-" + temp[1] + "\t" + temp[2]);
                bw.write("\n");
            }

            br.close();
            bw.close();

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

    }
}
