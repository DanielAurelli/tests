package tools.dms;

import java.io.*;
import java.util.*;

public class SuperTriggerGenerator {
    //
    private static final String CREATE_TRIGGER = 
            "\n----- "
          + "\nCREATE TRIGGER TRG_%s "
          + "\nBEFORE INSERT OR UPDATE ON %s " 
          + "\nFOR EACH ROW" 
          + "\nBEGIN"
          + "\n%s"
          +" \nEND;"
          +" \n/"
          +" \nCOMMIT\n";
    // parms for CREATE_TRIGGER format:
    // 1 - trigger name
    // 2 - table name
    // 3 - trigger statements 
    //
    private static final String ELM_DEF="SUBSTR(LPAD(TO_CHAR(:NEW.%s),%d,'%s'),%d,%d)";
    // parms for ELM_DEF format:
    // 1 - name
    // 2 - size
    // 3 - lpad char -> ' ' or '0', depending on format
    // 4 - substring start position
    // 5 - substring length
    //
    //
    private InputStream inputData;
    private OutputStream result;
    private String project;
    /**
     * Inner Class to keep SuperField definition
     */
    private class SuperFieldElm {
        public String name="";
        public char format = ' ';
        public int size = 10;
        public int init = 0;
        public int end  = 0;
        public boolean isNull = false;
        @Override
        public String toString() {
            return String
                    .format("Elm [%s (%s %d), init=%d, end=%d, isNull=%s]",
                            name, format, size, init, end, isNull);
        }
        
    }

    public SuperTriggerGenerator() {
    }
    
    public SuperTriggerGenerator(String prj, InputStream in, OutputStream out) {
        project = prj;
        inputData = in;
        result = out;
    }

    public static void main(String[] args) {
        if ( args.length != 2 ) {
            System.err.println( "Missing arguments: file_name project." );
            return;
          }
          FileInputStream lFile=null;
          try {
              String prj= args [1];
              File lF = new File( args[ 0 ] );
              if ( !lF.exists() )
                  throw new IOException( "File not found!!!!" );
              File outfile = new File(lF.getAbsoluteFile() + ".sql");
              PrintStream output = new PrintStream(outfile);
              lFile = new FileInputStream( lF );
              SuperTriggerGenerator stg = new SuperTriggerGenerator(prj, lFile, output);
              stg.execute();
              lFile.close();
              output.flush();
              output.close();
          } catch (EOFException e) { System.out.println("*** END OF FILE ***");
          } catch (Exception e) {
              if (lFile!=null)
                  try { lFile.close();
                  } catch (IOException e1) { }
              System.err.println("error: " + e.toString());
          }


    }

    private boolean execute() {
        if (inputData == null) return false;
        Scanner sc = new Scanner(inputData);
        
        String table="";
        String superField="";
        SuperFieldElm elm = null;
        ArrayList <SuperFieldElm> superCompound = new ArrayList <SuperFieldElm>();
        HashMap <String, ArrayList <SuperFieldElm>> structure = new HashMap <String, ArrayList <SuperFieldElm>> ();
        while (sc.hasNext()) {
            String line=sc.nextLine();
            String [] token = line.split(";");
            if (token.length != 16) {
                System.err.println("error: missing tokens at line: " + line);
            }
            if (! token[0].equalsIgnoreCase(project)) continue;
            if (table.length()==0) table=token[1];
            if (! table.equalsIgnoreCase(token[1])) {
                structure.put(superField, superCompound);
                createCommand(table, structure);
                structure = new HashMap <String, ArrayList <SuperFieldElm>> ();
                table=token[1];
                superField="";
                superCompound = new ArrayList <SuperFieldElm>();
                elm = null;
            }
            if (superField.length()==0) superField=token[3];
            if (!superField.equalsIgnoreCase(token[3])) {
                structure.put(superField, superCompound);
                superCompound = new ArrayList <SuperFieldElm>();
                superField=token[3];
            }
            elm = new SuperFieldElm();
            elm.name=token[10];
            elm.init=Integer.parseInt(token[11]);
            elm.end=Integer.parseInt(token[12]);
            elm.format=token[13].charAt(0);
            elm.size=Integer.parseInt(token[14]);
            elm.isNull=(token[6].equals("Y") || token[15].equals("Y"));
            superCompound.add(elm);
        }
        // last one
        structure.put(superField, superCompound);
        createCommand(table, structure);
        sc.close();
        return true;
        
    }

    private void createCommand(String table,
            HashMap<String, ArrayList<SuperFieldElm>> structure) {
        if (structure == null || structure.size() <= 0 ) {
            return;
        }
        StringBuffer statements= new StringBuffer();
        StringBuffer superAssign= new StringBuffer();
        StringBuffer superCond= new StringBuffer();
        String elmDef="";
        int subStart=0;
        int subLenth=0;
        String fill="";
        String nullValue="";
        //
        ArrayList <SuperFieldElm> superCompound = new ArrayList <SuperFieldElm>();
        for (String superField : structure.keySet()) {
            int c=0;
            boolean hasNull=false;
            superAssign.setLength(0);
            superCond.setLength(0);
            superCond.append("IF (");
            superAssign.append(":NEW.");
            superAssign.append(superField);
            superAssign.append(":=");
            superCompound=structure.get(superField);
            for (SuperFieldElm elm : superCompound) {
                switch (elm.format) {
                case 'P':
                case 'U':
                    subLenth=(elm.end - elm.init) + 1;
                    if (subLenth > elm.size) {
                        elm.size=subLenth;
                    }
                    subStart=elm.size - (elm.end - 1);
                    fill="0";
                    nullValue="0";
                    break;
                default:
                    subLenth=(elm.end - elm.init) + 1;
                    subStart=elm.init;
                    fill=" ";
                    nullValue="' '";
                    break;
                }
                elmDef=String.format(ELM_DEF, elm.name, elm.size, fill, subStart, subLenth);
                if (c > 0) superAssign.append("||");
                superAssign.append(elmDef);
                if (elm.isNull) {
                    hasNull=true;
                    if (c > 0) superCond.append(" AND ");
                    superCond.append(":NEW." + elm.name + "<>"+nullValue);
                }
                c++;
            }
            superAssign.append(";\n");
            if (hasNull) {
                superCond.append(") THEN\n  ");
                superAssign.insert(0,superCond.toString());
            }
            if (hasNull) {
                superAssign.append("ELSE\n  :NEW."+superField+":=NULL;\nEND IF;\n");
            }
            statements.append( superAssign.toString());
        }
        try {
            result.write(String.format(CREATE_TRIGGER, table, table, statements).getBytes("ISO-8859-1"));
        } catch (Exception e) {
            System.err.println(table + " Fail: " + e.toString());
        }
    }

}
