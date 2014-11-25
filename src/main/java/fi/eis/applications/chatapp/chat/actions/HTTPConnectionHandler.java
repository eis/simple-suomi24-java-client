package fi.eis.applications.chatapp.chat.actions;

/**
 * Creation Date: 23.11.2014
 * Creation Time: 21:09
 *
 * @author eis
 */
public interface HTTPConnectionHandler {
    String getHTMLFromURL(String url);

    String getHTMLFromURLWithCookie(String url, String cookieValue);
}
