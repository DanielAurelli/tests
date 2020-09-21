package basics;

public class Numbers
{

public static void main(String[] args)
{
  int in=65535;
  byte b = (byte) in;
  System.out.println(in +" = " + b);
    String value = "0013418";
    int number = Integer.parseInt(value);
    value = String.valueOf(Integer.parseInt(value));
    System.out.println(number +"string:"+ value);
    number+=1;
    System.out.println(number +"string:"+ value);
    number=+1 ; // 0;
    System.out.println(number +"string:"+ value);


    String valued = "00013418.99";
    double numberd = Double.parseDouble(valued);
    valued = String.valueOf(Double.parseDouble(valued));
    System.out.println(numberd +" string:"+ valued);

}

}
