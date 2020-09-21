package basics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.FileUtils;

import ads.repository.Comparator;
import editor.CompareTexts;

public class TestDiff
{

  public static void main(String[] args)
  {
    try
    {
      InputStream leftIn = new FileInputStream(new File("C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM.NSP"));
      InputStream rightIn = new FileInputStream(new File("C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM2.NSP"));
      Comparator diff = new CompareTexts();
      String res = diff.compare(leftIn, rightIn);
      JsonObject jo = Json.createReader(new StringReader(res)).readObject();
      String left= jo.getString("left");
      String right= jo.getString("right");
      generateHTML(left, right);      
    } catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  public static void generateHTML(String left, String right) throws IOException 
  {
    String template = FileUtils.readFileToString(new File("C:\\Work\\diff.tpl"), "utf-8");
    String out1 = template.replace("${left}", left);
    String output = out1.replace("${right}", right);
    // Write file to disk.
    FileUtils.writeStringToFile(new File("C:\\Work\\diff.html"), output, "utf-8");
    System.out.println("HTML diff generated.");
  }


}
