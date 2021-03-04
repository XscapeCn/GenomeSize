import java.util.ArrayList;
import java.util.Arrays;

public class SomethingInteresting {
    public static void main(String[] args) {

    }

    /**\
     * return type is different between parseInt & valueOf
     * Integer.valueOf(s) = new Integer(Integer.parseInt(s))
     */
    void parseIntInsteadOfValueOf(){
        String str = "2";
        int strToInt = Integer.parseInt(str);
        Integer strToInteger = Integer.valueOf(str);
        System.out.println(strToInteger.getClass().getName()); //Integer
    }


    /**
     * Both of these have run(), but the difference between touch the moral of inherit & interface
     * which is Inherit only if you want to override some behavior.
     * Inherit less, interface more.
     */
    void threadVSRunnable(){
        extendsThread ext = new extendsThread();
        ext.run();
        implementRunnable imr = new implementRunnable();
        imr.run();
    }

    /**
     * It seems that to.Array(new Object[0]) is faster
     */
    void numberOfToArray(){
        ArrayList<String> aa = new ArrayList<>();
        aa.add("A");
        aa.add("B");
        aa.add("C");
        aa.add("D");
        aa.add("E");

        String[] bb = aa.toArray(new String[aa.size()]);
        System.out.println(Arrays.toString(bb));
        String[] cc = aa.toArray(new String[0]);
        System.out.println(Arrays.toString(cc));
    }

}

class extendsThread extends Thread{
    public void run(){
        System.out.println("This method extends Class Thread");
    }
}

class implementRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("This method implements Runnable");
    }
}