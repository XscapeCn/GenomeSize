import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransFormat {
    public static void main(String[] args) throws  IOException {
        BufferedReader br  = new BufferedReader(new FileReader(args[0]));
        BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
        List<Integer> temp = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();

        String str = null;

        while ( (str = br.readLine()) != null){
            if (str.equals("")){
                temp.add(0);
            }else {
                temp.add(Integer.parseInt(str));
            }
        }

        List<Integer> te = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            if (i%21 == 0){
                te =  new ArrayList<>();
                res.add(te);
            }
            te.add(temp.get(i));
        }

        for (List<Integer> re : res) {
            for (Integer integer : re) {
                bw.write(integer + "\t");

            }
            bw.write("\n");
        }

        bw.close();
        br.close();
    }
}
