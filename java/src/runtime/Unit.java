package runtime;

public class Unit implements IReport{
    
    public void writeTitle(Unit u, Device d) {
        generateDefaultTile(d);
    }
    public void atStartOfPage(Unit u, Device d) {
    }
    public void atEndOfPage(Unit u, Device d) {
    }
    public void writeTrailer(Unit u, Device d) {
        
    }

    private void generateDefaultTile(Device d) {
        System.out.println("GENERATE UNIT DEFAULT TITLE page:" + d.pc);
    }
}
