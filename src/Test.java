import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class Test {

    public static void main(String[] args){
        final int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
        final int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
        final int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};
        HashSet<Integer> AA = new HashSet<>();
        HashSet<Integer> BB = new HashSet<>();
        HashSet<Integer> DD = new HashSet<>();

        for (int j : A) AA.add(j);
        for (int j : B) BB.add(j);
        for (int j : D) DD.add(j);

        long ADepth = 0;
        long BDepth = 0;
        long DDepth = 0;

        for (int a:DD
             ) {

            System.out.println(a);
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));

            String str;
            while ((str = br.readLine()) != null){

                String[] temp = str.split("\t");
                int start = Integer.parseInt(temp[0]);

                if (AA.contains(start)){
                    ADepth += Long.parseLong(temp[2]);
                } else if (BB.contains(start)){
                    BDepth += Long.parseLong(temp[2]);
                } else if (DD.contains(start)){
                    DDepth += Long.parseLong(temp[2]);
                }
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        String[] res = new String[]{args[0], String.valueOf(ADepth), String.valueOf(BDepth), String.valueOf(DDepth), String.valueOf(ADepth + BDepth + DDepth)};

        for (String re : res) {
            System.out.println(re);
        }
    }
}

