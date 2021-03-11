import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class burnAfterRead {
    public static void main(String[] args) {

        MyInterFace<String> mf = (String s) -> System.out.println(s);
        //lambda 用于函数式接口的实现。接口的实现需要一个函数的传入，类似method4。 此时lambda相当于一个函数（编程意义上的函数，参数，方法体，返回值）。
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
    }
}

interface MyInterFace<T>{
    void test(T avr);
}

class MF<T> implements MyInterFace<T>{
    @Override
    public void test(T var) {
        System.out.println(var);
    }
}

