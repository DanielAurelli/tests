package icu4j.test;
import com.ibm.icu.text.*;
public class Arabic
{

  public static void main(String[] args)
  {
    String text ="صصص 1234"; // "ششش ";// "ىىى 1234";
    try
    {
      ArabicShaping ar2 = new ArabicShaping(
           ArabicShaping.LETTERS_SHAPE |
           ArabicShaping.TASHKEEL_REPLACE_BY_TATWEEL |
           ArabicShaping.YEHHAMZA_TWOCELL_NEAR |
           ArabicShaping.SEEN_TWOCELL_NEAR |
           ArabicShaping.DIGITS_EN2AN
          );
      String text2 =ar2.shape(text);
       
          ArabicShaping ar3 = new ArabicShaping(
           ArabicShaping.LETTERS_UNSHAPE |
           ArabicShaping.DIGITS_EN2AN |
           ArabicShaping.LAMALEF_AUTO|
           ArabicShaping.SEEN_TWOCELL_NEAR
          );
          String text3 =ar3.shape(text);
      
      ArabicShaping ar4= new ArabicShaping(
          ArabicShaping.LETTERS_SHAPE_TASHKEEL_ISOLATED |
          ArabicShaping.DIGITS_AN2EN |
          ArabicShaping.TEXT_DIRECTION_VISUAL_LTR
         );
      String text4 =ar4.shape(text);
//        Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
      System.out.println("typed text");
      System.out.println(text);
      for (int i = 0; i < text.getBytes().length; i++)
        System.out.print(String.format("%02x", text.getBytes()[i]));
      System.out.println(" ");
      System.out.println(ar2);
      System.out.println(text2);
      for (int i = 0; i < text2.getBytes().length; i++)
        System.out.print(String.format("%02x", text2.getBytes()[i]));
      System.out.println(" ");
      System.out.println(ar3);
      System.out.println(text3);
      for (int i = 0; i < text3.getBytes().length; i++)
        System.out.print(String.format("%02x", text3.getBytes()[i]));
      System.out.println(" ");
      System.out.println(ar4);
      System.out.println(text4);
      for (int i = 0; i < text4.getBytes().length; i++)
        System.out.print(String.format("%02x", text4.getBytes()[i]));
    } catch (ArabicShapingException e)
    {
      System.out.println(e.toString());
    }
  }

}
