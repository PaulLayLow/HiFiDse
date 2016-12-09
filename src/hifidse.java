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
	public static int docId = 0; //Assigns a docID to a docUrl
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
			case "searchable": 
				searchable();
				break;
		 	case "token": 
		 		token();
		 		break;
		 	case "tokendebug": 
		 		tokendebug();
		 		break;
		 	case "stopword": 
		 		stopword();
		 		break;
		 	case "stem": 
		 		stem();
		 		break;
		 	case "stemdebug": 
		 		stemdebug();
		 		break;
		 	case "invert": 
		 		invert();
		 		break;
		 	default:
		 		queryEngine(); 
		 		break;      
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
			wordOffset = 0; 
			//input file
			File[] dir = new File(name).listFiles();
			showFilesToken(dir, bwout, false);
			 
			//close file
			bwout.close();
		}
		catch (Exception e){
			 System.out.println("Error occured while trying to tokenize file");
		}
	}
	
	//Recursive method to go through files for token
	public static void showFilesToken(File[] files, BufferedWriter bwout, boolean debug) {         
		try{
			for (File file : files) {
	        
				//if a directory
				if (file.isDirectory()) {
					showFilesToken(file.listFiles(), bwout, debug); // Calls same method again.
				} 
	          
				//if a file
				else {
					String fullPath = file.getAbsolutePath(); //only process text-readable files
					if (Arrays.asList(txtExtensions).contains(fullPath.substring(fullPath.indexOf("."), fullPath.length()))){
						readFile(file, bwout, debug);
					}
				}
			}
		}
	    
		catch (Exception e){
			System.out.println("Error occured while listing files in directory");
		}
	}	 
		
	//Read method
	public static void readFile(File fin, BufferedWriter bwout, boolean debug) throws IOException{
		Scanner br = new Scanner(new File(fin.toString())); //reading the file
			
		while (br.hasNext()){
			String[] tokens = br.next().split("\\s+"); //split document by spaces
			for (int i = 0; i < tokens.length; i++){
				writeFile(bwout, tokens[i].toLowerCase(), fin, debug); //convert to lower case and write to output file.
			}
		}
		docId++;
		br.close();
	}
	 
	//Write method
	public static void writeFile(BufferedWriter bwout, String bwin, File fin, boolean debug) throws IOException {
		if (debug){	
			bwout.write("("+ fin + "," + bwin + "," + wordOffset + "," + "0)"); //write in the order specified (docURL, word, offset, attrVal)
		}
		else{
			bwout.write("("+ docId + "," + bwin + "," + wordOffset + "," + "0)");
		}
		bwout.newLine();
		wordOffset++; //plus 1 after every word
	}

	public static void tokendebug(){
		try{	 
			//writer
			File fout = new File(path + "\\step2d.txt"); //Specify output file
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(fos)); 
			wordOffset = 0; 
			//input file
			File[] dir = new File(name).listFiles();
			showFilesToken(dir, bwout, true);
			 
			//close file
			bwout.close();
		}
		catch (Exception e){
			 System.out.println("Error occured while trying to tokenize file");
		}
	}
 
	public static void stopword(){
		try{
			token(); 
			 
			//writer
			File fout = new File(path + "\\step3.txt"); //Specify output file
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(fos)); 
			 
			//input file
			File fin = new File(path + "\\step2.txt");
			 
			//read file
			readFileStopwords(fin, bwout); 
			bwout.close();
		}
		catch (Exception e){
			 
		}
	}
	
	//Read/Write method - for stopwords, read previous step instead of original file.
	public static void readFileStopwords(File fin, BufferedWriter bwout) throws IOException{
		Scanner br = new Scanner(new File(fin.toString())); //reading the file
		while (br.hasNext()){
			String[] tokens = br.next().split("\\r\\n|\\n|\\r"); //split document by newline
			for (int i = 0; i < tokens.length; i++){
				if (!tokens[i].contains("i,") && !tokens[i].contains("a,") && !tokens[i].contains("about,") && !tokens[i].contains("an,") && !tokens[i].contains("are,") && !tokens[i].contains("as,") && !tokens[i].contains("at,") && !tokens[i].contains("be,") && !tokens[i].contains("by,") && !tokens[i].contains("com,") && !tokens[i].contains("en,") && !tokens[i].contains("for,") && !tokens[i].contains("from,") && !tokens[i].contains("how,") && !tokens[i].contains("in,") && !tokens[i].contains("is,") && !tokens[i].contains("it,") && !tokens[i].contains("of,") && !tokens[i].contains("on,") && !tokens[i].contains("or,") && !tokens[i].contains("that,") && !tokens[i].contains("the,") && !tokens[i].contains("this,") && !tokens[i].contains("to,") && !tokens[i].contains("was,") && !tokens[i].contains("what,") && !tokens[i].contains("when,") && !tokens[i].contains("where,") && !tokens[i].contains("who,") && !tokens[i].contains("will,") && !tokens[i].contains("with,") && !tokens[i].contains("www,")){
					bwout.write(tokens[i]); //convert to lower case and write to output file.
					bwout.newLine();
				}
			}
		}
		br.close();
	}
	
	//Stem method 
	public static void stem(){
		try{
			stopword(); 
			 
			//writer
			File fout = new File(path + "\\step4.txt"); //Specify output file
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(fos)); 
			 
			//input file
			File fin = new File(path + "\\step3.txt");
			 
			//read file
			readFileStem(fin, bwout); 
			bwout.close();
		}
		catch (Exception e){
			 
		}
	}
	
	//Read/Write method - for stemming, read previous step instead of original file.
	public static void readFileStem(File fin, BufferedWriter bwout) throws IOException{
		Scanner br = new Scanner(new File(fin.toString())); //reading the file

		while (br.hasNext()){
			String[] tokens = br.next().split("\\r\\n|\\n|\\r"); //split document by spaces
			for (int i = 0; i < tokens.length; i++){
				if (tokens[i].contains("ies,") && !tokens[i].contains("eies,") && !tokens[i].contains("aies,")){
					String stemToken = tokens[i].replaceAll("ies", "y");
					bwout.write(stemToken);
					bwout.newLine();
				}
				else if (tokens[i].contains("es,") && !tokens[i].contains("aes,") && !tokens[i].contains("ees,") && !tokens[i].contains("oes,")){
					String stemToken = tokens[i].replaceAll("es", "e");
					bwout.write(stemToken); 
					bwout.newLine();
				}
				else if (tokens[i].contains("s,") && !tokens[i].contains("us,") && !tokens[i].contains("ss,")){
					String stemToken = tokens[i].replaceAll("s", "");
					bwout.write(stemToken); 
					bwout.newLine();
				}
				else
				{
					bwout.write(tokens[i]);
					bwout.newLine();
				}
			}
		}
		br.close();
	}
	 
	public static void stemdebug(){
 	}
 
	public static void invert(){
	}
 
	public static void queryEngine(){
	}
	 
}


