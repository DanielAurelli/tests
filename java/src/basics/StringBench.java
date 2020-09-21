package basics;

public class StringBench
{

  public static void main(String[] args)
  {
    StringBuffer buf = null;
    StringBuilder building=null;
    String withString ="";
    long t0 = 0;

    t0 = System.currentTimeMillis();
    withString ="";
    for (int i = 0 ; i < 100000; i++){
        withString+="s";
    }
    System.out.println("small + strings:" + (System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    buf = new StringBuffer();
    for (int i = 0 ; i < 100000; i++){
        buf.append("s");
    }
    System.out.println("small Buffers   : "+(System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    building = new StringBuilder();
    for (int i = 0 ; i < 100000; i++){
        building.append("s");
    }
    System.out.println("small Builder : "+(System.currentTimeMillis() - t0));

    if (withString.equals(buf.toString()) && buf.toString().equals(building.toString()))
      System.out.println("all equals");
    
    t0 = System.currentTimeMillis();
    withString ="";
    for (int i = 0 ; i < 100000; i++){
        withString+="some string";
    }
    System.out.println("+ strings :" + (System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    buf = new StringBuffer();
    for (int i = 0 ; i < 100000; i++){
        buf.append("some string");
    }
    System.out.println("Buffers   : "+(System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    building = new StringBuilder();
    for (int i = 0 ; i < 100000; i++){
        building.append("some string");
    }
    System.out.println("Builder   : "+(System.currentTimeMillis() - t0));
    
    if (withString.equals(buf.toString()) && buf.toString().equals(building.toString()))
      System.out.println("all equals");
    
    t0 = System.currentTimeMillis();
    withString ="";
    for (int i = 0 ; i < 100000; i++){
        withString+="";
    }
    System.out.println("empty strings :" + (System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    buf = new StringBuffer();
    for (int i = 0 ; i < 100000; i++){
        buf.append("");
    }
    System.out.println("empty Buffers : "+(System.currentTimeMillis() - t0));

    t0 = System.currentTimeMillis();
    building = new StringBuilder();
    for (int i = 0 ; i < 100000; i++){
        building.append("");
    }
    System.out.println("empty Builder : "+(System.currentTimeMillis() - t0));

    if (withString.equals(buf.toString()) && buf.toString().equals(building.toString()))
      System.out.println("all equals");
    
  }

}
