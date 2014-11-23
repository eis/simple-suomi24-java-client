package fi.eis.applications.chatapp.actions.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Creation Date: 23.11.2014
 * Creation Time: 21:10
 *
 * @author eis
 */
public class DefaultHTTPConnectionImpl extends AbstractHTTPConnection {

    private static final String CHATAPP_USER_AGENT =  "Mozilla/5.0 (compatible; java-chatapp)";

    @Override
    public String getHTMLFromURL(final String url) {
        HttpURLConnection con = getHttpURLConnection(url);

        return readResponseContent(con);
    }

    private HttpURLConnection getHttpURLConnection(String url) {
        try {
            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection)myurl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", CHATAPP_USER_AGENT);

            con.setDoInput(true);
            return con;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getHTMLFromURLWithCookie(String url, String allCookiesAsString) {
        HttpURLConnection con = getHttpURLConnection(url);
        String cookieValuesAsAcceptedString = formatAsJavaNetAcceptableCookieString(allCookiesAsString, "ISO-8859-1");
        con.setRequestProperty("Cookie", cookieValuesAsAcceptedString);
        return readResponseContent(con);
    }

    protected String formatAsJavaNetAcceptableCookieString(final String allCookiesAsString, final String encoding) {
        String[] cookieValues = allCookiesAsString.split("\\r?\\n");
            System.out.println(String.format("Data now: %s (len %d)",
                    Arrays.asList(cookieValues), cookieValues.length));
            for (int i = 0; i < cookieValues.length; i++) {
                if (cookieValues[i].contains("; ")) {
                    cookieValues[i] = cookieValues[i].substring(0, cookieValues[i].indexOf("; "));
                }
            }
            System.out.println("Data now: " + Arrays.asList(cookieValues));
            return joinToString(cookieValues, "; ");
    }
}
