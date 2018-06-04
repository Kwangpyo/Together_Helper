package design.ws.com.Together_Helper.params;

public class MyTaskParam {

    int foo;
    long bar;
    double arple;
    String str;

    public MyTaskParam(int foo, long bar, double arple,String str) {
        this.foo = foo;
        this.bar = bar;
        this.arple = arple;
        this.str = str;
    }

    public MyTaskParam(int foo, String str) {
        this.foo = foo;
        this.str = str;
    }

    public int getFoo() {
        return foo;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

    public long getBar() {
        return bar;
    }

    public void setBar(long bar) {
        this.bar = bar;
    }

    public double getArple() {
        return arple;
    }

    public void setArple(double arple) {
        this.arple = arple;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
