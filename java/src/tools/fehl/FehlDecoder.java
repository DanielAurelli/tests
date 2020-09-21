package tools.fehl;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FehlDecoder {
    
    private static Charset ibmCharset = Charset.forName("IBM037");
//    private static int position = 0;
//    private static int fOffSet = 0;
    private static int fValueSize = 0;
    private static int fReclen = 0;
    private static String fShortName ="";
    private static int fErrorOffSet = 0;
    private static int fISN = 0;
    private static int fPEIndex = 0;
    private static int fAdaCode = 0;
    private static PrintStream output = System.out;

    public static void main(String[] args) {
        if ( args.length == 0 ) {
          System.err.println( "Missing argument: File name." );
          return;
        }
        FileInputStream lFile=null;
        try {
            File lF = new File( args[ 0 ] );
            if ( !lF.exists() )
                throw new IOException( "File not found!!!!" );
            // to send output to file:
            //File outfile = new File(lF.getAbsoluteFile() + ".diagnosis");
            //output = new PrintStream(outfile);
            lFile = new FileInputStream( lF );
            while (readHeader(lFile)) {
                output.print(String.format("ISN:%10d; Field:%2s; PE:%3d; CC:%2d;", fISN, fShortName, fPEIndex, fAdaCode));
                readRecord(lFile);
                output.flush();
            }
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
    
    private static void readRecord(FileInputStream aFile) throws IOException {
        if (fReclen > 16) {
            byte[] b = new byte[ fReclen - 16 ];
            readBuff( b, aFile );
            fReclen=readValue(b , 0);
            fISN = bytesToInt(b, 2, 4);
            if (fShortName.equals("  ")) {
                fValueSize = 0;
                output.print(String.format(" Offset:%04d; Value Size:%03d; ", fErrorOffSet, fValueSize ));
            } else {
                fValueSize = byteToInt(b[ fErrorOffSet]);
                output.print(String.format(" Offset:%04d; Value Size:%03d; ", fErrorOffSet, fValueSize - 1));
                output.print(readHexString( b, fErrorOffSet + 1 , fValueSize - 1  )+ ";");
                output.print(readString( b, fErrorOffSet + 1 , fValueSize - 1  )+ ";");
            }
            //output.println(readString( b, 8, b.length - 8  ));
            output.print("\n");
        }
    }
    
    private static boolean readHeader( FileInputStream aFile ) throws IOException
    {
      byte[] b = new byte[ 16 ];
      readBuff( b, aFile );
      //
      fReclen=readValue(b , 0);
      fShortName=readString( b, 4, 2 );
      fErrorOffSet=readValue(b , 6);
      fISN = bytesToInt(b, 8, 4);
      fPEIndex = byteToInt(b[ 12 ]);
      fAdaCode = ( int )b[ 13 ];
      //      
      return true;
    }
    
    private static void readBuff( byte[] b, FileInputStream aFile ) throws IOException
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
