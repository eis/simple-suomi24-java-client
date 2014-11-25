package fi.eis.applications.chatapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;
import fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection.ConnectionParameters;
import fi.eis.applications.chatapp.chat.actions.impl.DefaultHTTPConnectionImpl;
import fi.eis.applications.chatapp.login.actions.impl.Suomi24LoginHandler;

/**
 * This is an integration test, meant to verify that login actually works
 * It should not be run as part of unit tests.
 * 
 * @author eis
 *
 */
public class LoginIT {
    @Test
    public void tyyppiLoginTest() throws Exception {
        String userName = "testityyppi";
        String userPass = "KJfsu4yFdY!d5&5@!tMq";
        
        String sessionCookie = new Suomi24LoginHandler().tryLogin(userName, userPass.toCharArray());
        
        HTTPConnectionHandler httpHandlerInput = new DefaultHTTPConnectionImpl();
        
        ChattingConnection cn = new ChattingConnection(123, sessionCookie, httpHandlerInput);
        cn.connect();
        ConnectionParameters params = cn.getConnectionParameters();

        assertEquals("testityyppi", params.getNick());
        assertEquals("testityyppi", params.getUserId());
        
    }
}
