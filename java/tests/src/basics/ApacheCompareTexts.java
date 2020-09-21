package basics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;

public class ApacheCompareTexts {

  public static void main(String[] args) throws IOException {
    // Read both files with line iterator.
    LineIterator file1 = FileUtils.lineIterator(new File("C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM.NSP"), "utf-8");
    LineIterator file2 = FileUtils.lineIterator(new File("C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM2.NSP"), "utf-8");

    // Initialize visitor.
    FileCommandsVisitor fileCommandsVisitor = new FileCommandsVisitor();

    int lpos=0;
    int rpos=0;
    ArrayList<String> lbuffer = new ArrayList<String>();
    ArrayList<String> rbuffer = new ArrayList<String>();
    while (file1.hasNext() || file2.hasNext()) {
      lbuffer.add ((file1.hasNext() ? file1.nextLine() : "") );
      rbuffer.add ((file2.hasNext() ? file2.nextLine() : "") );
    }
    EditScript<Character> script = null;
    main:
    for (lpos=0; lpos<lbuffer.size();lpos++) 
    {
      System.out.println(lpos + "/"+rpos);
      String left  = normalize(lbuffer.get(lpos));
      String right = normalize(rbuffer.get(rpos));

      StringsComparator comparator = new StringsComparator(left, right);
      script = comparator.getScript();
      
      if (script.getLCSLength() > (script.getModifications())
          && (script.getModifications()<= 1 + (left.length()*0.4))) 
      {
        // semelhantes
        script.visit(fileCommandsVisitor);
        rpos++;
        continue main;
      }
      // diferentes, check next three l
      int l2=0;
      for (l2 = lpos+1; l2<lbuffer.size() && l2<lpos+3; l2++)
      {
        comparator = new StringsComparator(normalize(lbuffer.get(l2)), right);
        if (comparator.getScript().getModifications()==0) 
        {
          while (lpos<l2)
          {
            left = normalize(lbuffer.get(lpos++));
            comparator = new StringsComparator(left, "\n");
            comparator.getScript().visit(fileCommandsVisitor);
          }
          comparator = new StringsComparator(normalize(lbuffer.get(lpos)), right);
          comparator.getScript().visit(fileCommandsVisitor);
          rpos++;
          continue main;
        }
      }
      // diferentes
      int r2=0;
      for (r2=rpos;r2<rbuffer.size();r2++)
      {
        // procura igual no restante do r
        comparator = new StringsComparator(left, normalize(rbuffer.get(r2)));
        if (comparator.getScript().getModifications()==0) 
        {
          while (rpos<r2)
          {
            right = normalize(rbuffer.get(rpos++));
            StringsComparator rightComparator = new StringsComparator("\n", right);
            rightComparator.getScript().visit(fileCommandsVisitor);
          }
          comparator = new StringsComparator(left, normalize(rbuffer.get(rpos++)));
          comparator.getScript().visit(fileCommandsVisitor);
          break;
        }
      }
      if (r2==rbuffer.size()) 
      {
        for (r2=rpos;r2<rbuffer.size();r2++)
        {
          // procura semelhante no restante do r
          comparator = new StringsComparator(left, normalize(rbuffer.get(r2)));
          script = comparator.getScript();
          if (script.getLCSLength() > (script.getModifications())
              && (script.getModifications()<= 1 + (left.length()*0.2))) 
          {
            while (rpos<r2)
            {
              right = normalize(rbuffer.get(rpos++));
              StringsComparator rightComparator = new StringsComparator("\n", right);
              rightComparator.getScript().visit(fileCommandsVisitor);
            }
            comparator = new StringsComparator(left, normalize(rbuffer.get(rpos++)));
            comparator.getScript().visit(fileCommandsVisitor);
            break;
          }
        }
      }
      if (r2==rbuffer.size()) 
      {
        // l not found in r
        StringsComparator leftComparator = new StringsComparator(left, "\n");
        leftComparator.getScript().visit(fileCommandsVisitor);
      }
    }
    fileCommandsVisitor.endOfcheck();
    fileCommandsVisitor.generateHTML();
  }

  private static String normalize(String line)
  {
    line=line.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    return line + "\n";
  }
}

/*
 * Custom visitor for file comparison which stores comparison & also generates
 * HTML in the end.
 */
class FileCommandsVisitor implements CommandVisitor<Character> {

  // Spans with red & green highlights to put highlighted characters in HTML
  private static final String DELETION = "<span class=\"del\">%s</span>";
  private static final String INSERTION = "<span class=\"ins\">%s</span>";

  private StringBuffer left = new StringBuffer();
  private StringBuffer right = new StringBuffer();
  private StringBuffer del = new StringBuffer();
  private StringBuffer ins = new StringBuffer();

  @Override
  public void visitKeepCommand(Character c) 
  {
    checkDifferences();
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    left.append(toAppend);
    right.append(toAppend);
    ins.setLength(0);
    del.setLength(0);
  }

  public void endOfcheck()
  {
    checkDifferences();    
  }

  private void checkDifferences()
  {
    if (del.length()>0)
      left.append(String.format(DELETION, del));
    if (ins.length()>0)
      right.append(String.format(INSERTION, ins));
    ins.setLength(0);
    del.setLength(0);
  }

  @Override
  public void visitInsertCommand(Character c) 
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    ins.append(toAppend);
  }

  @Override
  public void visitDeleteCommand(Character c) 
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    del.append(toAppend);
  }

  public void generateHTML() throws IOException 
  {
    String template = FileUtils.readFileToString(new File("C:\\Work\\diff.tpl"), "utf-8");
    String out1 = template.replace("${left}", left);
    String output = out1.replace("${right}", right);
    // Write file to disk.
    FileUtils.writeStringToFile(new File("C:\\Work\\diff.html"), output, "utf-8");
    System.out.println("HTML diff generated.");
  }
}