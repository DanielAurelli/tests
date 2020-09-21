package basics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

public class MyEncoder {

    public MyEncoder() {
    }

    public static void main(String[] args) {
        String s =  "ﻟﻌرض​ اﻟﺟدول";
        String s1 = "ﻟ ﻌ ر ض​ ا ﻟ ﺟ د و ل";
        try {
            System.out.println(s);
            ByteArrayInputStream bais=new ByteArrayInputStream(s.getBytes("UTF-8"));
            unicodeToAscii(bais, "IBM420");
            System.out.println(s1);
            ByteArrayInputStream bais1=new ByteArrayInputStream(s1.getBytes("UTF-8"));
            unicodeToAscii(bais1, "IBM420");
        } catch (UnsupportedEncodingException e3) {
            e3.printStackTrace();
        }
        System.exit(0);
        

        //System.out.println(Charset.availableCharsets());
        PrintWriter pw=new PrintWriter(System.out);
        //String charSet="ISO-8859-1";
        //String charSet="ISO-8859-6";
        //String charSet="windows-1256";
        String[] charSets={"IBM420","windows-1256","ISO-8859-6"};
        for (String charSet : charSets) {
            
        pw=new PrintWriter(System.out);
        try {
            pw = new PrintWriter("C:\\Inbox\\Saudi\\"+charSet+".txt");
        } catch (FileNotFoundException e) {
        }
        printChars(charSet, Charset.forName(charSet).newEncoder(), 0, 127, "Basic", true, true, pw);
        printChars(charSet, Charset.forName(charSet).newEncoder(), 128, 255, "Latin-1", true, true, pw);
        printChars(charSet, Charset.forName(charSet).newEncoder(), 1536, 1791, "Arabic", true, true, pw);
        printChars(charSet, Charset.forName(charSet).newEncoder(), 64336, 65023, "Arabic P. A", true, true, pw);
        printChars(charSet, Charset.forName(charSet).newEncoder(), 65136, 65278, "Arabic P. B", true, true, pw);
        pw.flush();
        pw.close();
        }
        System.out.println("OK");
        System.exit(0); 
        for (String cs : Charset.availableCharsets().keySet()) {
            
        int i = -1;
        try {
            CharsetEncoder encoder = Charset.forName(cs).newEncoder();
            printChars(cs, encoder, 1536, 1791, "Arabic", false, false, pw);
            printChars(cs, encoder, 64336, 65023, "Arabic P. A", false, false, pw);
            printChars(cs, encoder, 65136, 65278, "Arabic P. B", false, false, pw);

        } catch (Exception e1) { 
            System.out.println("fail" + i + " " + e1.getMessage());
        }
        }

    }

    /**
     * @param cs
     * @param encoder
     * @param sta
     * @param end
     * @return
     */
    private static void printChars(String cs, CharsetEncoder encoder, int sta,
            int end, String range, boolean show, boolean notOk, PrintWriter pw) {
        int i;
        int ok=0;
        int nok=0;
        for (i = sta;i<end;i++) {
            char c = Character.toChars(i)[0];
            if (!Character.isDefined(c)) continue;
            int b=0x0;
            if (encoder.canEncode(c)) {
                try {
                    b=encoder.encode(CharBuffer.wrap(Character.toChars(i))).array()[0] & 0x00FF;
                } catch (CharacterCodingException e) {
                }
                ok++;
                if (show)
                pw.println(String.format("%05d %-85s -> OK, %04x : %04x = %s ", i,Character.getName(i), i, b, c));
            } else {
                b=encoder.replacement()[0];
                nok++;
                if (show && notOk)
                 pw.println(String.format("%05d %-85s ->NOK, %04x : %04x = %s ", i,Character.getName(i), i, b, c ));
            }
        }
        pw.println(String.format("ok: %d nok: %d - %s %s", ok , nok, cs, range));
    }
    
    public static byte[] intToBytes( final int i ) {
        if (i==0) return new byte[1];
        byte[] result = new byte[4];
        byte b = (byte) (i >> 24);
        if (b!=0) {
            result[0] = (byte) (i >> 24);
            result[1] = (byte) (i >> 16);
            result[2] = (byte) (i >> 8);
            result[3] = (byte) (i /*>> 0*/);
            return result;
        }
        b = (byte) (i >> 16);
        if (b!=0) {
            result = new byte[3];
            result[0] = (byte) (i >> 16);
            result[1] = (byte) (i >> 8);
            result[2] = (byte) (i /*>> 0*/);
            return result;
        }
        b = (byte) (i >> 8);
        if (b!=0) {
            result = new byte[2];
            result[0] = (byte) (i >> 8);
            result[1] = (byte) (i /*>> 0*/);
            return result;
        }
        result = new byte[1];
        result[0] = (byte) (i);
        return result;
    }

    /**
     * TEST
     * @param fis
     * @param charSet
     * @return
     */
    private static InputStream unicodeToAscii(InputStream fis, String charSet) {
        HashMap<Integer,byte[]> notMappedChars = new HashMap<Integer,byte[]>();
        CharsetEncoder encoder = Charset.forName(charSet).newEncoder();
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        int i = -1;
        char prev=' ';
        char next=' ';
        char curr=' ';
        boolean checked=false;
        try {
            char[] buf = new char[1];
            InputStreamReader r=new InputStreamReader(fis, "UTF-8"); 
            while ((i=r.read(buf)) >= 0) {
                next=buf[0];
                if (!checked) {
                    curr=next;
                    checked=true;
                    continue;
                } 
                buf= parseSurrogate(prev, next, curr);
                writeTarget(notMappedChars, encoder, target, buf);
                prev=curr;
                curr=next;
            }
            if (checked) {
                next=' ';
                buf= parseSurrogate(prev, next, curr);
                writeTarget(notMappedChars, encoder, target, buf);
            }

            r.close();
            target.close();

        } catch (IOException e1) { return fis ;
        }
        return new ByteArrayInputStream(target.toByteArray());
    }

    private static char[] parseSurrogate(char prev, char next, char curr) {
        int i;
        i = Character.codePointAt(new char[] {curr} , 0);
        if (Character.isLetter(curr)){
            if (Character.isLetter(prev)){
                if (Character.isLetter(next)){
                    System.out.println(String.format("%s %s %s %s %s", prev, curr, next, Character.getName(i), "middle"));
                } else {
                    System.out.println(String.format("%s %s %s %s %s", prev, curr, next, Character.getName(i), "final"));
                }
            } else {
                if (Character.isLetter(next)){
                    System.out.println(String.format("%s %s %s %s %s", prev, curr, next, Character.getName(i), "initial"));
                } else {
                    System.out.println(String.format("%s %s %s %s %s", prev, curr, next, Character.getName(i), "isolated"));
                }
            }
        } else {
            System.out.println(String.format("%s %s %s %s %s", prev, curr, next, Character.getName(i), "not letter"));
        }
        return new char[] {curr};
    }

    private static void writeTarget(HashMap<Integer, byte[]> notMappedChars, CharsetEncoder encoder,
            ByteArrayOutputStream target, char[] buf) throws IOException, CharacterCodingException {
        int i = Character.codePointAt(buf , 0);
        target.write(encoder.canEncode(buf[0])?
                encoder.encode(CharBuffer.wrap(buf)).array()
                :(notMappedChars.containsKey(i)?
                notMappedChars.get(i)
                :encoder.replacement()));
    }

}
