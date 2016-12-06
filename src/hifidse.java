//Paul Robert Lalo
//7863

import java.util.*;
import java.io.*;
import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.lang.*;

public class hifidse{
	public static String arguement; //To determine which method to run
	public static String name; //name of directory/file
	public static int dirCounter = 1; //counter for directory/files
	public static String[] txtExtensions = {".html" , ".htm" , ".txt" , ".cc" , ".cpp" , ".c" , ".h" , ".java"}; //text-readable extensions
	public static int wordOffset = 0; //Keeps position of words in the text
	final static String path = System.getProperty("user.dir"); //Get directory currently in
 
	 public static void main(String[] args){
		 try{  
			 int count = 0;
       
			 //Setting variables to determine what to do
			 for (String s: args){
				 if (count == 0){
					 arguement = s;
					 count++;
				 }
				 else{
					 name = s;
				 }
			 }
       
			 //Switching to different methods
			 switch (arguement){
			 	case "searchable": searchable();
			 	case "token": token();
			 	case "tokendebug": tokendebug();
			 	case "stopword": stopword();
			 	case "stem": stem();
			 	case "stemdebug": stemdebug();
			 	case "invert": invert();
			 	default: queryEngine();      
			 } 
		 }
    
		 catch (Exception e){
			 System.out.println("Error occured while handling arguement and name of directory/file");
		 }
	 }
 
	 //Searchable method
	 public static void searchable(){
		 try{
			 //writer
			 File fout = new File(path + "\\step1.txt");
			 FileOutputStream fos = new FileOutputStream(fout);
			 BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(fos));
       
			 bwout.write(name + " DIR " + dirCounter);
			 bwout.newLine();
			 File[] dir = new File(name).listFiles();
			 showFiles(dir, bwout);
       
			 bwout.close();
		 }
    
		 catch (Exception e){
			 System.out.println("File was not made.");
		 }
	 }
 
	 //Recursive method to go through files
	 public static void showFiles(File[] files, BufferedWriter bwout) {         
		 try{
			 String type;
			 for (File file : files) {
        
				 //if a directory
				 if (file.isDirectory()) {
					 dirCounter++;
					 String fullPath = file.getAbsolutePath();
					 String shortPath = fullPath.substring(fullPath.indexOf(name), fullPath.length());
					 bwout.write(shortPath + " DIR " + dirCounter);
					 bwout.newLine();
					 showFiles(file.listFiles(), bwout); // Calls same method again.
				 } 
          
				 //if a file
				 else {
					 dirCounter++;
					 String fullPath = file.getAbsolutePath();
					 String shortPath = fullPath.substring(fullPath.indexOf(name), fullPath.length());
					 
					 if (Arrays.asList(txtExtensions).contains(shortPath.substring(shortPath.indexOf("."), shortPath.length()))){
						 type = " TXTS ";
					 }
					 else{
						 type = " TXTN ";
					 }
					 
					 bwout.write(shortPath + type + dirCounter);
					 bwout.newLine();
				 }
			 }
		 }
    
		 catch (Exception e){
			 System.out.println("Error occured while listing files in directory");
		 }
	 }	
 
	 //Token method
	 public static void token(){
		 try{
			 //writer
			 File fout = new File(path + "\\step2.txt"); //Specify output file
			 FileOutputStream fos = new FileOutputStream(fout);
			 BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(fos)); 
			 
			 //input file
			 File fin = new File(path + "\\" +name);
			 
			 //read file
			 readFile(fin, bwout); 
			 bwout.close();
		 }
		 catch (Exception e){
			 
		 }
	 }
	 
	 //Read method
	 public static void readFile(File fin, BufferedWriter bwout) throws IOException{
			Scanner br = new Scanner(new File(fin.toString())); //reading the file
			
			while (br.hasNext()){
				String[] tokens = br.next().split("\\s+"); //split document by spaces
				for (int i = 0; i < tokens.length; i++){
					writeFile(bwout, tokens[i].toLowerCase(), fin); //convert to lower case and write to output file.
				}
			}
			br.close();
	 }
	 
	 //Write method
	 public static void writeFile(BufferedWriter bwout, String bwin, File fin) throws IOException {
			
			bwout.write("("+ fin + ", " + bwin + ", " + wordOffset + ", " + "0)"); //write in the order specified (docURL, word, offset, attrVal)
			bwout.newLine();
			wordOffset++; //plus 1 after every word
	}

	 public static void tokendebug(){
	 }
 
	 public static void stopword(){
	 }
 
	 public static void stem(){
	 }
 
	 public static void stemdebug(){
 	 }
 
	 public static void invert(){
	 }
 
	 public static void queryEngine(){
	 }
	 
}


