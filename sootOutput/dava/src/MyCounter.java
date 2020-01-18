import java.util.ArrayList;
import java.io.PrintStream;
import java.util.List;

public class MyCounter
{
    private static int cnt;
    private static List line;
    private static int[] arr;

    static
    {


        cnt = 0;
        line = new ArrayList();
        arr = new int[100];
    }

    public static synchronized void increase(int  i0)
    {


        cnt = cnt + i0;
        arr[i0]++;
    }

    public static synchronized void report()
    {

        int i0;
        for (i0 = 0; i0 < line.size(); i0++)
        {
            System.out.println((new StringBuilder()).append(line.get(i0)).append(" : ").append(arr[i0]).toString());
        }
    }

    public static synchronized void line(String  r0, int  i0)
    {


        label_0:
        {
            if (line.contains(r0) && i0 <= line.lastIndexOf(r0))
            {
                break label_0;
            }

            line.add(r0);
        } //end label_0:

    }
}
