/**
 *==================================================================*
 *                   Copyright 2015 AdvanSol Inc.                   *
 *------------------------------------------------------------------*
 *                                                                  *
 * Function: ConsistADS Java Configuration Sample File              *
 * Date: 09/10/2015                                                 *
 *                                                                  *
 *==================================================================*
 */
package runtime.config;

import adsoftec.config.AdsConfiguration;
import adsoftec.config.AdsDeviceInfo;
import adsoftec.config.AdsDeviceInfo.DevIOMethod;

public class ADSBAT extends AdsConfiguration
{
  private static final short LINPERPAGE = 66;
  private static final short LINESIZE = 132;
  
  public ADSBAT()
  {
  super();                              /* Set base configuration   */
  int i;
  initialLibrary = "ADSINFRA";
  iodevices[0] = new AdsDeviceInfo(DevIOMethod.FILE, "$prt31", 32767, LINESIZE, LINPERPAGE);
  for (i = 1; i < ADS_MAX_FILE_ID; i++)
    {
    String devName = String.format("$prt%d", i);
    iodevices[i] = new AdsDeviceInfo(DevIOMethod.FILE, devName, 32767, LINESIZE, LINPERPAGE);
    }
  userIdentification = "CONSIST";
  execMode           = ADS_EXECUTION_MODES.Batch;
  errorTreatment     = "NFWERRTA";
  }
}
