/* =================================================================*
 *                   Copyright AdvanSol Inc.                        *
 *------------------------------------------------------------------*
 * Function: Keep context data for a debugger session               *
 * Author:  Consist                                Date: 15/06/2016 *
 *==================================================================*
 */
package awd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import awd.controller.EndPointController.ROLE;

public class SessionContext {
    
    private static HashMap <String, SessionContext> contexts = new HashMap <String, SessionContext>();
    //
    // instance attributes
    //
    private String id="";
    private Queue<EndPointController> queue = new ConcurrentLinkedQueue<EndPointController>();

    /**
     * SessionContext Factory
     * @param id
     * @return SessionContext
     */
    public static SessionContext getContext(String id) {
        if (!contexts.containsKey(id)) {
            contexts.put(id, new SessionContext(id));
        }
        return contexts.get(id);
    }
    
    /**
     * Constructor
     * @param id
     */
    public SessionContext(String id) {
        this.id=id;
    }
    
    public boolean addSession(EndPointController s) {
        return queue.add(s);
    }
    
    public ArrayList<EndPointController> getSessions() {
        ArrayList<EndPointController> result = new ArrayList<EndPointController>();
        for (EndPointController s : queue) {
            result.add(s);
        }
        return result;
    }
    
    public ArrayList<EndPointController> getSessionsByRole(ROLE r) {
        ArrayList<EndPointController> result = new ArrayList<EndPointController>();
        for (EndPointController s : queue) {
            if (s.getRole() == r) {
                result.add(s);
            }
        }
        return result;
    }
    
    public void sendAll(String msg) throws Exception{
        removeClosed();
        for (EndPointController s : queue) {
            if(s.getSession().isOpen()) {
                s.getSession().getBasicRemote().sendText(msg);
            }
        }
    }
    
    public void sendTo(ROLE role, String msg) throws Exception {
        removeClosed();
        for (EndPointController s : queue) {
            if (s.getRole()==role && s.getSession().isOpen()) {
                s.getSession().getBasicRemote().sendText(msg);
            }
        }
    }

    public void removeClosed() throws Exception {
        ArrayList<EndPointController> closedSessions= new ArrayList<EndPointController>();
        for (EndPointController s : queue) {
            if(!s.getSession().isOpen()) {
                closedSessions.add(s);
            }
        }
        queue.removeAll(closedSessions);
    }

    /**
     * check client connections, notify all and close.
     * @return boolean
     */
    public boolean close() throws Exception {
        sendTo(ROLE.CONSOLE, "Closing conections...");
        for (EndPointController s : queue) {
            s.getSession().close();
        }
        removeClosed();
        contexts.remove(this.id);
        return true;
    }

}
