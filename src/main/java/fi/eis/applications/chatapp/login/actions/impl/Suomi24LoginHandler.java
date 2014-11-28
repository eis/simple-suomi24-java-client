package fi.eis.applications.chatapp.login.actions.impl;

import fi.eis.applications.chatapp.login.actions.LoginFailedException;
import fi.eis.applications.chatapp.login.actions.LoginHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: eis
 * Creation Date: 8.11.2014
 * Creation Time: 14:56
 */
public class Suomi24LoginHandler implements LoginHandler {

    private static final String httpsURL = "https://oma.suomi24.fi/index.php?q=/user/login";
    private static final String HEADER_SET_COOKIE = "Set-Cookie";
    private static final String S24_COOKIE_NAME = "S24auth";

    private boolean printDebug = false;

    @Override
    public void setDebug(boolean value) {
        this.printDebug = value;
    }

    @Override
    public String tryLogin(String userName, char[] password) throws LoginFailedException {
        try {

            String postBody = String.format("name=%s&pass=%s&form_build_id=form-16ac2b653fc2d785719c402adfc5800d&form_id=user_login&op=Kirjaudu+sis%%C3%%A4%%C3%%A4n",
                    URLEncoder.encode(userName,"UTF-8"),
                    URLEncoder.encode(String.valueOf(password),"UTF-8"));

            URL myurl = new URL(httpsURL);
            HttpsURLConnection con = null;
                con = (HttpsURLConnection)myurl.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Length", String.valueOf(postBody.length()));
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; java-chatapp)");
            con.setInstanceFollowRedirects(false);
            con.setDoOutput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());

            output.writeBytes(postBody);

            output.close();

            printDebug("Resp Code:" + con.getResponseCode());
            printDebug("Resp Message:" + con.getResponseMessage());

            // these have to be iterated: getHeaderField() only gives the first, and we want all cookies
            final Map<String,List<String>> headerFields = con.getHeaderFields();
            boolean loginSuccess = false;
            String cookieValue = null;

            for (Map.Entry<String, List<String>> entry: headerFields.entrySet()) {
                String headerName = entry.getKey();
                String headerValue = concatWithLineBreak(entry.getValue());
                printDebug("%s: %s%n", headerName, headerValue);
                if (HEADER_SET_COOKIE.equalsIgnoreCase(headerName)) {
                    cookieValue = headerValue;
                    if (headerValue.contains(S24_COOKIE_NAME)) {
                        loginSuccess = true;
                    }
                }
            }
            printDebug("Resp Cookie:" + cookieValue);

            if (!loginSuccess) {
                throw new LoginFailedException("login failed");
            }

            return cookieValue;
        } catch (IOException e) {
            throw new LoginFailedException(e);
        }
    }

    private void printDebug(final String message) {
        if (printDebug) {
            System.out.printf("%s Suomi24LoginHandler: DEBUG %s%n", new Date(), message);
        }
    }
    private void printDebug(final String message, String... params) {
        printDebug(String.format(message, (Object[])params));
    }

    private static String concatWithLineBreak(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            StringBuilder sb = new StringBuilder();
            boolean firstSkipped = false;
            sb.append(list.get(0));
            for (String str: list) {
                if (!firstSkipped) {
                    firstSkipped = true;
                } else {
                    sb.append("\r\n");
                    sb.append(str);
                }
            }
            return sb.toString();
        }
    }
}
