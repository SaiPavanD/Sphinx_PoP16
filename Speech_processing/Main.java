import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;
//import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

class Main{
    public static void main(String[] args){
	String s = "class Factorial{    public static void main(String[] a){      "; // s should hold the entire code that was generated till now.
	String test1 = "sistng"; // This is the token/word received from speech recognition.
	String a = "class name open public static void main open string open close args close open print open open number 1 plus number 10 close close";
	Predict.generateProcessed(a);
    }
}


// The class used for processing and predicting.
class Predict{
   
   public static final int hamThreshold = 2; // Hamming distance threshold - might need to be changed later.
   public static String code = "";
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
      "System.out.println","void",
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
       "booleaan","class",
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
   // Code written by Srinidhi 
   public static String generateProcessed(String s){
   	String[] split_tokens;
   	ArrayList<String> next_tokens;
   	ArrayList<String> next_processed;
   	split_tokens = s.split(" ");
   	String toAdd;
   	int index;
   	for(int i=0;i<split_tokens.length;i++){
   		next_tokens = Predict.getNextToken(code);
   		next_processed = new ArrayList<String>();
   		if(split_tokens[i].equals("number")){
   			code = code + split_tokens[i+1] + " ";
   			i++;
   			continue;
   		}
   		for(int j=0;j<next_tokens.size();j++){
   			index = miniJavaTokens.indexOf(next_tokens.get(j));
   			//System.out.println(index);
   			if(index != -1)next_processed.add(myJavaTokens.get(index));
   		}
   		//System.out.println(next_processed);
   		if(next_processed.size() != 0){
   			index = Predict.hammingDistance(next_processed,split_tokens[i]);
   			toAdd = next_tokens.get(index);
   		}
   		else toAdd = split_tokens[i];   		
   		code = code + toAdd + " ";
   		
   	}
   	//System.out.println(code);
   	next_tokens = Predict.getNextToken(code);
   	//System.out.println(next_tokens);
   	while(next_tokens != null && next_tokens.size() == 1 && !(next_tokens.get(0).equals("<IDENTIFIER>"))){
   	//System.out.println(next_tokens);
   		code = code + next_tokens.get(0) + " ";
   		next_tokens = Predict.getNextToken(code);
   		//System.out.println(next_tokens);
   		
   	}
   	System.out.println(code);
   	return null;
   }
   
   // End of code written by Srinidhi
   
   
   // Generates the list of next tokens by generating a parse exception.
   public static ArrayList<String> getNextToken(String code) {
   	String[] result;
   	ArrayList<String> result2 = new ArrayList<String>();
      try {
         //String s = "class Factorial{    public static void main(String[] a){       System.out.println(new Fac().ComputeFac((10+0)));    }}class Fac ";
         InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
         Node root = new MiniJavaParser(stream).Goal();
      }
      catch (ParseException e) {
         result = e.toString().split(":");
         result = result[2].split("...\n");
      	for(String a: result){
		
		result2.add(a.replaceAll("\\s+","").replaceAll("\n","").replaceAll("\"",""));
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
		int min_val = 100;
		int ret_val = 0;
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
		//System.out.println(score);
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


} 

