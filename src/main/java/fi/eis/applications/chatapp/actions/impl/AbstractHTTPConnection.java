package fi.eis.applications.chatapp.actions.impl;

import fi.eis.applications.chatapp.actions.HTTPConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Creation Date: 23.11.2014
 * Creation Time: 22:12
 *
 * @author eis
 */
public abstract class AbstractHTTPConnection implements HTTPConnectionHandler {
    protected String readInputStream(final InputStream in)  {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            in.close();
            return result.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    protected String readResponseContent(HttpURLConnection con)  {
        try {
            return readInputStream(con.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    protected static String joinToString(String[] aArr, String sSep) {
        if (aArr == null || (aArr.length == 0)) {
            return "";
        }
        StringBuilder sbStr = new StringBuilder(aArr[0]);
        for (int i = 1, il = aArr.length; i < il; i++) {
            sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}
