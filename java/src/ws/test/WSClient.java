
package ws.test;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

@ClientEndpoint
public class WSClient  {
    private static Object waitLock = new Object();
    private static Session session = null;

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received msg: "+message);        
        try
        {
          if (session!=null && message.startsWith("New Connection"))
            session.getBasicRemote().sendText("{\"action\":\"setup\",\"data\":\"{\\\"key\\\":\\\"listSessionsId\\\",\\\"value\\\":\\\" \\\"}\"}");
        } catch (IOException e)
        {
          e.printStackTrace();
        }
    }
    @OnOpen
    public void onOpen() {
        System.out.println("OPEN");
    }
    @OnClose
    public void onClose() {
        System.out.println("CLOSE");
        System.exit(0);
    }

    private static void  wait4TerminateSignal()
    {
        synchronized(waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException e) {    
            }
        }
    }

 public static void main(String[] args) {

     WebSocketContainer container=null;//
     try {
         container = ContainerProvider.getWebSocketContainer(); 
         String myUrl="/ads/awi/debug?_id=3&_role=console";
         //String myUrl="/ads20/awi/debug?_id=10&_role=watcher";
         System.out.println(myUrl);
         URI uri=URI.create("ws://192.168.2.141:8080" +myUrl);
         session=container.connectToServer(WSClient.class, uri); 
         wait4TerminateSignal();
     } catch (Exception e) {
         e.printStackTrace();
     }

     finally{
         if(session!=null){
             try {
                 session.close();
             } catch (Exception e) {     
                 e.printStackTrace();
             }
         }         
     } 
 }
}