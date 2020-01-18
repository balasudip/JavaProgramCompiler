Compiler Construction CS201 Project
Madhurima Chakraborty-862174877
Sudip Bala-862188812


Instructions Peculiar to our implementation:
1.Change argument to the test file and edit the same file in static code analysis body(marked in program).
2.Run the program with the updated code and arguments.
3.The static analysis results can be found on the console and a jimple file will be generated in the sootOutput folder.
4.Decompile the code in sootOutput.
5.A java file file should be generated in the sootOutput>dava>src folder.
6.Copy this code to the default package.
7.At this point, you might come across a couple of errors due to SocketFlow, change the parameters as stated below:
	
Before Change
After Change
SocketFlow.NORMAL_PRIORITY
1
SocketFlow.HIGH_PRIORITY
2
SocketFlow.UNSET
-1

Optionally, you may also add the statement -import jdk.net.SocketFlow but, you might have to edit the value for UNSET to -1. We don’t recommend this solution if you are using any other test cases. The reason being, the SocketFlow does not gaurantee to fetch the right values. The replacement of the SocketFlow parameters as mentioned in the table is consistent with the jimple code. So, in case any other test script is used, and a SocketFlow parameter is generated other than the ones mentioned in the table, we would recommend to navigate to the same line in the jimple code and replace the SocketFlow parameter with the value in the corresponding jimple statement.


Also, We have intentionally left all our test results for your benefit with the changed Decompiled codes as indicated above.
