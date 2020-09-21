/* =================================================================*
 *             Copyright (C), Advansol Technologies                 *
 *------------------------------------------------------------------*
 *                                                                  *
 * Function: Implementation of text compare                         *
 *           return side by side contents in JSON format            *
 *           with html tags                                         *
 *           Dependence: Apache commons-text-1.8.jar                *
 * Author: Daniel                                  Date: 07/07/2020 *
 *                                                                  *
 *==================================================================*
 */
package basics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.json.Json;

import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.StringsComparator;

import ads.repository.Comparator;

public class CompareTexts implements Comparator, CommandVisitor<Character>
{
  // Spans with red & green highlights to put highlighted characters in HTML
  private static final String DELETION = "<span class=\"del\">%s</span>";
  private static final String INSERTION = "<span class=\"ins\">%s</span>";

  private StringBuffer left = new StringBuffer();
  private StringBuffer right = new StringBuffer();
  private StringBuffer del = new StringBuffer();
  private StringBuffer ins = new StringBuffer();
  private String left_name = "";
  private String right_name = "";

  //
  // Comparator methods  
  //
  @Override
  public String compare(InputStream inLeft, InputStream inRight)
  {
    return compare(inLeft, inRight, "left", "right");
  }

  @Override
  public String compare(InputStream inLeft, InputStream inRight, String lTag, String rTag)
  {
    this.left_name=lTag;
    this.right_name=rTag;
    int lpos=0;
    int rpos=0;
    ArrayList<String> lbuffer = new ArrayList<String>();
    ArrayList<String> rbuffer = new ArrayList<String>();
    Scanner scLeft = new Scanner(inLeft);
    Scanner scRight = new Scanner(inRight);
    while (scLeft.hasNext() || scRight.hasNext()) {
      lbuffer.add ((scLeft.hasNext() ? scLeft.nextLine() : "") );
      rbuffer.add ((scRight.hasNext() ? scRight.nextLine() : "") );
    }
    scLeft.close();
    scRight.close();
    main:
    for (lpos=0; lpos<lbuffer.size();lpos++) 
    {
      String lineleft  = normalize(lbuffer.get(lpos));
      String lineright = (rpos<rbuffer.size()? normalize(rbuffer.get(rpos)):"\n");

      StringsComparator comparator = new StringsComparator(lineleft, lineright);
      if (comparator.getScript().getLCSLength() > (comparator.getScript().getModifications())
          && (comparator.getScript().getModifications()<= 1 + (lineleft.length()*0.4))) 
      {
        // almost equal lines
        left.append(String.format("%04d ", lpos));
        right.append(String.format("%04d ", rpos));
        comparator.getScript().visit(this);
        rpos++;
        continue main;
      }
      // not equal, check next three left lines
      int l2=0;
      for (l2 = lpos+1; l2<lbuffer.size() && l2<lpos+3; l2++)
      {
        comparator = new StringsComparator(normalize(lbuffer.get(l2)), lineright);
        if (comparator.getScript().getModifications()==0) 
        {
          while (lpos<l2)
          {
            lineleft = normalize(lbuffer.get(lpos++));
            comparator = new StringsComparator(lineleft, "\n");
            left.append(String.format("%04d ", lpos));
            comparator.getScript().visit(this);
          }
          comparator = new StringsComparator(normalize(lbuffer.get(lpos)), lineright);
          left.append(String.format("%04d ", lpos));
          right.append(String.format("%04d ", rpos));
          comparator.getScript().visit(this);
          rpos++;
          continue main;
        }
      }
      // different lines
      int r2=0;
      for (r2=rpos;r2<rbuffer.size();r2++)
      {
        // search equal lines in right side
        comparator = new StringsComparator(lineleft, normalize(rbuffer.get(r2)));
        if (comparator.getScript().getModifications()==0) 
        {
          while (rpos<r2)
          {
            lineright = normalize(rbuffer.get(rpos++));
            StringsComparator rightComparator = new StringsComparator("\n", lineright);
            right.append(String.format("%04d ", rpos));
            rightComparator.getScript().visit(this);
          }
          comparator = new StringsComparator(lineleft, normalize(rbuffer.get(rpos++)));
          left.append(String.format("%04d ", lpos));
          right.append(String.format("%04d ", rpos));
          comparator.getScript().visit(this);
          break;
        }
      }
      if (r2==rbuffer.size()) 
      {
        for (r2=rpos;r2<rbuffer.size();r2++)
        {
          // search lines with up to 2 chars different in the right side
          comparator = new StringsComparator(lineleft, normalize(rbuffer.get(r2)));
          if (comparator.getScript().getModifications()<=2) 
          {
            while (rpos<r2)
            {
              lineright = normalize(rbuffer.get(rpos++));
              StringsComparator rightComparator = new StringsComparator("\n", lineright);
              right.append(String.format("%04d ", rpos));
              rightComparator.getScript().visit(this);
            }
            comparator = new StringsComparator(lineleft, normalize(rbuffer.get(rpos++)));
            left.append(String.format("%04d ", lpos));
            right.append(String.format("%04d ", rpos));
            comparator.getScript().visit(this);
            break;
          }
        }
      }
      if (r2==rbuffer.size()) 
      {
        // left line not found in right side
        StringsComparator leftComparator = new StringsComparator(lineleft, "\n");
        left.append(String.format("%04d ", lpos));
        leftComparator.getScript().visit(this);
      }
    }
    return getResult();
  }
  
  private static String normalize(String line)
  {
    line=line.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    return line + "\n";
  }

  private String getResult()
  {
    checkDifferences();
    return Json.createObjectBuilder()
        .add("subject", "diff")
        .add("left_name", left_name)
        .add("left", left.toString())
        .add("right_name", right_name)
        .add("right", right.toString() )
        .build().toString();
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
  
  //
  // CommandVisitor methods  
  //
  @Override
  public void visitDeleteCommand(Character c)
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    del.append(toAppend);
  }

  @Override
  public void visitInsertCommand(Character c)
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    ins.append(toAppend);
  }

  @Override
  public void visitKeepCommand(Character c)
  {
    checkDifferences();
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    left.append(toAppend);
    right.append(toAppend);
  }
}
