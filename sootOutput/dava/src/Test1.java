
public class Test1
{

    public static void main(String[]  r0)
    {

        byte b0, b1;
        b0 = (byte) (byte) 0;
        b1 = (byte) (byte) 95;
        Test1.func1(b0);
        Test1.func1(b1);
        MyCounter.report();
    }

    public static void func1(int  i0)
    {

        int i1, i2;
        label_0:
        if (i0 != 0)
        {
            while (true)
            {
                MyCounter.increase(0);
                MyCounter.line("temp$0 = x % 4", 0);
                i1 = i0 % 4;

                if (i1 == 0)
                {
                    break label_0;
                }

                MyCounter.increase(SocketFlow.NORMAL_PRIORITY);
                MyCounter.line("temp$1 = x / 4", SocketFlow.NORMAL_PRIORITY);
                i2 = i0 / 4;
                i0 = i2;
            }
        }
    }
}
