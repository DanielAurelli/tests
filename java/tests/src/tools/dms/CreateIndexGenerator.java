package tools.dms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;

public class CreateIndexGenerator {

    private static HashMap<String,String> command = new HashMap<String,String>();
    static {
        command.put("msq", "CREATE INDEX %s\n on %s (%s );\nGO\n");
        command.put("ora", "CREATE INDEX %s\n on %s (%s );\n");
        command.put("db2", "CREATE INDEX %s\n on %s (%s );\n");
        command.put("sql", "CREATE INDEX %s\n on %s (%s );\n");
    }
    private static ArrayList<String> iNames = new ArrayList<String>();
    
    public CreateIndexGenerator() {
    }

    public static void main(String[] args) {
        String local = System.getProperty("user.dir");
        String filePath;
        String fileDestPath;
        String sysCode;
        String dbType;

        if (args.length>0) {
            filePath=args[0];
            dbType=(args.length>1?args[1].toLowerCase():"ora");
            sysCode=(args.length>2?args[2]:"");
            fileDestPath=(args.length>3?args[3]:"");
        } else {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the source CSV file for the index creation(use .\\ or ./ for specify the JAR dir): ");
            filePath = input.nextLine();
            if(filePath.contains(".\\"))
                filePath = filePath.replace(".\\", local + "\\");
            if(filePath.contains("./"))
                filePath = filePath.replace("./", local + "/");
            
            //DB Type specification.
            System.out.print("Enter the Database type (msq/ora/db2/sql): ");
            dbType = input.nextLine().toLowerCase();
            
            //System Code specification.
            System.out.print("Enter the System Code for Index criation: ");
            sysCode = input.nextLine();
            
            //Target output File.
            System.out.print("Enter the destination file for the index criation(use .\\ or ./ for specify the JAR dir): ");
            fileDestPath = input.nextLine();
            if(fileDestPath.contains(".\\"))
                fileDestPath = fileDestPath.replace(".\\", local + "\\");
            if(fileDestPath.contains("./"))
                fileDestPath = fileDestPath.replace("./", local + "/");
            input.close();
        }
        String cmd=command.get(dbType);
        if (cmd==null) {
            System.err.println("Invalid db type");
            System.exit(-1);
        }
        //Source File.
        InputStream in=null;
        try{
            in = new FileInputStream(filePath);
        } catch (FileNotFoundException e){
            System.err.println("Specified file don't exist");
            System.exit(-1);
            return;
        }
        PrintWriter out = null;
        try {
            out=(fileDestPath.length()>0?new PrintWriter(fileDestPath):new PrintWriter(System.out));
        } catch (IOException e1) {
            System.err.println("Invalid output. " + e1.getMessage());
            System.exit(-1);
        }
        //
        start(cmd, sysCode, in, out);
    }
    
    public static void start(String cmd, String sysCode, InputStream in, PrintWriter out){
        String dados[] = new String[4];
        LinkedHashMap<String, String> keysReaded = new LinkedHashMap<String, String>();
        Scanner sc = new Scanner(in);
        while(sc.hasNext()) {
            dados = sc.nextLine().split(";");
            if (dados[1].equalsIgnoreCase("program")) continue;
            if (sysCode.length()>0 && ! dados[0].equalsIgnoreCase(sysCode)) continue;
            
            if((dados[3].toUpperCase().startsWith("SUPER") 
                    || dados[3].toUpperCase().startsWith("SP_"))) continue;
            //
            if(keysReaded.containsKey(dados[3])){
                if(keysReaded.get(dados[3]).equals(dados[2]))
                    continue;
            }
            String index ="IDX_" + dados[2] + "_" + dados[3];
            if (index.length()>30) {
                index="IDX_"+dados[2].substring(0, Math.min(6, dados[2].length()))+"_"+gererateHashKey(dados[3]);
            }
            int s=12;
            while (s>0 && (index.length()>30 || iNames.contains(index))) {
                if (s<4) {
                    index="I_"+generateRandomName(8)+"_"+generateRandomName(s--);
                    continue;
                }
                index="IDX_"+dados[2].substring(0, Math.min(6, dados[2].length()))+"_"+generateRandomName(s--);
            }
            iNames.add(index);
            out.write(String.format(cmd, index, dados[2], dados[3]));
            keysReaded.put(dados[3], dados[2]);
            out.flush();
        }
        sc.close();
        out.close();
        System.out.println("Job Finished with Success");
    }
    
    /**
     * generate new randomized string with valid chars
     * @param length
     * @return String
     */
    protected static String generateRandomName(int size) {
        final String characters = "ABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
        Random rand = new Random();
        StringBuffer result = new StringBuffer();
        while(size-- > 0) {
            result.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return result.toString();
     }
    
    protected static String gererateHashKey(String s) {
        return String.format("%d", Math.abs(s.hashCode()));
    }
}
