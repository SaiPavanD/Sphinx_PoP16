public class Main{
  public static void main(String[] args) {
        System.out.println("Hello, World");
        int  i=(new Main()).call_func();
        System.out.println("i = "+i);
    }

    public int call_func(){
      System.out.println("In call_func");
      return 1;
    }
}
