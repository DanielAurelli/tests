package runtime;

public interface IReport {
    
    public void writeTitle(Unit u, Device d) ;
    public void writeTrailer(Unit u, Device d) ;
    public void atStartOfPage(Unit u, Device d);
    public void atEndOfPage(Unit u, Device d) ;
}
