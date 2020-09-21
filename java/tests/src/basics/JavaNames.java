package basics;

public class JavaNames {

    public static void main(String[] args) {
        String[] names = {
                "CLIENTE_SUP_FATURADO-1-20",
                "COD-CLIENTE-SUP-FATURADO1-20",
                "#COD_CLIENTE_SUP_FATURADO",
                "#CD-GRUPO-EMPRESAS-R",
                "##VL-VERBA002",
                "G##VL-VERBA002",
                "#TAB-DIAS-DESCONTADOS-TOT",
                "#MSG---A-R",
                "P#PARM_001---FINAL",
                "P#PARM____001---FINAL",
                "$___$$$_001---FINAL___$",
                "$___####_001-###--FINAL___$",
                "_#$___####_001-###--FINAL___$",
                "_#$_A__!@#$%¨&*())#-L___$"
        };
        for (String s : names) {
            System.out.println(getJavaName(s));
            System.out.println(internalSubroutineName(s));
            System.out.println("================================");
        }
        long s1=System.nanoTime();
        for (int i = 0;i< 1000000; i++) {
            for (String s : names) {
                getJavaName(s);
            }
        }
        long f1=System.nanoTime();
        long e1=f1 - s1;
        System.out.println(String.format("JavaName 1000000 times    : %d nanos",e1));

        long s2=System.nanoTime();
        for (int i = 0;i< 1000000; i++) {
            for (String s : names) {
                internalSubroutineName(s);
            }
        }
        long f2=System.nanoTime();
        long e2=f2 - s2;
        System.out.println(String.format("internalName 1000000 times: %d nanos",e2));

    }

    public static String getJavaName(String aName){
        StringBuffer sb = new StringBuffer(aName.length());
        boolean capital=false;
        char last=' ';
        for (int i = 0;i<aName.length();i++) {
            char c = aName.charAt(i);
            if (Character.isLetter(c)) {
                sb.append(capital?Character.toUpperCase(c):Character.toLowerCase(c));
                capital=false;
            } 
            else if (Character.isDigit(c)) sb.append(c); // digit
            else if (!Character.isLetter(last) && !Character.isDigit(last))
                sb.append((Character.isJavaIdentifierPart(c)?c:'_'));
            else capital=true; 
            last=c;
        }
        return sb.toString(); 
    }
    
    public static String internalSubroutineName(String aName)
    {
    int j, k, l = aName.length();
    boolean toggleUp = false;
    byte b;
    byte []i, o = new byte[l];
    i = aName.toLowerCase().getBytes();
    for (j = k = 0; j < l; j++)
      if (((b = i[j]) >= 0x30) && (b <= 0x39)) o[k++] = b;
      else if ((b >= 0x61) && (b <= 0x7A)) if (toggleUp) { o[k++] = (byte)(b ^ 0x20); toggleUp = false; } else o[k++] = b;
      else if ((b == 0x2D) || (b == 0x5F)) if (toggleUp) o[k++] = 0x5F; else toggleUp = true;
      else o[k++] = 0x5F;
    return new String(o, 0, k);
    }
}
