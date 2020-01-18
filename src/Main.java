import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.SootMethod;
import soot.jimple.IntConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.util.Chain;

public class Main {
	static SootClass counterClass;
    static SootMethod increaseCounter, reportCounter, lineCounter;

	public static List<Unit> dynstmt = new ArrayList<Unit>();
public static void main(String[] args) {

//Static Analysis (Retrieve Flow Graph)
staticAnalysis();

//Dynamic Analysis (Instrumentation)
dynamicAnalysis();

Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
Scene.v().addBasicClass("MyCounter",SootClass.SIGNATURES);
soot.Main.main(args);

System.out.println("-------------------------------------");
System.out.println("End of execution");
}


private static void staticAnalysis(){
configure("/home/nimmo/Desktop/CS201Fall19/CS201Fall19/Analysis/"); //Change this path to your Analysis folder path in your project directory
SootClass sootClass = Scene.v().loadClassAndSupport("Test4"); //Input your test code (same as in arguments)
   sootClass.setApplicationClass();
   //Static Analysis code
   Body body = null;
        for (SootMethod method : sootClass.getMethods()) {
                if (method.isConcrete()) {
                    body = method.retrieveActiveBody();
            }
        System.out.println("Method:" + method.toString());

        System.out.println("Blocks:");
        BlockGraph blockGraph = new ExceptionalBlockGraph(body);
        for (Block block : blockGraph.getBlocks()) {
        System.out.println("BB" + block.getIndexInMethod());
        for(Iterator<Unit> itr1 = block.iterator(); itr1.hasNext();)
{
Unit u = itr1.next();
System.out.println(u);
}
        System.out.println("\n");
        }
        //Printing the loops
      LoopNestTree loopNestTree = new LoopNestTree(body);
      Integer cnt = 0;
      for (Loop loop : loopNestTree)
             {
              String str="";
              cnt++;
             
              for (Block block : blockGraph.getBlocks()) {
                if (block.getHead()==loop.getHead()){
                str+="Loop: [BB"+block.getIndexInMethod();
        }
               
                if (block.getTail()==loop.getBackJumpStmt()){
                str+=" BB"+block.getIndexInMethod()+"]";
        }
               
                }
              if (str.length()>0){
                System.out.println(str);
                }
             }
     
      System.out.println("Total loops found:" + cnt+"\n");
     
     //Printing the loop invariants
      System.out.println("Instructions having definitions outside the loop: ");
      Integer cnt2 = 0;
      UnitGraph g=new ExceptionalUnitGraph(body);
      SimpleLocalDefs sld=new SimpleLocalDefs(g);
      List<Stmt> headers = new ArrayList<Stmt>();
      
      for (Loop loop : loopNestTree)
      {		
    	  headers=loop.getLoopStatements();
    	  for (Unit head: headers){
    		  for (ValueBox useBox : head.getUseBoxes()) {
    		        Value v = useBox.getValue();
    		        if (v instanceof Local) {
    		          // Add this statement to the uses of the definition of the local
    		          Local l = (Local) v;
    		          List<Unit> defs = sld.getDefsOfAt(l, head);
    		          for (Unit def: defs){
    		        	  if (!(headers.contains(def))){
    		        		  cnt2+=1;
    		        		  System.out.println(head);
    		        		  dynstmt.add(head);
    		        	  }
    		          }
    		          
    		        }
    		        }

    	  }
      }
      if (cnt2==0){
    	  System.out.println("None\n");
    	  
      }
      else{
    	  System.out.println("\n");
      }
    	  	
            }
}
        
            
        

       
       
private static void dynamicAnalysis(){
	
PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {
	/* some internal fields */
	
@Override
protected void internalTransform(Body arg0, String arg1, Map arg2) {
//Dynamic Analysis (Instrumentation) code
	
	List<SootMethod> methods = arg0.getMethod().getDeclaringClass().getMethods();
	Body body=arg0;
    
    //Use MyCounter class and its method
    counterClass    = Scene.v().loadClassAndSupport("MyCounter");
    increaseCounter = counterClass.getMethod("void increase(int)");
    reportCounter   = counterClass.getMethod("void report()");
    lineCounter   = counterClass.getMethod("void line(java.lang.String,int)");
	synchronized(this)
    {
        if (!Scene.v().getMainClass().
                declaresMethod("void main(java.lang.String[])"))
            throw new RuntimeException("couldn't find main() in mainClass");

     }
	
	
		//instrumentation of code
        boolean isMainMethod = body.getMethod().getSubSignature().equals("void main(java.lang.String[])");
        
        Chain units = body.getUnits();
        
        for (int i = 0; i < dynstmt.size(); i++) {
        	Iterator stmtIt = units.snapshotIterator();
        while(stmtIt.hasNext())
        {
               Stmt s= (Stmt) stmtIt.next();
            if (dynstmt.get(i)==(s))
            {
            	InvokeExpr incExpr= Jimple.v().newStaticInvokeExpr(increaseCounter.makeRef(),
            		                                                        IntConstant.v(i));
            		    
            		    Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
            		    units.insertBefore(incStmt, s);
            		    InvokeExpr reportExpr1= Jimple.v().newStaticInvokeExpr(lineCounter.makeRef(),StringConstant.v(dynstmt.get(i).toString()),IntConstant.v(i));
                        Stmt reportStmt2 = Jimple.v().newInvokeStmt(reportExpr1);
                        units.insertBefore(reportStmt2, s);
            		    
            }
            
            
        }
       
        }
        
        
        //reporting statement at the end of main
        Iterator stmtIt2 = units.snapshotIterator();
        
        while(stmtIt2.hasNext())
        {
            Stmt s2 = (Stmt) stmtIt2.next();
           
            if (isMainMethod && (s2 instanceof ReturnStmt || s2 instanceof ReturnVoidStmt))
            	
            {
            	InvokeExpr reportExpr2= Jimple.v().newStaticInvokeExpr(reportCounter.makeRef());
                Stmt reportStmt2 = Jimple.v().newInvokeStmt(reportExpr2);
                units.insertBefore(reportStmt2, s2);
            }
        }
    }
 
}

));


}

public static void configure(String classpath) {
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_soot_classpath(classpath);
        Options.v().set_prepend_classpath(true);
        Options.v().setPhaseOption("cg.spark", "on");   
        
        
    }
}

