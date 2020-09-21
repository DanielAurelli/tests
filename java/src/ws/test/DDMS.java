package ws.test;

import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.jws.*;
import ads.*;
import obvius.sys.Association;
import obvius.util.OInteger;

import ads.ReadConfig;

public class DDMS
{
private static final String TABLE = "%s;%s\n";


public static class DDMOWNSOut {
private String next;
public String getnext() {
  return this.next;
}
private int nbRows;
public int getnbRows() {
  return this.nbRows;
}
private String[] creator;
public String[] getcreator() {
  return this.creator;
}
}

public static class DDMTABSOut {
private String owner;
public String getowner() {
  return this.owner;
}
private String filter;
public String getfilter() {
  return this.filter;
}
private String next;
public String getnext() {
  return this.next;
}
private int nbRows;
public int getnbRows() {
  return this.nbRows;
}
private String[] table;
public String[] gettable() {
  return this.table;
}
}

@WebMethod(action="ddmowns") 
public static DDMOWNSOut DDMOWNS( 
      @WebParam(name="next") String anext, @WebParam(name="nbRows") int anbRows, 
      @WebParam(name="creator") String[] acreator ) throws Exception
{
  Association lAssoc = SessionPool.getSession();
  if ( lAssoc == null )
    throw new Exception( "Error getting session");
  ByteArrayOutputStream oHeaderBuffer = new ByteArrayOutputStream();
  ByteArrayOutputStream oParamBuffer = new ByteArrayOutputStream();
  ByteArrayOutputStream oBuffer = new ByteArrayOutputStream();
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )30, ( short )0 );
  ADSWPBufferBuilder.writeParamInt( oParamBuffer, false, false, false, ( byte )4, ( short )0 );
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, true, false, false, ( short )30, ( short )200 );
  oHeaderBuffer.write( "DDMOWNS ".getBytes() );
  oHeaderBuffer.write( ( byte )ADSWPBufferBuilder.FMT_OPTION_DATE_TIME_EXTERNAL );
  oHeaderBuffer.write( ( byte )0 );
  ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )3 );
  ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )oParamBuffer.size() );
  oParamBuffer.writeTo( oHeaderBuffer );
  if ( anext == null) anext = " ";
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( anext, 30 ) );
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareIntBuffer( anbRows ) );
  for ( int i=0; i<200; i++ )
      if ( i < acreator.length ){
          if ( acreator[i] == null) acreator[i] = " ";
          oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( acreator[i], 30 ) );
      }else{
          if ( acreator[i] == null) acreator[i] = " ";
          oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( "", 30 ) );
      }
  oHeaderBuffer.writeTo( oBuffer );
  OInteger oLen = new OInteger( 0 );
  byte[] oOutput = new byte[ 64000 ];
  boolean lResMth = ADSWPStb.ADSWPEX( lAssoc, 0, oBuffer.size(), oBuffer.toByteArray(), 0, oLen, oOutput );
  int multiplier = 0;
  while(lResMth && oLen.value==16000 && multiplier < 4){
     multiplier++;
     byte[] next16k = new byte[8];
     lResMth = ADSWPStb.ADSWPEX( lAssoc, 0, 8, next16k, 16000*multiplier, oLen, oOutput );
  }
  //SessionPool.releaseSession( lAssoc );
  SessionPool.freeSession(lAssoc);
  if ( lResMth ) {
    ByteArrayInputStream oResult = new ByteArrayInputStream( oOutput );
    DDMOWNSOut res = new DDMOWNSOut();
    res.next = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )30 );
    res.nbRows = ADSWPBufferBuilder.getIntBuffer( oResult );
    res.creator = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )30, 200 );
    return res;
  }
  else
    throw new Exception( "Error Type:" + lAssoc.GetErrorType() + " Error Code:" + lAssoc.GetErrorCode() );
}

