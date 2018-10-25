/*
 * Author: Swrajit Paul
 */
import java.io.*;
import java.util.ArrayList;

public class OSProcess{
	public static ProcessBuilder pb;
	
	public OSProcess(){
		pb = new ProcessBuilder();
		pb.directory(new File(System.getProperty("user.dir")));
	}
	
	public static void process(ArrayList<String> list){

		 if(list.size() < 1){
			 System.err.println("Usage: java OSProcess <command>");
			 System.exit(0);
		 }
		 
		 // args[0] is the command that is run in a separate process
		 pb.command(list);
		 try {
			 Process process = pb.start();
			// obtain the input stream
			 InputStream is = process.getInputStream();
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr);
			 // read the output of the process
			 
			 String line;
			 while ( (line = br.readLine()) != null) {
				 System.out.println(line);
			 }
			 br.close();
		 }
		 catch(IOException e) {
			 System.out.println("Command not found");
		 }
	}
}
