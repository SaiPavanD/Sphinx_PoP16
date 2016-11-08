// package Speech_processing;

import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;
//import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

class Main{
    public static void main(String[] args){
		// String s = "class Factorial{    public static void main(String[] a){      "; // s should hold the entire code that was generated till now.
		// String test1 = "sistng"; // This is the token/word received from speech recognition.
		// String a = "name open public static void main open string open close args close open print open open number 1 close close";
		// Predict.generateProcessed(a);
		Predict.code = "";
		Predict.getWindow("class ab");
		System.out.println(Predict.code);
	  	//Predict.code = "class ab { public static";
	  	//Predict.getWindow("open public static");
		//System.out.println(Predict.getWindow("void"));
		//Predict.getWindow("main open spring open close ");
		Predict.getWindow("under args hello");
		System.out.println(Predict.code);
    	//Predict.getWindow("hello");
    }
}


// The class used for processing and predicting.
public class Predict{

   public static final int hamThreshold = 2; // Hamming distance threshold - might need to be changed later.
   public static String code = "";
   public static ArrayList<String> id_names = new ArrayList<String>();
   public static ArrayList<String> method_names = new ArrayList<String>(); 
   public static ArrayList<String> class_names = new ArrayList<String>();
   public static ArrayList<String> prev = new ArrayList<String>();
   public static ArrayList<String> idFonts = new ArrayList<String>(Arrays.asList("camel","title","under")); // Test which works better with speech.
   public static ArrayList<String> miniJavaTokens = new ArrayList<String>(
    Arrays.asList(
      "(", ")",
      "[", "]",
      "{", "}",
      ";", ".",
      "=", "<=",
      "!=", "+",
      "-","*",
      "/","&&",
      "||","!",
      "boolean","class",
      "interface","else",
      "extends","false",
      "if","while",
      "int","length",
      "main","new",
      "public","return",
      "static","String",
      "this","true",
      "// System.out.println","void",
      "#define"
    ) );
    public static ArrayList<String> myJavaTokens = new ArrayList<String>(
     Arrays.asList(
       "open", "close",
       "open", "close",
       "open", "close",
       "next", "dot",
       "equal", "less",
       "different", "plus",
       "minus","times",
       "by","and",
       "or","not",
       "boolean","class",
       "interface","else",
       "extends","false",
       "if","while",
       "integer","length",
       "main","new",
       "public","return",
       "static","string",
       "this","true",
       "print","void",
       "define"
     ) );

    // CODE WRITTEN BY NV AND REVANTH
    public static void storeIds(String token, String flag){
      if(flag.equals("class")){
        class_names.add(token);
      }
      else if(flag.equals("method")){
        method_names.add(token);
      }
      else if(flag.equals("identifier")){
        id_names.add(token);
      }
      else{
        ///    UNKNOWN ERROR.
      }
    }

    // Every time a token is processed , before adding it 
    // to the final code, call lastFour(token)
    public static void lastFour(String token){
      if(prev.size() == 4){
        prev.remove(0);
      }
      prev.add(token);
    }


    // this function is used to get the best identifier name 
    // from existing identifiers.
    public static String checkHamming(String token){
      
      if(prev.get(3).equals("declare") || prev.get(3).equals("new") || prev.get(3).equals("extends")){
        int hamCheck = Predict.hammingDistance(class_names, token);
        if(hamCheck != -1){
          return class_names.get(hamCheck);
        }
        else{
          return token;
        }
      }
      else if(prev.get(3).equals(".")){
        int hamCheck = Predict.hammingDistance(method_names, token);
        if(hamCheck != -1){
          return method_names.get(hamCheck);
        }
        else{
          return token;
        }
      }
      else{
        int hamCheck = Predict.hammingDistance(id_names, token);
        if(hamCheck != -1){
          return id_names.get(hamCheck);
        }
        else{
          return token;
        }
      }
    }

    // Once a token is finalised as an identifier, call the 
    // function processID which either stores or returns the correct Identifer.
    public static String processId(String token){
      if(prev.get(prev.size()-1).equals("class")){
        storeIds(token, "class");
        return token;
      }
      else if(prev.get(2).equals("public")){
        storeIds(token, "method");
        return token;
      }
      else if(prev.get(2).equals("declare")){
        storeIds(token, "identifier");
        return token;
      }
      else{
        return checkHamming(token);
      }
    }

