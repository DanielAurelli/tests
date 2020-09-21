/* =================================================================*
 *             Copyright (C), Advansol Technologies                 *
 *------------------------------------------------------------------*
 *                                                                  *
 * Function: Implementation of text compare                         *
 *           return side by side contents in JSON format            *
 *           with html tags                                         *
 *           Using own longest match sequence algorithm             *
 * Author: Daniel                                  Date: 09/07/2020 *
 *                                                                  *
 *==================================================================*
 */
package basics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import ads.repository.LongestSequence;
import ads.repository.LongestSequence.MatchInfo;

public class MyCompareTexts {

  // Spans with red & green highlights to put highlighted characters in HTML
  private static final String DELETION = "<span class=\"del\">%s</span>";
  private static final String INSERTION = "<span class=\"ins\">%s</span>";

  private StringBuffer left = new StringBuffer();
  private StringBuffer right = new StringBuffer();
  private StringBuffer del = new StringBuffer();
  private StringBuffer ins = new StringBuffer();
  private String left_name = "";
  private String right_name = "";



  public static void main(String[] args) throws IOException 
  {
    // Read both files with line iterator.
    String lTag = "C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM.NSP";
    String rTag = "C:\\Work\\Consist\\application\\SAMPLES\\SRC\\ADSPSUM2.NSP";
    FileInputStream inLeft = new FileInputStream(lTag);
    FileInputStream inRight = new FileInputStream(rTag);
    MyCompareTexts diff= new MyCompareTexts();
    diff.compare(inLeft, inRight, lTag, rTag);
  }