@WebMethod(action="ddmtabs") 
public static DDMTABSOut DDMTABS( 
      @WebParam(name="owner") String aowner, @WebParam(name="filter") String afilter, 
      @WebParam(name="next") String anext, @WebParam(name="nbRows") int anbRows, 
      @WebParam(name="table") String[] atable ) throws Exception
{
  Association lAssoc = SessionPool.getSession();
  if ( lAssoc == null )
    throw new Exception( "Error getting session");
  ByteArrayOutputStream oHeaderBuffer = new ByteArrayOutputStream();
  ByteArrayOutputStream oParamBuffer = new ByteArrayOutputStream();
  ByteArrayOutputStream oBuffer = new ByteArrayOutputStream();
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )30, ( short )0 );
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )32, ( short )0 );
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )32, ( short )0 );
  ADSWPBufferBuilder.writeParamInt( oParamBuffer, false, false, false, ( byte )4, ( short )0 );
  ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, true, false, false, ( short )32, ( short )400 );
  oHeaderBuffer.write( "DDMTABS ".getBytes() );
  oHeaderBuffer.write( ( byte )ADSWPBufferBuilder.FMT_OPTION_DATE_TIME_EXTERNAL );
  oHeaderBuffer.write( ( byte )0 );
  ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )5 );
  ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )oParamBuffer.size() );
  oParamBuffer.writeTo( oHeaderBuffer );
  if ( aowner == null) aowner = " ";
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( aowner, 30 ) );
  if ( afilter == null) afilter = " ";
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( afilter, 32 ) );
  if ( anext == null) anext = " ";
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( anext, 32 ) );
  oHeaderBuffer.write( ADSWPBufferBuilder.prepareIntBuffer( anbRows ) );
  for ( int i=0; i<400; i++ )
      if ( i < atable.length ){
          if ( atable[i] == null) atable[i] = " ";
          oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( atable[i], 32 ) );
      }else{
          if ( atable[i] == null) atable[i] = " ";
          oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( "", 32 ) );
      }    
  oHeaderBuffer.writeTo( oBuffer );
  OInteger oLen = new OInteger( 0 );
  byte[] oOutput = new byte[ 64000 ];
  boolean lResMth = ADSWPStb.ADSWPEX( lAssoc, 0, oBuffer.size(), oBuffer.toByteArray(), 0, oLen, oOutput );
  int multiplier = 0;
  while(lResMth && oLen.value==16000 && multiplier < 4){
     multiplier++;
     byte[] next16k = new byte[8];
     lResMth = ADSWPStb.ADSWPEX( lAssoc, 0, 8, next16k, 16000*multiplier, oLen, oOutput );
  }
  //SessionPool.releaseSession( lAssoc );
  SessionPool.freeSession(lAssoc);
  if ( lResMth ) {
    ByteArrayInputStream oResult = new ByteArrayInputStream( oOutput );
    DDMTABSOut res = new DDMTABSOut();
    res.owner = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )30 );
    res.filter = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )32 );
    res.next = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )32 );
    res.nbRows = ADSWPBufferBuilder.getIntBuffer( oResult );
    res.table = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )32, 400 );
    return res;
  }
  else
    throw new Exception( "Error Type:" + lAssoc.GetErrorType() + " Error Code:" + lAssoc.GetErrorCode() );
}


public static void main(String[] args)
{
    
    // set server configuration
    Properties p = new Properties();
    p.put(ReadConfig.SERVER_IDENT, "172.20.199.90");
    p.put(ReadConfig.PORT_IDENT, "7220" /* "7576" */);
    p.put(ReadConfig.USER_IDENT, "ads");
    p.put(ReadConfig.PWD_IDENT, "ads");
    p.put(ReadConfig.MAXCONN_IDENT, "100");
    new ReadConfig(p);
    //
    System.out.println(listTableOwner());
    System.out.println(listTableOwner());
    System.out.println(tableList( "DB2INST1", "", "" ));
    System.out.println(listTableOwner());
}
public static String listTableOwner()
{
  StringBuilder lResult = new StringBuilder( );
  String lNext = "";
  try
  {
    do
    {
      DDMOWNSOut lRes = DDMOWNS( lNext, 0, new String[ 200 ] );
      lNext = lRes.getnext();
      int lLen = lRes.getnbRows();
      for ( int i=0; i<lLen; i++ )
        lResult.append( lRes.getcreator()[ i ].trim() + "\n");
    }
    while ( !lNext.trim().equalsIgnoreCase( "" ) ); 
  }
  catch (Exception e)
  {
    return "(!) " + e.toString();
  }
  return lResult.toString();
}

public static String tableList( String aOwner, String aDBName, String aFilter )
{
  StringBuilder lResult = new StringBuilder( );
  String lNext = "";
  try
  {
    do
    {
      DDMTABSOut lRes = DDMTABS( aOwner, aFilter, lNext, 0, 
                                     new String[ 400 ] );
      lNext = lRes.getnext();
      int lLen = lRes.getnbRows();
      for ( int i=0; i<lLen; i++ )
        lResult.append( String.format( TABLE, aOwner, lRes.gettable()[ i ].trim() ) );
    }
    while ( !lNext.trim().equalsIgnoreCase( "" ) ); 
  }
  catch (Exception e)
  {
    return "(!) " + e.toString();
  }
  return lResult.toString();
}

}
