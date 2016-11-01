import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;
//import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

class Main{
    public static void main(String[] args){
	String s = "class Factorial{    public static void main(String[] a){       System.out.println();    }}class Fac "; // s should hold the entire code that was generated till now.
	String test1 = "sistng"; // This is the token/word received from speech recognition.
	ArrayList<String> test = new ArrayList<String>();
	test.add("system");
	test.add("println");
	test.add("hello");
	test.add("string");
	ArrayList<String> result  = Predict.getNextToken(s);
	System.out.println(result);
	System.out.println(Predict.hammingDistance(test, test1));
    }
}

class Predict{
   
   public static final int hamThreshold = 2;
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
	public static String hammingDistance(ArrayList<String> predicted, String original){
		String result = null;
		ArrayList<Integer> score = new ArrayList<Integer>();
		//int min_ind = -1;
		int min_val = 100;
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
    				result = a;
				min_val = val;
			}
		}
		System.out.println(score);
		return result;
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

