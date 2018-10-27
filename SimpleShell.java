/**
 * Author: Swrajit Paul
 */
import java.io.*;
import java.util.ArrayList;

public class SimpleShell {
 
	public static void main(String[] args) throws java.io.IOException {
		
		String commandLine;
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		
		// process builder
		ProcessBuilder pb = new ProcessBuilder();
		
		ArrayList<String> history = new ArrayList<String>();
		
		int index = 0; // number of commmands inputted 
		
		OSProcess os = new OSProcess();
		
		while (true){
			 
			 try{
				 
				// read what the user entered
				 System.out.print("jsh>");
				 commandLine = console.readLine();
				 			 
				 // break out with <control><C> different versions 
				 if (commandLine.equals("<control><C>") || commandLine.equals("<Control><C>") || commandLine.equals("<control><c>")) {
					System.out.println("exited"); 
					System.exit(0);
				 } 
				 
				// if the user entered a return, just loop again
				 if (commandLine.equals("")) {
					 continue;
			 	 }
				 
				// parsing commands and adding them into the list
				 String[] st = commandLine.split(" ");
				 ArrayList<String> list = new ArrayList<String>();
				 
				 
				 String OSName = System.getProperty("os.name");
				 
				 // if windows then add two things to the list 
				 if(OSName.contains("Windows")) {
					 list.add("cmd.exe");
					 list.add("/c");
				 }
				 
				 // if the command is not history nor !! then add to history and increment
				 if(!commandLine.equalsIgnoreCase("history") && !commandLine.matches("!!") && !((commandLine.charAt(0) == '!') && (commandLine.charAt(1) != '!'))) {
					 history.add(index + " " + commandLine);
					 index++;
				 }
				 
				 // loop through the list and add it into the list that will be passed to the processbuilder
				 for(String s: st){ 
					 if(!commandLine.equalsIgnoreCase("history") && !commandLine.matches("!!") && !((commandLine.charAt(0) == '!') && (commandLine.charAt(1) != '!'))) {
						 list.add(s);
					 }
				 }
				 
				 // if cd ----> change directory
				 if(st[0].equalsIgnoreCase("cd")) {
					 // if change directory --- go back to home 
					 if(st.length == 1) {
						 os.pb.directory(new File(System.getProperty("user.dir")));
						 System.out.println(os.pb.directory());
						 continue;
					 }
					 // else change directory to the following
					 else if(st.length == 2){
						 File home = os.pb.directory();
						 String[] lst = home.list();
						 boolean found = false; // file not found
						 for (String s: lst) {
							 //if file is found 
							if(s.equalsIgnoreCase(st[1])) {
								File temp = new File(s);
								// check if its a directory or same as not file
								if(!temp.isFile()) {
									os.pb.directory(temp.getAbsoluteFile());
									System.out.println(os.pb.directory());
									found = true;
								}
								// print out error not a dorectory
								else {
									System.out.println(st[1] + " is not a directory");
									found = true;
									break;
								}
							}
						 }
						 // if file not found
						 if(!found) {
							System.out.println("The system cannot find the path specified.");
							continue;
						 }
						 continue;
					 }
					 // if the cd command has more than two arguments
					 else {
						 System.out.println("The syntax of the command is incorrect.");
						 continue;
					 }
					 
				 }
				 
				 // if command is history 
				 if(st[0].equalsIgnoreCase("history")){
					 String[] hist = new String[history.size()];
					 history.toArray(hist);
					 for(String s : hist)
						 System.out.println(s);
					 history.add(index + " " + commandLine);
					 index++;
					 continue;
				 }
				
				 // if command is !!
				 if(commandLine.equalsIgnoreCase("!!")){
					 String[] hist = new String[history.size()];
					 history.toArray(hist);
					 // check to see if previous command exists
					 if(hist.length != 0) {
						 String[] temp = hist[hist.length-1].split(" ");
						 // if previous was history command
						 if(temp[1].equalsIgnoreCase("history")){
							 String[] hist2 = new String[history.size()];
							 history.toArray(hist2);
							 for(String s : hist2)
								 System.out.println(s);
							 history.add(index + " " + "history");
							 index++;
							 continue;
						 }
						 String s = "";
						 for (int i = 1; i < temp.length; i++) {
							 list.add(temp[i]);
							 s += temp[i] + " ";
						 }
						 history.add(index + " " + s);
						 index++;
					 }
					 // no prevous command
					 else {
						 System.out.println("No previous command");
						 continue;
					 }
				 }
				 
				 // if a specific command is chosen to run
				 if(commandLine.charAt(0) == '!' && commandLine.charAt(1) != '!') {
					 String str = "";
					 for(char c: commandLine.toCharArray()) {
						 if(c != '!') {
							 str += c;
						 }
					 }
					  
					 int n = Integer.parseInt(str);
					 String[] hist = new String[history.size()];
					 history.toArray(hist);
					 // if the command number chosen exists 
					 if(hist.length != 0 && n < hist.length) {
						 String[] temp = hist[n].split(" ");
						 if(temp[1].equalsIgnoreCase("history")){
							 String[] hist2 = new String[history.size()];
							 history.toArray(hist2);
							 for(String s : hist2)
								 System.out.println(s);
							 history.add(index + " " + "history");
							 index++;
							 continue;
						 }
						 String s = "";
						 for (int i = 1; i < temp.length; i++) {
							 list.add(temp[i]);
							 s += temp[i] + " ";
						 }
						 history.add(index + " " + s);
						 index++;
						 
					 }
					 else {
						 System.out.println("Command " + n + " doesnt exist" );
						 continue; 
					 }
					 
				 }
				 
				 os.process(list);
			 }
			 catch (IOException e){
				System.out.println("Error... Please try again!");
			 }
		}
	}
}
				