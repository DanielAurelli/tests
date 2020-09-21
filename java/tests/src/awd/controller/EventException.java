/*
 *==================================================================*
 *                   Copyright 2009 AdvanSol Inc.                   *
 *------------------------------------------------------------------*
 *                                                                  *
 * Function: Exception for AWI Debugger                             *
 * Author: Consist                                                  *
 * Date: 20/06/2016                                                 *
 *                                                                  *
 *==================================================================*
*/
package awd.controller;

import awd.controller.EndPointController.ROLE;

public class EventException extends Exception {

    public EventException(String message) {
        super(message);
    }
    public EventException(SessionContext s, String message) {
        super(message);
        try {
            s.sendTo(ROLE.CONSOLE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
