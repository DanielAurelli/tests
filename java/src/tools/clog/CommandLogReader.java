package tools.clog;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CommandLogReader
{
  private static Charset ibmCharset = Charset.forName("IBM037");
  private static PrintStream output = System.out;
  private static final int BASIC_RLEN = 8904;


  public static void main(String[] args)
  {
    if ( args.length == 0 ) {
      System.err.println( "Missing argument: File name." );
      return;
    }
    InputStream lFile=null;
    try {
        File lF = new File( args[ 0 ] );
        if ( !lF.exists() )
            throw new IOException( "File not found!!!!" );
        // to send output to file:
        File outfile = new File(lF.getAbsoluteFile() + ".diagnosis");
        output = new PrintStream(outfile);
        lFile = new FileInputStream( lF );
        while (readHeader(lFile)) 
        {
            //readRecord(lFile);
            output.flush();
        }
        lFile.close();
        output.flush();
        output.close();
    } catch (EOFException e) { output.println("\n*** END OF FILE ***");
    } catch (Exception e) {
        if (lFile!=null)
            try { lFile.close();
            } catch (IOException e1) { }
        System.err.println("error: " + e.toString());
    }

  }

  @SuppressWarnings("unused")
  private static boolean readHeader(InputStream lFile) throws Exception
  {
    byte[] b = new byte[ BASIC_RLEN ];
    readBuff( b, lFile );
    int rLen = readValue(b, 0);
    int rFiller = readValue(b, 2);
    int rType = readValue(b, 4);
        rFiller = readValue(b, 6);
    String storeClock=readHexString(b, 8, 8);
    String bType = readHexString(b, 16, 1);
    int prty  = byteToInt(b[17]);
    byte cType = b[18];
    int nECB  = byteToInt(b[19]);
    int nThread = byteToInt(b[20]);
    byte ub      = b[21];
    int ndesc  = readValue(b, 22);
    String jName = readString(b,24,8);
    String commId = readString(b, 32, 28); 
    String eTime = readHexString(b, 60, 4); 
    String cNumber = readHexString(b, 64, 4); 
    int dbid = readValue(b,68);
    int nASSO = readValue(b,70);
    int nDATA = readValue(b,72);
    int nWORK = readValue(b,74);
    int rID = readValue(b,76);
    byte architeture = b[78];
    String filler = readHexString(b, 79, 13); 
    //
    if ((b[16] == 0x00)) 
      return true;
    
    output.format("\nRecord Lenght...: %d"
               +"\nRecord Type.....: %d"
               +"\nBuffer Type.....: %s"
               +"\nCommunicId .....: %s"
               +"\nJobName ........: %s"
               +"\nDBID ..... .....: %d"
               +"\nCommand Sequence: %s"
               +"\n"
               , rLen, rType, bType, commId, jName, dbid, cNumber );
    //
    // CB always present
    String cmdCode = readString(b, 94, 2);
    String cmdId   = readHexString(b, 96, 4);
    int fnr        = readValue(b,100);
    int cmdRC      = readValue(b,102);
    int FBlen      = readValue(b,116);
    int RBlen      = readValue(b,118);
    int SBlen      = readValue(b,120);
    int VBlen      = readValue(b,122);
    int IBlen      = readValue(b,124);
    output.format("\nCB: "
        +"\ncmd ID .........: %s"
        +"\ncmd Code .......: %s"
        +"\ncmd RC .........: %d"
        +"\nFNR    .........: %d"
        +"\n"
        , cmdId, cmdCode, cmdRC, fnr );
    //
    int offSet=172;
    int len = readValue(b,offSet);
    if ((b[16] & 0x02)>0 && FBlen > 0) // FB 
    {
      output.println("FB lenght: " +FBlen); 
      output.println(readHexString(b, offSet + 2, len - 2));
      output.println(readString(b, offSet + 2, len - 2));
      offSet = offSet + len;
      len = readValue(b,offSet);
    }
    if ((b[16] & 0x04)>0 && RBlen > 0) // RB 
    {
      output.println("RB lenght: "+RBlen); 
      output.println(readHexString(b, offSet + 2, len - 2));
      output.println(readString(b, offSet + 2, len - 2));
      offSet = offSet + len;
      len = readValue(b,offSet);
    }
    if ((b[16] & 0x08)>0 && SBlen > 0) // SB 
    {
      output.println("SB lenght: "+SBlen); 
      output.println(readHexString(b, offSet + 2, len - 2));
      output.println(readString(b, offSet + 2, len - 2));
      offSet = offSet + len;
      len = readValue(b,offSet);
    }
    if ((b[16] & 0x10)>0 && VBlen > 0) // VB 
    {
      output.println("VB lenght: "+VBlen); 
      output.println(readHexString(b, offSet + 2, len - 2));
      output.println(readString(b, offSet + 2, len - 2));
      offSet = offSet + len;
      len = readValue(b,offSet);
    }
    if ((b[16] & 0x20)>0 && IBlen > 0) // IB 
    {
      output.println("IB lenght: "+IBlen); 
      output.println(readHexString(b, offSet + 2, len - 2));
      output.println(readString(b, offSet + 2, len - 2));
      offSet = offSet + len;
      len = readValue(b,offSet);
    }
    if ((b[16] & 0x40)>0) // IO LIST
    {
      output.println("IO LIST"); 
    }
    if ((b[16] & 0x80)>0) // EXIT B 
    {
      output.println("EXIT B"); 
    }
    return true;
  }

  private static void readBuff( byte[] b, InputStream aFile ) throws IOException
  {
    Arrays.fill( b, ( byte )0 );
    int lRead = aFile.read( b );
    if ( lRead <= 0 ) {
       throw new EOFException( "End Of File" );
    }
  }
  
  private static String readString( byte[] aBuff, int aInitial, int aLen )
  {
    return new String( aBuff, aInitial, aLen, ibmCharset );
  }

  private static String readHexString( byte[] aBuff, int aInitial, int aLen )
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < aLen; i++) {
        sb.append(String.format("%02X ", aBuff[aInitial + i]));
    }
    return sb.toString();
  }
  
  private static int readValue( byte[] b, int aInit )
  {
    int lResult = byteToInt( b[ aInit ] )<< 8;
    lResult += byteToInt( b[ aInit+1 ] );
    if ( lResult == 0xFFFF )
      return -1;
    else return lResult; 

  }

  private static int byteToInt( byte b )
  {
    return b & 0xFF;
  }
  
  private static int bytesToInt( byte[] b, int aInit, int aLen )
  {
    int lLen = 0;
    for ( int i=0; i<aLen; i++)
      lLen += byteToInt( b[ aInit + i ] ) << ( 8 * ( aLen-i-1 ) );
    return lLen;
  }

}
