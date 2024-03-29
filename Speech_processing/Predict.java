// package Speech_processing;
/*
class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac((10+0)));
    }
}

class Fac {
    public int ComputeFac(int num){
        int num_aux ;
        num_aux = 0;
        if (num <= 0)
            num_aux = (1+0) ;
        if(!(num <=0))
            num_aux = num * (this.ComputeFac(num-1)) ;
        return num_aux ;
    }
}
*/

import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;
//import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

class Main{
    public static void main(String[] args){
		Predict.getWindow("class title factorial",Predict.code);
		Predict.getWindow("camel main args",Predict.code);
		Predict.getWindow("new title fac",Predict.code);
		Predict.getWindow("dot camel compute fac",Predict.code);
    Predict.getWindow("open number 10 plus number 0",Predict.code);
		Predict.getWindow("close",Predict.code);

		Predict.getWindow("class title fac",Predict.code);
		Predict.getWindow("open public integer camel compute fac",Predict.code);
    Predict.getWindow("declare integer num",Predict.code);
    Predict.getWindow("close ",Predict.code);

    Predict.getWindow("declare integer under num aux",Predict.code);
    Predict.getWindow("assign under num aux",Predict.code);
    Predict.getWindow("number 0 semicolon",Predict.code);
    Predict.getWindow("if open num",Predict.code);
    Predict.getWindow("less number 0 close ",Predict.code);

    Predict.getWindow("assign under num aux",Predict.code);
    Predict.getWindow("open number 1 plus number 0 close semicolon",Predict.code);
    Predict.getWindow("if open not open num",Predict.code);
    Predict.getWindow("less number 0 close close",Predict.code);
		Predict.getWindow("assign under num aux",Predict.code);
		Predict.getWindow("num",Predict.code);
     Predict.getWindow("times open this dot camel compute fac",Predict.code);
        Predict.getWindow("num",Predict.code);
            Predict.getWindow("minus number 1",Predict.code);
        Predict.getWindow("close close",Predict.code);

      Predict.getWindow("return under num aux",Predict.code);
      Predict.getWindow("semicolon",Predict.code);

		Predict.getWindow("close ",Predict.code);
  //   Predict.getWindow("assign hello",Predict.code);
  //   Predict.getWindow("assign hello",Predict.code);
  //   Predict.getWindow("\n",Predict.code);
		// Predict.getWindow("\n",Predict.code);
		// System.out.println(Predict.code);
		// System.out.println(Predict.getNextToken(Predict.code));
    }
}


// The class used for processing and predicting.
public class Predict{

