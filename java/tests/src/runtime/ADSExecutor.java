package runtime;

import java.util.Properties;

import ads.ReadConfig;
import ads.reference.remote.server.UIHandler;
import awi.AWIExecutor;
import awi.AWIServerInterface;
import ads.AdsRemoteUI;
import ads.AdsScreen;
import obvius.sys.Session;

public class ADSExecutor
{
  public static void main(String[] args) {
    Properties sessionConf=new Properties();
    sessionConf.setProperty(ReadConfig.MAXCONN_IDENT, "50");
    sessionConf.setProperty(ReadConfig.SERVER_IDENT, "177.93.109.92");
    sessionConf.setProperty(ReadConfig.PORT_IDENT, "7577");
    sessionConf.setProperty(ReadConfig.USER_IDENT, "CTDAA");
    sessionConf.setProperty(ReadConfig.PWD_IDENT, "CTDAA");
    new ReadConfig(sessionConf);
    
    AWIServerInterface lExec = new AWIServerInterface(new Session(ReadConfig.getServer(), ReadConfig.getPort()), "");
    if (!lExec.logon(ReadConfig.getUser(), ReadConfig.getPwd(), "")) {
      if (!lExec.recover()) {
        System.out.println("logon fail:" + lExec.getReturnCode());
        System.exit(-1);
      } 
      lExec.logon(ReadConfig.getUser(), ReadConfig.getPwd(), "");
    }
    int lResp=0;
    try {
      if (!lExec.start("HELLO", "SAMPLES", ReadConfig.getUser())) {
        System.out.println("start fail");
        System.exit(-1);
      }
      lResp=lExec.getReturnCode();
      AdsRemoteUI ui=new UIHandler();
      //ui.setRenderer(new AdsFx());
      AdsScreen screen = UIHandler.createAdsScreen(lExec.form);
      screen.requestState=AdsScreen.STATE_WAIT;
      while (lResp==0) {
        lResp=ui.requestUI(screen);  // show screen
        byte key =screen.getKeyTyped();
        if (key==0xff || lResp==-1) { // close request
          break;
        }
        lResp = execute(lExec, key);
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    lExec.logoff();
    System.out.println("Resp:"+ lResp);
    System.exit(0);
  }

  public static int execute(AWIServerInterface lExec, byte aKey){
    if ( !lExec.execute( aKey ) ) return AWIExecutor.ERR_CODE_FATAL;
    if (lExec.terminated()) return AWIExecutor.ERR_CODE_TERMINATED;
    if (lExec.restart()) return AWIExecutor.ERR_CODE_RESTART;
    if (lExec.uploading()) return AWIExecutor.ERR_CODE_UPLOAD;
    if (lExec.invTerminated() ) return AWIExecutor.ERR_CODE_FATAL;    
    return AWIExecutor.ERR_CODE_NONE;
  }

  public static void outraForma() {
    // Execute Remote ADS
    Properties sessionConf=new Properties();
    sessionConf.setProperty(ReadConfig.MAXCONN_IDENT, "2");
    sessionConf.setProperty(ReadConfig.SERVER_IDENT, "192.168.2.90");
    sessionConf.setProperty(ReadConfig.PORT_IDENT, "7322");
    sessionConf.setProperty(ReadConfig.USER_IDENT, "CTDAA");
    sessionConf.setProperty(ReadConfig.PWD_IDENT, "CTDAA");
    AdsRemoteUI ui=new UIHandler();
    //ui.setRenderer(new ui.application.AdsFx());
    try {
      ui.setupExec(sessionConf);
      int res = ui.start("SAMPLES", "HELLO", new AdsScreen(""));
      System.out.println("terminando:" + res);
    } catch (Exception e) {
      System.out.println(e.toString());
    }

  }
}
