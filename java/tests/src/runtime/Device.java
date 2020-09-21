package runtime;

public class Device {
    private int id=0;
    private Unit u;
    private IReport report;
    private int lc=0;
    public int pc=0;
    public int ps=10;
    public int ls=40;
    
    public Device(int repId) {
        this.id=repId;
    }
    public int getId() {
        return id;
    }
    public void setup(Unit unit, IReport rep) {
        this.report=rep;
        this.u=unit;
    }
    
    public void write(String text) {
        if (lc>=ps) {
            report.atEndOfPage(u, this);
            report.writeTrailer(u, this);
            lc=0;
        }
        if (lc==0) {
            pc++;
            report.writeTitle(u, this);
            report.atStartOfPage(u, this);
        }
        System.out.println(text);
        lc++;
    }
    
    public void close() {
        if (lc > 0) {
            report.atEndOfPage(u, this);
            report.writeTrailer(u, this);
        }
        System.out.println("Close Device:" + getId());
    }
    

}
