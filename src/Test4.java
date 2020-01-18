import jdk.net.SocketFlow;


public class Test4
{

    public static void main(String[]  r0)
    {

        int i0, i2;
        i0 = 10;

        do
        {
            MyCounter.increase(0);
            MyCounter.line("temp$0 = x", 0);
            i2 = i0 + SocketFlow.NORMAL_PRIORITY;
            i0 = i2;
        }
        while (i0 < 40);

        MyCounter.report();
    }
}
