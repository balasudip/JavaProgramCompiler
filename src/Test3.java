import jdk.net.SocketFlow;


public class Test3
{

    public static void main(String[]  r0)
    {


        Test3.func1(100);
        Test3.func2(10);
        MyCounter.report();
    }

    public static void func1(int  i0)
    {

        int i2;
        while (true)
        {
            MyCounter.increase(0);
            MyCounter.line("if x > 0 goto nop", 0);

            if (i0 <= 0)
            {
                return;
            }

            MyCounter.increase(SocketFlow.NORMAL_PRIORITY);
            MyCounter.line("temp$0 = x", SocketFlow.NORMAL_PRIORITY);
            i2 = i0 + (-1);
            i0 = i2;
        }
    }

    public static void func2(int  i0)
    {

        int i2;
        while (true)
        {
            MyCounter.increase(SocketFlow.HIGH_PRIORITY);
            MyCounter.line("if x < 100 goto nop", SocketFlow.HIGH_PRIORITY);

            if (i0 >= 100)
            {
                return;
            }

            MyCounter.increase(3);
            MyCounter.line("temp$0 = x", 3);
            i2 = i0 + SocketFlow.NORMAL_PRIORITY;
            i0 = i2;
        }
    }
}