   public static final int disThreshold = 2; // Edit distance threshold - might need to be changed later.
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
      "System.out.println","void",
      "#define"
    ) );
    public static ArrayList<String> myJavaTokens = new ArrayList<String>(
     Arrays.asList(
       "open", "close",
       "open", "close",
       "open", "close",
       "semicolon", "dot",
       "equal", "less",
       "different", "plus",
       "minus","times",
       "by","and",
       "or","not",
       "boolean","class",
       "interface","else",
       "extend","false",
       "if","while",
       "integer","length",
       "main","new",
       "public","return",
       "static","string",
       "this","true",
       "print","void",
       "define"
     ) );

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
    public static String checkDistance(String token){
      if(prev.get(3).equals("declare") || prev.get(3).equals("new") || prev.get(3).equals("extends")){
        int disCheck = Predict.editDistance(class_names, token);
        if(disCheck != -1){
          return class_names.get(disCheck);
        }
        else{
          return token;
        }
      }
      else if(prev.get(3).equals(".")){
        int disCheck = Predict.editDistance(method_names, token);
        if(disCheck != -1){
          return method_names.get(disCheck);
        }
        else{
          return token;
        }
      }
      else{
        int disCheck = Predict.editDistance(id_names, token);
        if(disCheck != -1){
          return id_names.get(disCheck);
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
        return checkDistance(token);
      }
    }

   public static String generateProcessed(String s){
   	String[] split_tokens;
   	ArrayList<String> next_tokens;
   	ArrayList<String> next_processed;
   	split_tokens = s.split(" ");
    String prev_token = null;
    String curr_token = null;

   	String toAdd;
   	int index;
   	int dot_flag = 0;
    int var_flag = 0,assign_flag = 0;

   	// Handling enter
   	if(split_tokens.length == 1 && split_tokens[0] == "\n"){
   		next_tokens = Predict.getNextToken(code);
   		if(next_tokens.contains(";")){
   			code += ";";
   		}
   	}
   		// End of handling enter
   	else{

	   	for(int i=0;i<split_tokens.length;i++){
	   		next_tokens = Predict.getNextToken(code);
	   		next_processed = new ArrayList<String>();
		  if(next_tokens == null){
		    code+="\n";
		    next_tokens = new ArrayList<String>();
		    next_tokens.add("class");
		    // continue;
		  }
      // System.out.println(next_tokens);
		  for(int j=0;j<next_tokens.size();j++){
		    index = miniJavaTokens.indexOf(next_tokens.get(j));
		    if(index != -1)
		      next_processed.add(myJavaTokens.get(index));
		    else next_processed.add("will be handled");

		  }
      // System.out.println(split_tokens[i]);
      // System.out.println(next_processed);
      // System.out.println("\n\n\n");
		  int disCheck = Predict.editDistance(next_processed,split_tokens[i]);
		  if(disCheck != -1){
		    curr_token = next_tokens.get(disCheck);
		    if(miniJavaTokens.indexOf(curr_token) == 7) {
		    	dot_flag = 1;
		    }
		    Predict.lastFour(curr_token);
		    code += " " + curr_token;
		  }
		  else if(split_tokens[i].equals("number")){
		  	i++;
		  	curr_token = split_tokens[i];
		  	Predict.lastFour(curr_token);
		  	code += " " + curr_token;
		  }
      else if(split_tokens[i].equals("assign")){
        assign_flag = 1;
      }

		  else{
		  	if(split_tokens[i].equals("declare")){
		  		Predict.lastFour("declare");
		  	}
		  	else{
		  		toAdd = "";
		  		disCheck = Predict.editDistance(idFonts,split_tokens[i]);
		  		if(disCheck == -1){
		  			for(int j=i;j<split_tokens.length;j++){
			  			toAdd += split_tokens[j];
			  		}
		  		}
		  		else if(disCheck == 0){
		  			char first;
		  			toAdd = toAdd + split_tokens[i+1];
		  			for(int j=i+2;j<split_tokens.length;j++){
		  				first = split_tokens[j].charAt(0);
		  				first = Character.toUpperCase(first);
		  				toAdd = new StringBuilder(toAdd).append(first).append(split_tokens[j].substring(1)).toString();
		  			}

		  		}
		  		else if(disCheck == 1){
		  			char first;
		  			first = split_tokens[i+1].charAt(0);
		  			first = Character.toUpperCase(first);
		  			toAdd = new StringBuilder(toAdd).append(first).append(split_tokens[i+1].substring(1)).toString();
		  			for(int j=i+2;j<split_tokens.length;j++){
			  			toAdd += split_tokens[j];
			  		}
		  		}
		  		else if(disCheck == 2){
		  			toAdd += split_tokens[i+1];
		  			for(int j=i+2;j<split_tokens.length;j++){
			  			toAdd += "_" + split_tokens[j];
			  		}
		  		}
          var_flag = 1;
		  		toAdd = Predict.processId(toAdd);
			  	Predict.lastFour(toAdd);
			  	code += " " + toAdd;
			  	break;

		  	}
		  }

	   	}
   	}
   	for(int i=0;i<split_tokens.length;i++){
   		if(dot_flag == 1 && var_flag == 1){
   			code += "(";
   			break;
   		}
      else if(assign_flag == 1)
      {  code += " = ";
          break;
        }
   	}
   	next_tokens = Predict.getNextToken(code);

   	 while(next_tokens != null && next_tokens.size() == 1 && !(next_tokens.get(0).equals("<IDENTIFIER>"))){
   	 	code = code + next_tokens.get(0) + " ";
   	 	Predict.lastFour(next_tokens.get(0));
   	 	next_tokens = Predict.getNextToken(code);
   	 }
   	return code;
   }


   // Generates the list of next tokens by generating a parse exception.
   public static ArrayList<String> getNextToken(String code) {
   	String[] result;
   	ArrayList<String> result2 = new ArrayList<String>();
      try {
         InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
         Node root = new MiniJavaParser(stream).Goal();
      }
      catch (ParseException e) {
        result = e.toString().split(":");
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

	public static int editDistance(ArrayList<String> predicted, String original){
		String result = null;
		ArrayList<Integer> score = new ArrayList<Integer>();
		//int min_ind = -1;
		int min_val = disThreshold;
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
  public static String getWindow(String curr, String hist){
    String ret = "";
    Predict.code = hist;
    ret = Predict.generateProcessed(curr);
    // System.out.println(ret);
    return ret;
  }

}
