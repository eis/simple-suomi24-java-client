package fi.eis.applications.chatapp.chat.actions.impl;

import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;
import fi.eis.applications.chatapp.chat.actions.impl.AbstractHTTPConnection;
import fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Creation Date: 23.11.2014
 * Creation Time: 21:45
 *
 * @author eis
 */
public class ChattingConnectionTest {

    @Test(expected = IllegalStateException.class)
    public void testParsing() {
        HTTPConnectionHandler httpHandler = new AbstractHTTPConnection() {
            @Override
            public String getHTMLFromURL(String url) {
                InputStream in = null;
                try {
                    in = this.getClass().getResource("who-parsing-example-failed-login.html").openStream();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
                return readInputStream(in);
            }

            @Override
            public String getHTMLFromURLWithCookie(String url, String cookieValue) {
                return getHTMLFromURL(url);
            }
        };
        ChattingConnection sut = new ChattingConnection(111, "foo", httpHandler);
        sut.connect();
    }
}
