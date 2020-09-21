package runtime;

public class Program extends Unit {

    public static void main(String[] args) {
        Program p = new Program();
        p.run();
    }

    private void run() {
        IReport rep1 = new IReport() {
            public void writeTitle(Unit u, Device d) {
                u.writeTitle(u, d);
            }
            public void atStartOfPage(Unit u, Device d) {
                System.out.println("PROGRAM LOGIC FOR AT START OF PAGE DEVICE: " + d.getId());
            }
            public void atEndOfPage(Unit u, Device d) {
            }
            public void writeTrailer(Unit u, Device d) {
                
            }
        };
        Device dev0=new Device(0);
        dev0.setup(this, rep1);

        Device dev1=new Device(1);
        dev1.setup(this, new IReport() {
            public void writeTitle(Unit u, Device d) {
                System.out.println("PROGRAM TITLE device:" + d.getId() +" Page:" + d.pc );
            }
            public void atStartOfPage(Unit u, Device d) {
                System.out.println("PROGRAM LOGIC FOR AT START OF PAGE " + d.pc);
            }
            public void atEndOfPage(Unit u, Device d) {
                System.out.println("PROGRAM LOGIC FOR AT END OF PAGE " + d.pc);
            }
            public void writeTrailer(Unit u, Device d) {
                
            }
        });
        
        //
        // Program body
        //
        for (int i = 1;i<=25;i++) {
            dev0.write("OLA DEV 0 " + i);
        }
        for (int i = 1;i<=15;i++) {
            dev1.write("OLA DEV 1 " + i);
        }
        //
        // End of Program
        //
        dev0.close();
        dev1.close();

        
    }

    
    
}
