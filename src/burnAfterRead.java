import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class burnAfterRead {
    public static void main(String[] args) {
        MyInterFace<String> mf = s -> System.out.println(s);
        //lambda 用于函数式接口的实现。接口的实现需要一个函数的传入，类似method4。 此时lambda相当于一个函数（编程意义上的函数，参数，方法体，返回值）。
        //lambda 给一个方法传递mf等价于传递lambda表达式。这是一种把方法作为参数进行传递的编程思想。
        mf.test("This is method1, lambda");

        MF<String> mf2 = new MF<>();
        mf2.test("This is method2, class implements interface");

        MyInterFace<String> mf3 = new MyInterFace<String>() {
            @Override
            public void test(String avr) {
                System.out.println(avr);
            }
        };
        mf3.test("This is method3, Inner override");

        MyInterFace<String> mff = burnAfterRead::charByChar;
        mff.test("This is method4, method reference");
    }

    public static void charByChar(String var){
        for (int i = 0; i < var.length(); i++) {
            System.out.print(var.charAt(i));
            System.out.print("-");
        }
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    }

    public void method1(String str1){
        Thread t1 = new Thread(
                () -> System.out.println(str1)
        );
        t1.start();
    }

}

interface MyInterFace<T>{
    void test(T var);
}

interface MyInterFace2<T>{
    void test();
}

class MF2<T> implements MyInterFace2<T>{
    T var;

    public MF2(T var){
        this.var = var;
    }

    @Override
    public void test() {
        System.out.println(this.var);
    }

    public static void charByChar(String var){
        for (int i = 0; i < var.length(); i++) {
            System.out.print(var.charAt(i));
            System.out.print("-");
        }
    }
}

class MF<T> implements MyInterFace<T>{

    @Override
    public void test(T var) {
        System.out.println(var);
    }

    public static void charByChar(String var){
        for (int i = 0; i < var.length(); i++) {
            System.out.print(var.charAt(i));
            System.out.print("-");
        }
    }
}