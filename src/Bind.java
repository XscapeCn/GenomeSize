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
            StringBuilder sb = null;
            while ((str = br.readLine()) != null){
                sb.setLength(0);
                String[] temp = str.split("\t");
                sb.append(temp[0]).append("-").append(temp[1]).append("\t").append(temp[2]);
                bw.write(sb.toString());
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
