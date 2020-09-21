/* =================================================================*
 *                   Copyright AdvanSol Inc.                        *
 *------------------------------------------------------------------*
 * Function: Controller for client connections                      *
 * Author:  Consist                                Date: 15/06/2016 *
 *==================================================================*
 */
package awd.controller;

import javax.websocket.*;
import javax.websocket.server.*;

/**
 * This class is a implementation of ServerEndPoint 
 * {@link javax.websocket.server.ServerEndpoint}.
 * Many clients can connect to this server end point. 
 * The same client can connect more than one component with different roles.
 * The roles defined are:
 * <br>  - "handler": main component responsible to issue debug actions 
 * command like: start, step, resume...
 * <br>  - "watcher": the inspector component, inquiry server and receive data
 *  variables contents.
 * <br>  - "stacker": the call stack component, receive the status
 *  of call stack on run-time interpreter
 * <br>  - "console": component that receive all information about debug processing.
 * <p>When a client component establishes a new connection, the method onOpen in invoked. 
 * The client must pass the parameters _id and _role. 
 * <br> _id: is a valid CwaUser index. 
 * <br> _role: one of describe above. 
 * <br> for each new value of _Id a new Session context is created to 
 * store debugger data and handling actions.
 * 
 * @author Consist
 *
 */
@ServerEndpoint(value="/awd/control")
public class EndPointController {
    //
    public enum ROLE {
        DLG("dialog"),
        IF("input"),
        BTN("button"),
        CONSOLE("console"),
        NONE;
        private String name="";
        private ROLE() {
        }
        private ROLE(String name) {
            this.name=name;
        }
        public static ROLE getRole(String s) {
            for (ROLE r : ROLE.values()) {
                if (r.name.equalsIgnoreCase(s)) return r;
            }
            return NONE;
        }
    }
    
    private ROLE clientRole=ROLE.NONE;
    private Session session;
    private SessionContext context=null;
    
    public ROLE getRole() {
        return this.clientRole;
    }
    public Session getSession() {
        return this.session;
    }

    
    @OnOpen
    public void onOpen(Session session) throws Exception { 
        String sessionId=(session.getRequestParameterMap().get("_id").get(0));
        this.clientRole=ROLE.getRole(session.getRequestParameterMap().get("_role").get(0));
        this.session = session; 
        this.context=SessionContext.getContext(sessionId);
        this.context.addSession(this);
        this.context.sendTo(ROLE.CONSOLE, "New Connection: " + session.getId() + " from:" + clientRole + " with:" + session.getQueryString());
    }
    
    @OnClose
    public void onClose(Session session) throws Exception { 
        this.context.removeClosed();
        this.context.sendTo(ROLE.CONSOLE, "Close Connection: " + session.getId() + " from:" + clientRole);
    }
    
    @OnMessage
    public void onMessage(Session session, String message) throws Exception { 
        this.context.sendTo(ROLE.CONSOLE, "Receive message: " + session.getId() + " from:" + clientRole + " : " + message);
        // Parse message, check role and command
        ActionHandler.parseCommand(this.context, this.clientRole, message);
    }
    
    @OnError
    public void onError(Session session, Throwable t) throws Exception { 
        this.context.removeClosed();
        this.context.sendTo(ROLE.CONSOLE, "Error detected: " + session.getId() + " from:" + clientRole + " msg:" + t.getMessage());
    }
    
}