    ///  END OF CODE BY NV AND REVANTH

   // Code written by Srinidhi
   public static String generateProcessed(String s){
   	String[] split_tokens;
   	ArrayList<String> next_tokens;
   	ArrayList<String> next_processed;
   	split_tokens = s.split(" ");
    String prev_token = null;
    String curr_token = null;

   	String toAdd;
   	int index;
    
   	for(int i=0;i<split_tokens.length;i++){
   		next_tokens = Predict.getNextToken(code);
       //System.out.println("VEdant : "+next_tokens+"\n");
   		next_processed = new ArrayList<String>();


      // Start of Code by Vedant
      for(int j=0;j<next_tokens.size();j++){
        index = miniJavaTokens.indexOf(next_tokens.get(j));
        if(index != -1)
          next_processed.add(myJavaTokens.get(index));

      }
      int hamCheck = Predict.hammingDistance(next_processed,split_tokens[i]);
      if(hamCheck != -1){
        curr_token = next_tokens.get(hamCheck);
        Predict.lastFour(curr_token);
        code += " " + curr_token;
      }
      else if(split_tokens[i].equals("number")){
      	i++;
      	curr_token = split_tokens[i];  
      	Predict.lastFour(curr_token); 
      	code += " " + curr_token;   	
      }
      else{
      	if(split_tokens[i].equals("declare")){
      		Predict.lastFour("declare");
      	}
      	else{
      		toAdd = "";
      		hamCheck = Predict.hammingDistance(idFonts,split_tokens[i]);
      		if(hamCheck == -1){
      			for(int j=i;j<split_tokens.length;j++){		  			      			
		  			toAdd += split_tokens[j];
		  		}
      		}
      		else if(hamCheck == 0){
      			char first;
      			toAdd = toAdd + split_tokens[i+1];
      			for(int j=i+2;j<split_tokens.length;j++){
      				first = split_tokens[j].charAt(0);
      				first = Character.toUpperCase(first);
      				toAdd = new StringBuilder(toAdd).append(first).append(split_tokens[j].substring(1)).toString(); 
      			}
      			
      		}
      		else if(hamCheck == 1){
      			char first;
      			first = split_tokens[i+1].charAt(0);
      			first = Character.toUpperCase(first);
      			toAdd = new StringBuilder(toAdd).append(first).append(split_tokens[i+1].substring(1)).toString();
      			for(int j=i+2;j<split_tokens.length;j++){		  			      			
		  			toAdd += split_tokens[j];
		  		}
      		}
      		else if(hamCheck == 2){
      			toAdd += split_tokens[i+1];
      			for(int j=i+2;j<split_tokens.length;j++){		  			      			
		  			toAdd += "_" + split_tokens[j];
		  		}
      		}
      		toAdd = Predict.processId(toAdd);
		  	Predict.lastFour(toAdd);
		  	code += " " + toAdd;
		  	break;
      		
      	}
      }

      // Set the current token
      /*if(myJavaTokens.contains(split_tokens[i])){
        index = next_processed.indexOf(split_tokens[i]);
        curr_token = next_tokens.get(index);
      } else if (split_tokens[i].equals("number")){
        curr_token = "<INTEGER_LITERAL>";
      } else {
        curr_token = "<IDENTIFIER>";
      }

      // System.out.println(curr_token);
      if(next_tokens.contains(curr_token)){

        if(curr_token.equals("<IDENTIFIER>")){
          code = code + " " + split_tokens[i];      // If the token is an identifier, add whatever has been encountered.
        } else if(curr_token.equals("<INTEGER_LITERAL>")){
          code = code + " " + split_tokens[i+1];    // If the token is a number, add the next thing.
          i++;
        } else {
          code = code + " " + curr_token;           // For a normal token, just add the same thing.
        }

      } else {
        if(curr_token.equals("<IDENTIFIER>")){
          if(prev_token.equals("<IDENTIFIER>")){
            code = code + split_tokens[i];          // If earlier token was also an identifier, do not add a space.
          } else {
            // System.out.println("Another error not yet handled");
          }
        } else {
          // System.out.println("Error which is not yet handled");
        }
      }

      prev_token = curr_token;*/

      // End of code by Vedant





   		// if(split_tokens[i].equals("number")){
   		// 	code = code + split_tokens[i+1] + " ";
   		// 	i++;
   		// 	continue;
   		// }

   		// //// System.out.println(next_processed);
   		// if(next_processed.size() != 0){
   		// 	index = Predict.hammingDistance(next_processed,split_tokens[i]);
   		// 	toAdd = next_tokens.get(index);
     //    //curr_token = next_tokens.get(index);
   		// }
   		// else {
     //    toAdd = split_tokens[i];
     //    //curr_token = "identifier";
     //  }
   		// code = code + toAdd + " ";

   	}
   	//// System.out.println(code);
   	next_tokens = Predict.getNextToken(code);

    //// System.out.println(code);
   	 while(next_tokens != null && next_tokens.size() == 1 && !(next_tokens.get(0).equals("<IDENTIFIER>"))){
   	 	code = code + next_tokens.get(0) + " ";
   	 	Predict.lastFour(next_tokens.get(0));
   	 	next_tokens = Predict.getNextToken(code);   	 	
   	 }
   	// System.out.println(code);
   	/*System.out.println("Class: "+class_names);
    System.out.println("Methods: "+method_names);
    System.out.println("ID: "+id_names);*/
   	return code;
   }

