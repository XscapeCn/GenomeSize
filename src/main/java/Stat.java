import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stat {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        List<String> chr = new ArrayList<>();
        List<List<String[]>> block = new ArrayList<>();
        int init = 0;
        List<String[]> temp = new ArrayList<>();

        String str;
        while ( (str = br.readLine()) != null){
            String[] split = str.split("\t");
            if (Integer.parseInt(split[6]) != init) {
                init = Integer.parseInt(split[6]);
                block.add(temp);
                temp = new ArrayList<>();
            }
            temp.add(split);

        }
//        for (List<String[]> strings : block) {
//            System.out.println(Arrays.toString(strings.get(0)));
//        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
        for (int i = 0; i < block.size(); i++) {
            String[] aa = proBlock(block.get(i));
            for (int j = 0; j < aa.length; j++) {
                bw.write(aa[j] + "\t");

            }
            bw.write("\n");
//            System.out.println(Arrays.toString(aa));

        }
        br.close();
        bw.close();

    }

    public static String[] proBlock(List<String[]> block){
        DecimalFormat defor = new DecimalFormat("0.000");
        String[] res= new String[5];
        res[0] = block.get(0)[0];
        double mean = 0;
        double sumMean = 0;
        for (int i = 0; i < block.size(); i++) {
            mean += Double.parseDouble(block.get(i)[4]);
            int t = (Integer.parseInt(block.get(i)[2])) - Integer.parseInt(block.get(i)[1]);
            sumMean += Double.parseDouble(block.get(i)[4]) * t;
        }
        res[1] = String.valueOf(defor.format(mean/block.size()));
        res[2] = String.valueOf(defor.format(sumMean/block.size()));
        res[3] = block.get(0)[6];
        res[4] = block.get(0)[7];
        return res;
    }
}
