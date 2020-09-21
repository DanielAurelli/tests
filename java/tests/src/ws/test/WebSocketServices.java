package ws.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 */
@ServerEndpoint(value="/wsoc/chat")
public class WebSocketServices {
    
    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static Queue<WebSocketServices> queue = new ConcurrentLinkedQueue<WebSocketServices>();

    private String nickname="";
    private Session session;
    
    public WebSocketServices() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }

    
    @OnOpen
    public void onOpen(Session session) { 
        this.session = session;
        // nickname=(this.session.getPathParameters().getOrDefault("username", nickname));
        nickname=(this.session.getRequestParameterMap().get("username").get(0));
        queue.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        sendAll(message);
        whois();
    }


    @OnClose
    public void onClose(Session session) { 
        queue.remove(session);
        String message = String.format("* %s %s", nickname, "has exit.");
        sendAll(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) { 
        if (message.equalsIgnoreCase(":who?")) {
            whois();
            return;
        }
        // Never trust the client
        sendAll(String.format("%s: %s",
                nickname, filter(message.toString())));
    }

    @OnError
    public void onError(Session session, Throwable throwable) { 
        queue.remove(session);
        String message = String.format("* %s %s", nickname, "has BROKEN.");
        sendAll(message);
    }
    
    
    private static void sendAll(String msg) {
        try {
            ArrayList<WebSocketServices> closedSessions= new ArrayList<WebSocketServices>();
            for (WebSocketServices wsoc : queue) {
                if(!wsoc.session.isOpen()) {
                    System.err.println("Closed session: "+wsoc.session.getId());
                    closedSessions.add(wsoc);
                } else {
                    wsoc.session.getBasicRemote().sendText(msg);
                }    
            }
            queue.removeAll(closedSessions);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    private void whois() {
        String message;
        try {
            message=String.format(" current sessions => %d ", queue.size() );
            this.session.getBasicRemote().sendText(message);
            for (WebSocketServices wsoc : queue) {
                if(wsoc.session.isOpen()) {
                    message=String.format("%s => %s ", wsoc.session.getId(), wsoc.nickname );
                    this.session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
        }
    }


    private static String filter(String message) {
        if (message == null)
            return (null);
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(content[i]);
            }
        }
        return (result.toString());
    }
}