   // End of code written by Srinidhi


   // Generates the list of next tokens by generating a parse exception.
   public static ArrayList<String> getNextToken(String code) {
   	String[] result;
   	ArrayList<String> result2 = new ArrayList<String>();
      try {
        //ReInit();
         //String s = "class Factorial{    public static void main(String[] a){       // System.out.println(new Fac().ComputeFac((10+0)));    }}class Fac ";
         InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
         Node root = new MiniJavaParser(stream).Goal();
      }
      catch (ParseException e) {
        result = e.toString().split(":");
        // System.out.println(code);
        result = result[2].split("...\n");
      	for(String a: result){
          String temp = a.replaceAll("\\s+","").replaceAll("\n","").replaceAll("\"","");
          if(!result2.contains(temp))
		          result2.add(temp);
	      }
	      result2.remove(result2.size() - 1);
        return result2;
      }
      return null;
   }

	public static int hammingDistance(ArrayList<String> predicted, String original){
		String result = null;
		ArrayList<Integer> score = new ArrayList<Integer>();
		//int min_ind = -1;
		int min_val = hamThreshold;
		int ret_val = -1;
		for(String a: predicted){
			int shorter = Math.min(a.length(), original.length());
    	int longer = Math.max(a.length(), original.length());
    	int val = 0;
    	for (int i = 0; i < shorter; i++){
    		if(a.charAt(i) != original.charAt(i))	val++;
    	}
    	//val += longer-shorter;
    	val = Predict.LevenshteinDistance(a,original);
    	score.add(val);
    	if(val < min_val){
    		ret_val = predicted.indexOf(a);
				min_val = val;
			}
		}
		//// System.out.println(score);
		return ret_val;
	}
	public static int LevenshteinDistance(String x, String y){
		  int cost;

		  /* base case: empty strings */
		  if (x.length() == 0) return y.length();
		  if (y.length() == 0) return x.length();

		  /* test if last characters of the strings match */
		  if (x.charAt(x.length()-1) == y.charAt(y.length()-1))
		      cost = 0;
		  else
		      cost = 1;

		  /* return minimum of delete char from s, delete char from t, and delete char from both */
		  return Math.min(LevenshteinDistance(x.substring(0, x.length() - 1), y) + 1,
				 Math.min(LevenshteinDistance(x, y.substring(0, y.length() - 1)) + 1,
				 LevenshteinDistance(x.substring(0, x.length() - 1), y.substring(0, y.length() - 1)) + cost));
	}
  public static void assignCode(String miniJavaCode){
  	Predict.code = miniJavaCode;
  }
  public static String getWindow(String curr){
    String ret = "";
    // System.out.println(code);
    ret = Predict.generateProcessed(curr);
    return ret;
  }

}
