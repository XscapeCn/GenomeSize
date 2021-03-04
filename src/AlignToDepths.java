import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlignToDepths {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        ArrayList<String> align = new ArrayList<>();
        String str = null;
        while ((str = br.readLine()) != null){
//            temp.add(str.split("\t"));
            String[] aa = str.split("\t");
            align.add(aa[0] + aa[1]);
        }
        br.close();

        BufferedReader br2 = new BufferedReader(new FileReader(args[1]));
        BufferedWriter bw = new BufferedWriter(new FileWriter(args[2]));
        String str2;
        while ((str2 = br2.readLine()) != null){
            String[] aa = str2.split("\t");
            if (align.contains(aa[0] + aa[1])){
                bw.write(str2);
                bw.newLine();
            }
        }

        bw.close();
        br2.close();
    }
}
