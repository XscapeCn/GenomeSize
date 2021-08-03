import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AnaGff {

    public static void main(String[] args) {
        File[] files = getFiles(args[0]);

        String outFile;
        if (!args[0].endsWith("/")){
            outFile = args[0] + "/" + "out.txt";
        }else {
            outFile = args[0] + "out.txt";
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            for (File file: files) {
                bw.write(file.getName() + "\n");

                List<Integer> res = new ArrayList<>();
                List<Integer> res2 = new ArrayList<>();
                int a = 1;
                String str;
                int sum = 0;
                int num = 0;


                BufferedReader br = new BufferedReader(new FileReader(file));
                while ( (str = br.readLine()) != null) {
                    String[] split = str.split("\t");
                    int e = Integer.parseInt(split[4]);
                    if (e > a * 30000000) {
                        a += 1;
                        res.add(sum);
                        res2.add(num);
                        sum = 0;
                        num = 0;
                    }
                    sum += (e - Integer.parseInt(split[3]));
                    num += 1;
                }
                res.add(sum);
                res2.add(num);
                for (int i = 0; i < res.size(); i++) {
                    bw.write(res.get(i) + "\t" + res2.get(i) + "\n");
                }
                bw.write(getSum(res) + "\t" + getSum(res2) + "\n");
                br.close();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static File[] getFiles(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    public static int getSum(List<Integer> a){
        int sum = 0;
        for (int aa:a
             ) {
            sum += aa;
        }
        return sum;
    }
}