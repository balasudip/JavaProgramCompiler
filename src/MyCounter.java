import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
// The counter class
public class MyCounter {
  // the counter, initialize to zero
  private static int cnt=0;
  
  private static List line= new ArrayList();
  private static int[] arr= new int[100];
  
  //keep a tab of execution for each line
  public static synchronized void increase(int num) {
	  cnt+=num;
    arr[num]=arr[num]+1;
  }
  //print for every line
  public static synchronized void report() {
	  for (int i=0;i<line.size();i++)
	  { 
		  System.out.println(line.get(i)+" : " +arr[i]);
			
	  }
	  
  }
  //take a note of every line
 public static synchronized void line(String n,int num){
	 
	 if ( !line.contains(n) || num>line.lastIndexOf(n))
	 {
		 line.add(n);
	 }
	
	}
}