  public String compare(InputStream inLeft, InputStream inRight, String lTag, String rTag)
  {
    this.left_name=lTag;
    this.right_name=rTag;
    String lineleft="";
    String lineright="";
    int lpos=0;
    int rpos=0;
    ArrayList<String> lbuffer = new ArrayList<String>();
    ArrayList<String> rbuffer = new ArrayList<String>();
    ArrayList<MatchInfo> matches = null;
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
        lineleft  = normalize(lbuffer.get(lpos));
        lineright = (rpos<rbuffer.size()? normalize(rbuffer.get(rpos)):"\n");

        matches = LongestSequence.getMatches(lineleft, lineright);
        if (isSimilar(matches, lineleft.length()))
        {
          left.append(String.format("%04d ", lpos+1));
          right.append(String.format("%04d ", rpos+1));
          buildSequences(lineleft, lineright, matches);
          rpos++;
          continue main;
        }
        // not equal, check next three left lines
        int l2=0;
        for (l2 = lpos+1; l2<lbuffer.size() && (l2-(lpos+1)<=3); l2++)
        {
          if (lineright.equals(normalize(lbuffer.get(l2)))) 
          {
            while (lpos<l2)
            {
              lineleft = normalize(lbuffer.get(lpos));
              matches = LongestSequence.getMatches(lineleft, "\n");
              left.append(String.format("%04d ", lpos+1));
              buildSequences(lineleft, "\n", matches);
              lpos++;
            }
            lineleft = normalize(lbuffer.get(lpos));
            matches = LongestSequence.getMatches(lineleft, lineright);
            left.append(String.format("%04d ", lpos+1));
            right.append(String.format("%04d ", rpos+1));
            buildSequences(lineleft, lineright, matches);
            rpos++;
            continue main;
          }
        }
        // search equal on right side
        int r2=0;
        for (r2=rpos;r2<rbuffer.size()&&(r2-rpos)<=300;r2++)
        {
          if (lineleft.equals(normalize(rbuffer.get(r2)))) 
          {
            while (rpos<r2)
            {
              lineright = normalize(rbuffer.get(rpos));
              matches = LongestSequence.getMatches("\n", lineright);
              right.append(String.format("%04d ", rpos+1));
              buildSequences("\n", lineright, matches);
              rpos++;
            }
            lineright = normalize(rbuffer.get(rpos));
            matches = LongestSequence.getMatches(lineleft, lineright);
            left.append(String.format("%04d ", lpos+1));
            right.append(String.format("%04d ", rpos+1));
            buildSequences(lineleft, lineright, matches);
            rpos++;
            continue main;
          }
        }
        // search similar on right side 
        for (r2=rpos;r2<rbuffer.size()&&(r2-rpos)<=300;r2++)
        {
          matches = LongestSequence.getMatches(lineleft, normalize(rbuffer.get(r2)));
          if (isSimilar(matches, lineleft.length()))
          {
            while (rpos<r2)
            {
              lineright = normalize(rbuffer.get(rpos));
              matches = LongestSequence.getMatches("\n", lineright);
              right.append(String.format("%04d ", rpos+1));
              buildSequences("\n", lineright, matches);
              rpos++;
            }
            lineright = normalize(rbuffer.get(rpos));
            matches = LongestSequence.getMatches(lineleft, lineright);
            left.append(String.format("%04d ", lpos+1));
            right.append(String.format("%04d ", rpos+1));
            buildSequences(lineleft, lineright, matches);
            rpos++;
            continue main;
          }
        }
        // left line not found in right side
        lineleft= normalize(lbuffer.get(lpos));
        matches = LongestSequence.getMatches(lineleft, "\n");
        left.append(String.format("%04d ", lpos+1));
        buildSequences(lineleft, "\n", matches);
      }
    // finally complete right side
    while (rpos < rbuffer.size())
    {
      lineright = normalize(rbuffer.get(rpos));
      matches = LongestSequence.getMatches("\n", lineright);
      right.append(String.format("%04d ", rpos+1));
      buildSequences("\n", lineright, matches);
      rpos++;
    }
    return generateHTML();
  }

  private static String normalize(String line)
  {
    line=line.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    return line + "\n";
  }

  private boolean isSimilar(ArrayList<MatchInfo> matches, int len)
  {
    int mlen=0;
    for (MatchInfo match : matches)
    {
      mlen += match.maxlen;
    }
    return isEqual(matches, len) || (matches.size() < 8 
        && (mlen >= 1 + (len*0.6))); 
  }

  private static boolean isEqual(ArrayList<MatchInfo> matches, int length)
  {
    return (matches.size()==1 
        && matches.get(0).maxp1==0 
        && matches.get(0).maxp2==0
        && matches.get(0).maxlen==length);
  }

  private void buildSequences(String left, String right, ArrayList<MatchInfo> matches)
  {
    int lpos=0;
    int rpos=0;
    for (MatchInfo match : matches)
    {
      while (lpos<match.maxp1 && lpos<left.length())
      {
        visitDeleteCommand(left.charAt(lpos++));
      }
      while (rpos<match.maxp2 && rpos<right.length())
      {
        visitInsertCommand(right.charAt(rpos++));
      }
      checkDifferences();
      for (int i = 0 ; i < match.maxlen && lpos < left.length(); i++)
      {
        visitKeepCommand(left.charAt(lpos++));
        rpos++;
      }
    }
    while (lpos<left.length())
    {
      visitDeleteCommand(left.charAt(lpos++));
    }
    while (rpos<right.length())
    {
      visitInsertCommand(right.charAt(rpos++));
    }
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

  public void visitDeleteCommand(Character c) 
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    del.append(toAppend);
  }

  public void visitInsertCommand(Character c) 
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    ins.append(toAppend);
  }

  public void visitKeepCommand(Character c) 
  {
    String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
    left.append(toAppend);
    right.append(toAppend);
  }

  public String generateHTML() 
  {
    String template;
    try
    {
      template = FileUtils.readFileToString(new File("C:\\Work\\diff.tpl"), "utf-8");
      String out1 = template.replace("${left}", "<p>"+left_name+"</p><br>" + left);
      String output = out1.replace("${right}", "<p>"+right_name+"</p><br>" + right);
      // Write file to disk.
      FileUtils.writeStringToFile(new File("C:\\Work\\diff.html"), output, "utf-8");
      System.out.println("HTML diff generated. Open C:\\Work\\diff.html");
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "";
  }
}