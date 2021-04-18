import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        int AI = 0;
        int BI = 0;
        int DI = 0;

        long ADepth = 0;
        long BDepth = 0;
        long DDepth = 0;

        int index = 100000000;

        List<Integer> AAA = new ArrayList<>();
        for (int i = 0 , length = A.length; i < length; i++) {
            AAA.add(A[i]);
        }

//        long startTime=System.currentTimeMillis();
//        for (int i = 0; i < index; i++) {
//            if (i%2 == 0){
//                ADepth += Long.parseLong("1");
//            }
//        }
//        System.out.println(ADepth);
//
//        System.out.println(System.currentTimeMillis() - startTime);
//
//        System.out.println("===============");
//
//
//        long startTime1=System.currentTimeMillis();
//        for (int i = 0; i < index; i++) {
//            if (i%2 == 0) BDepth += Integer.parseInt("1");
//        }
//        System.out.println(BDepth);
//
//        System.out.println(System.currentTimeMillis() - startTime1);
//        System.out.println("===============");


        long startTime2=System.currentTimeMillis();
        for (int i = 0; i < index; i++) {
            if (i % (AAA.get(1)) == 0){
                AI += 1;
            }
        }

        System.out.println(System.currentTimeMillis() - startTime2);
        System.out.println("===============");


        long startTime3=System.currentTimeMillis();
        for (int i = 0; i < index; i++) {
            if (i % (A[1]) == 0){
                BI += 1;
            }
        }
        System.out.println(System.currentTimeMillis() - startTime3);





//        for (int a:DD
//             ) {
//
//            System.out.println(a);
//        }
//
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(args[0]));
//
//            String str;
//            while ((str = br.readLine()) != null){
//
//                String[] temp = str.split("\t");
//                int start = Integer.parseInt(temp[0]);
//
//                if (AA.contains(start)){
//                    ADepth += Long.parseLong(temp[2]);
//                } else if (BB.contains(start)){
//                    BDepth += Long.parseLong(temp[2]);
//                } else if (DD.contains(start)){
//                    DDepth += Long.parseLong(temp[2]);
//                }
//            }
//            br.close();
//
//        }catch(Exception e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//        String[] res = new String[]{args[0], String.valueOf(ADepth), String.valueOf(BDepth), String.valueOf(DDepth), String.valueOf(ADepth + BDepth + DDepth)};
//
//        for (String re : res) {
//            System.out.println(re);
//        }
    }
}

