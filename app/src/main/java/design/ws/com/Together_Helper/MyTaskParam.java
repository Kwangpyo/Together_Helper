package design.ws.com.Together_Helper;

public class MyTaskParam {

    int foo;
    long bar;
    double arple;
    String str;

    MyTaskParam(int foo, long bar, double arple,String str) {
        this.foo = foo;
        this.bar = bar;
        this.arple = arple;
        this.str = str;
    }

    public MyTaskParam(int foo, String str) {
        this.foo = foo;
        this.str = str;
    }
}
