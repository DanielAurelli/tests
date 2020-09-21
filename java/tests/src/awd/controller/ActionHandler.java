/* =================================================================*
 *                   Copyright AdvanSol Inc.                        *
 *------------------------------------------------------------------*
 * Function: debug users actions                                    *
 * Author:  Consist                                Date: 15/06/2016 *
 *==================================================================*
 */
package awd.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;

import awd.controller.EndPointController.ROLE;

public class ActionHandler {
    public enum ACTION {
        START   ("start"),
        STOP    ("stop"),
        RESUME  ("resume"),
        STEP    ("stepnext"),
        GOINTO  ("stepinto"),
        GOOUT   ("stepout"),
        GOBREAK ("continue"),
        SETBREAK("setbreak"),
        REMBREAK("remove"),
        GETVAR  ("getvar"),
        SETVAR  ("setvar"),
        NONE;
        private String name="";
        private ACTION() {
        }
        private ACTION(String name) {
            this.name=name;
        }
        public static ACTION getAction(String s) {
            for (ACTION a : ACTION.values()) {
                if (a.name.equalsIgnoreCase(s)) return a;
            }
            return NONE;
        }
    }

    public static boolean parseCommand(SessionContext session, ROLE role, String command) throws Exception{
        JsonObject jo ;
        try {
            jo = Json.createReader(new StringReader(command)).readObject();
        } catch (Exception e) {
            return false;
        }
        ACTION action=ACTION.getAction(jo.getString("action"));
        String data=jo.getString("data");
        switch (role) {
        case DLG:
            switch (action) {
            case START:
                try {
                    return executeActionStart(session, data);
                } catch (Exception e ) {
                    throw new EventException(session, e.toString());
                }
                //
            case STOP:
                try {
                    return session.close();
                } catch (Exception e ) {
                    throw new EventException(session, e.toString());
                }
                //
            case RESUME:
            case GOINTO:
            case GOOUT:
            case GOBREAK:
            case STEP:
                try {
                    return true;
                } catch (Exception e ) {
                    throw new EventException(session, e.toString());
                }
                //
            case SETBREAK:
            case REMBREAK:
                throw new EventException(session, "Command not implemented.");
            }
            break;
            //
        }
        return false;
    }

    private static boolean executeActionStart(SessionContext session, String data) throws EventException {
        JsonObject jo;
        try {
            HashMap <String, String[]> pageParameters = new HashMap<String, String[]>();
            jo = Json.createReader(new StringReader(data)).readObject();
            String control=jo.getString("control");
            String event=jo.getString("event");
            StringWriter sw = new StringWriter();
            return true;
        } catch (Exception e ) {
            throw new EventException(session, e.toString());
        }
    }


    private static StringBuffer sendSourceLines(SessionContext session, ROLE role, Scanner sc) throws Exception {
        StringBuffer sb=new StringBuffer("[\n");
        int i=1;
        while (sc.hasNext()) {
            if (i>1) sb.append(",\n");
            sb.append(String.format("{\"idx\":%d,\"line\":\"%s\"}",i++,sc.nextLine()));
        }
        sc.close();
        sb.append("\n]");
        session.sendTo(role, Json.createObjectBuilder()
                .add("subject", "source")
                .add("content", sb.toString())
                .build().toString());
        //
        return sb;
    }

}
