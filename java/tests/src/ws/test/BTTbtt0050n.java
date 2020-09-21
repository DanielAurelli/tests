/*==================================================================
 *         Copyright (C) 2016, Advansol Technologies Inc.
 *------------------------------------------------------------------
 * Function: Stub Generator                      Date: 01-jul-2016
 *
 *==================================================================
 */

package ws.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.jws.*;
import ads.*;
import obvius.sys.Association;
import obvius.util.OInteger;

@WebService(name="BTTbtt0050n") public class BTTbtt0050n {

  public static class BTT0050NOut {
      private String wprLinhJcl;
      public String getwprLinhJcl() {
        return this.wprLinhJcl;
      }
      private String wprTipoComdJcl;
      public String getwprTipoComdJcl() {
        return this.wprTipoComdJcl;
      }
      private String wprDlmtComdJcl;
      public String getwprDlmtComdJcl() {
        return this.wprDlmtComdJcl;
      }
      private String wprNomeComdJcl;
      public String getwprNomeComdJcl() {
        return this.wprNomeComdJcl;
      }
      private String wprParmComdJcl;
      public String getwprParmComdJcl() {
        return this.wprParmComdJcl;
      }
      private String wprCmtrComdJcl;
      public String getwprCmtrComdJcl() {
        return this.wprCmtrComdJcl;
      }
      private String wprIndcCmtrJcl;
      public String getwprIndcCmtrJcl() {
        return this.wprIndcCmtrJcl;
      }
  }
  @WebMethod(action="btt0050n") 
  public static BTT0050NOut BTT0050N( 
        @WebParam(name="wprLinhJcl") String awprLinhJcl,
    
        @WebParam(name="wprTipoComdJcl") String awprTipoComdJcl, 
        @WebParam(name="wprDlmtComdJcl") String awprDlmtComdJcl, 
        @WebParam(name="wprNomeComdJcl") String awprNomeComdJcl,
    
        @WebParam(name="wprParmComdJcl") String awprParmComdJcl, 
        @WebParam(name="wprCmtrComdJcl") String awprCmtrComdJcl, 
        @WebParam(name="wprIndcCmtrJcl") String awprIndcCmtrJcl ) throws Exception
  {
    Association lAssoc = SessionPool.getSession();
    if ( lAssoc == null )
      throw new Exception( "Error getting session");
    ByteArrayOutputStream oHeaderBuffer = new ByteArrayOutputStream();
    ByteArrayOutputStream oParamBuffer = new ByteArrayOutputStream();
    ByteArrayOutputStream oBuffer = new ByteArrayOutputStream();
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )253, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )4, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )80, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )80, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )253, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )253, ( short )0 );
    ADSWPBufferBuilder.writeParamAlpha( oParamBuffer, false, false, false, ( short )1, ( short )0 );
    oHeaderBuffer.write( "BTT0050N".getBytes() );
    oHeaderBuffer.write( ( byte )ADSWPBufferBuilder.FMT_OPTION_DATE_TIME_EXTERNAL );
    oHeaderBuffer.write( ( byte )0 );
    ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )7 );
    ADSWPBufferBuilder.writeShort( oHeaderBuffer, ( short )oParamBuffer.size() );
    oParamBuffer.writeTo( oHeaderBuffer );
    if ( awprLinhJcl == null) awprLinhJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprLinhJcl, 253 ) );
    if ( awprTipoComdJcl == null) awprTipoComdJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprTipoComdJcl, 4 ) );
    if ( awprDlmtComdJcl == null) awprDlmtComdJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprDlmtComdJcl, 80 ) );
    if ( awprNomeComdJcl == null) awprNomeComdJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprNomeComdJcl, 80 ) );
    if ( awprParmComdJcl == null) awprParmComdJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprParmComdJcl, 253 ) );
    if ( awprCmtrComdJcl == null) awprCmtrComdJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprCmtrComdJcl, 253 ) );
    if ( awprIndcCmtrJcl == null) awprIndcCmtrJcl = " ";
    oHeaderBuffer.write( ADSWPBufferBuilder.prepareStrBuffer( awprIndcCmtrJcl, 1 ) );
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
    SessionPool.releaseSession( lAssoc );
    if ( lResMth ) {
      ByteArrayInputStream oResult = new ByteArrayInputStream( oOutput );
      BTT0050NOut res = new BTT0050NOut();
      res.wprLinhJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )253 );
      res.wprTipoComdJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )4 );
      res.wprDlmtComdJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )80 );
      res.wprNomeComdJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )80 );
      res.wprParmComdJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )253 );
      res.wprCmtrComdJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )253 );
      res.wprIndcCmtrJcl = ADSWPBufferBuilder.getStrBuffer( oResult, ( short )1 );
      return res;
    }
    else
      throw new Exception( "Error Type:" + lAssoc.GetErrorType() + " Error Code:" + lAssoc.GetErrorCode() );
  }

}
