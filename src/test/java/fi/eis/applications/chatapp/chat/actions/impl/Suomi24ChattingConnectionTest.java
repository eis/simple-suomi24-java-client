package fi.eis.applications.chatapp.chat.actions.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;


/**
 * Creation Date: 23.11.2014
 * Creation Time: 21:45
 *
 * @author eis
 */
public class Suomi24ChattingConnectionTest {

    @Test(expected = IllegalStateException.class)
    public void testParsing() {
        HTTPConnectionHandler httpHandler = new AbstractHTTPConnection() {
            @SuppressWarnings("resource")
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
        ChattingConnection sut = new Suomi24ChattingConnection(111, "foo", httpHandler);
        sut.connect();
    }
    
    @Test
    public void testRegexp1() {
        String testcontent1 =
            "<div class=\"login_form\">\n"+
            "<form action='http://chat2.suomi24.fi:8080/login' name='loginfrm' id='loginfrm' method=\"get\" accept-charset=\"ISO-8859-1\">\n"+
            "<input type='hidden' name='cid' value=\"123\" />\n"+
            "<font class=\"nickname_header\"><b>Nimimerkki:</b></font><br/>\n"+
            "<font class=\"nickname\">eis</font><br/>\n"+
            "<input type=\"hidden\" id=\"nick\" name=\"nick\" value=\"eis\" />\n"+
            "<input type=\"hidden\" name=\"name\" value=\"e-i-s-s-s\" />\n"+
            "<input type=\"hidden\" name=\"who\" value=\"7d318c1da342aed8eea783429d75c87b\" />\n"+
            "<!--METROPOL_SETTINGS-->\n"+
            "<!--\n"+
            "<input type=\"submit\" value='Sis&auml;&auml;n' />\n"+
            "-->\n"+
            "<button type=\"submit\" class=\"S24_button\"><span>Sis&auml;&auml;n</span></button>\n"+
            "</form>\n"
        ;
        Matcher m = Suomi24ChattingConnection.firstPattern.matcher(testcontent1);
        Assert.assertTrue(Suomi24ChattingConnection.firstPattern + " failed to match " + testcontent1,
                m.matches());
        Assert.assertEquals("eis", m.group(1));
        Assert.assertEquals("e-i-s-s-s", m.group(2));
        Assert.assertEquals("7d318c1da342aed8eea783429d75c87b", m.group(3));
    }
    @Test
    public void testRegexp2() {

        String testcontent2 = 
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">"+
            "<HTML>"+
            "<HEAD>"+
            "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">"+
            "</HEAD>"+
            "<BODY onload=\"top.window.location=('http://chat1.suomi24.fi:8080/chat/?uid=296801&cid=673&cs=4598984022979187204')\">"+
            "</BODY>"+
            "</HTML>"
        ;
        Matcher m = Suomi24ChattingConnection.secondPattern.matcher(testcontent2);

        Assert.assertTrue(Suomi24ChattingConnection.secondPattern + " failed to match " + testcontent2,
                m.matches());
        Assert.assertEquals("http://chat1.suomi24.fi:8080/chat/?uid=296801&cid=673&cs=4598984022979187204",
                m.group(1));
    }
    @Test
    public void testRegexp3() {
        
        String testurl = "http://chat1.suomi24.fi:8080/chat/?uid=296801&cid=673&cs=4598984022979187204";
        Matcher m = Suomi24ChattingConnection.thirdPattern.matcher(testurl);

        Assert.assertTrue(Suomi24ChattingConnection.thirdPattern + " failed to match " + testurl,
                m.matches());
        Assert.assertEquals("296801",
                m.group(1));
        Assert.assertEquals("4598984022979187204",
                m.group(2));
    }
    @Test
    public void testRegexp4() {
        String testContent = 
            "var channelId = 123;"+
            "var userId = 413409;"+
            "var userCs = '7803832308989846675';"+
            "var userRegistered = true;"+
            "var maxSelections = 5;"+
            "var maxEmoticons = 3;"+
            "var maxUsers = 90;"+
            "var roomName = 'Seurahuone';"+
            "var chatPageUrl = 'http://chat1.suomi24.fi:8080/tpl/';"+
            "var chatBodyUrl = 'http://chat1.suomi24.fi:8080/body/';"+
            "var chatBodyUrlBackup = '';"
                ;
        Matcher m = Suomi24ChattingConnection.fourthPattern.matcher(testContent);

        Assert.assertTrue(Suomi24ChattingConnection.fourthPattern + " failed to match " + testContent,
                m.matches());
        Assert.assertEquals("http://chat1.suomi24.fi:8080/body/",
                m.group(1));
    }
}
