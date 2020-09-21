package basics;

public class Hex2Asc {

    public static void main(String[] args) {
        byte cr = 0x0a;
        byte lf = 0x0d;
        cr|=0x10;
        lf|=0x10;
        String a ="Hello World!"+(char)cr+(char)lf;
        String b=encodeValue(a);
        String c=decodeValue(b);
        System.out.println(String.format("a=%s, b=%s, c=%s", a,b,c));
        String duasAspas = new String(new byte[] {Integer.decode("0x22").byteValue()});
        System.out.println(duasAspas);
        duasAspas = String.format("%c%s%c", 0x22,c, 0x22);
        System.out.println(duasAspas);
        String apos = "\u0027";
        System.out.println(apos);
        apos = String.format("%c%s%c", 0x27,c, 0x27);
        System.out.println(apos);
        System.out.println("byte 0x20:" + new String(new byte[] {0x20}));
        System.out.println("byte 0x40:" + new String(new byte[] {0x40}));
        System.out.println("byte 0x41:" + new String(new byte[] {0x41}));
        System.out.println("byte 0x27:" + new String(new byte[] {0x27}));
        System.out.println("byte 0x32:" + new String(new byte[] {0x32}));


    }
    public static String decodeValue(String value) {
        if (value.length() > 0) {
            int j=0;
            byte[] code = new byte[value.length()];
            for (int i = 0 ; i < value.length(); i=i + 2) {
                code[j++]=Integer.decode("0x" + value.substring(i, i+2)).byteValue();
            }
            return new String(code).trim();
        }
        return value;
    }
    
    public static String encodeValue(String value) {
        if (value.length() > 0) {
            StringBuffer sb= new StringBuffer();
            byte[] code = value.getBytes();
            for (int i = 0 ; i < code.length; i++) {
                sb.append(String.format("%x",code[i]));
            }
            return sb.toString();
        }
        return value;
    }

}
