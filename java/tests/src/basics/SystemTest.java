package basics;

public class SystemTest
{
  public static void main(String[] args)
  {
    System.out.println(System.getProperty("os.arch"));
    for (Object propertyKeyName:System.getProperties().keySet()){
      System.out.println(propertyKeyName+" - "+System.getProperty(propertyKeyName.toString()));
    }
  }
}
