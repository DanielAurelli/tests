package basics;

public class Texts
{

public static void main(String[] args)
{
    String tar16=" 2345";
    tar16=new StringBuffer(String.format("%-16s", tar16)).replace(6, 6+6, "XXXXXX").toString();
    //System.out.println(tar16);
    //tar16=new StringBuffer(tar16).replace(6, 6+6, "XXXXXX").toString();
    System.out.println(tar16);
    System.out.println("'\\\\x00'");
}

}
