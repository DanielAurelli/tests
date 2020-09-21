package runtime.config;

import adsoftec.config.AdsConfiguration;
import adsoftec.config.AdsDeviceInfo;
import adsoftec.config.AdsDeviceInfo.DevIOMethod;

public class ADSTEST extends AdsConfiguration
{
  public ADSTEST()
  {
  super();
  initialLibrary = "NATINFRA";
  zeroPrinting = false;
  //iodevices[0] = new AdsDeviceInfo(DevIOMethod.EXIO, ADS_STDIO_NAME, ADS_STDIO_MAXPAG, ADS_STDIO_LINSIZ, ADS_STDIO_PAGSIE);
  iodevices[0] = new AdsDeviceInfo(DevIOMethod.STDIO, ADS_STDIO_NAME, ADS_STDIO_MAXPAG, ADS_STDIO_LINSIZ, ADS_STDIO_PAGSIZE);
  iodevices[1] = new AdsDeviceInfo(DevIOMethod.FILE, "C:\\TEMP\\FILE1.OUT", 9999, (short)80, (short)66);
  }
 
}
