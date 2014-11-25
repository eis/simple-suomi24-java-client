package fi.eis.applications.chatapp.chat.actions.impl;


import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChattingConnection {
    
    private final int selectedRoomId;
    private final String sessionId;
    private final HTTPConnectionHandler httpHandler;
    private final boolean debug = false;

    public static class ConnectionParameters {
        private String whoParam;

        @Override
        public String toString() {
            return "ConnectionParameters{" +
                    "whoParam='" + whoParam + '\'' +
                    ", nickParam='" + nickParam + '\'' +
                    ", userIdParam='" + userIdParam + '\'' +
                    '}';
        }

        private String nickParam;
        private String userIdParam;
        private ConnectionParameters(String whoParam, String userIdParam, String nickParam) {
            this.whoParam = whoParam;
            this.nickParam = nickParam;
            this.userIdParam = userIdParam;
        }
        public String getNick() {
            return this.nickParam;
        }
        public String getWho() {
            return this.whoParam;
        }
        public String getUserId() {
            return this.userIdParam;
        }
    }

    protected static ConnectionParameters buildConnectionParameters(String whoParam, String nickParam, String userIdParam) {
        return new ConnectionParameters(whoParam, nickParam, userIdParam);
    }

    public ChattingConnection(int selectedRoomIdInput, String sessionIdInput, HTTPConnectionHandler httpHandlerInput) {
        this.selectedRoomId = selectedRoomIdInput;
        this.sessionId = sessionIdInput;
        this.httpHandler = httpHandlerInput;
    }
    final static Pattern firstPattern = Pattern.compile(".*name=\"nick\" value=\"(.+?)\".*name=\"name\" value=\"(.+?)\".*name=\"who\" value=\"([a-f0-9]+?)\".*", Pattern.DOTALL);
    protected ConnectionParameters params = null;
    public ConnectionParameters getConnectionParameters() {
        return this.params;
    }

    public void connect() {
        final String whoParam;
        final String nickParam;
        final String userId;

        String url = String.format("http://chat.suomi24.fi/login.cgi?cid=%d", selectedRoomId);
        String HTML = httpHandler.getHTMLFromURLWithCookie(url, sessionId);
        debugPrint("Got HTML " + HTML);
        Matcher matcher  = firstPattern.matcher(HTML);

        if (matcher.matches()) {
            nickParam = matcher.group(1);
            userId = matcher.group(2);
            whoParam = matcher.group(3);
            params = buildConnectionParameters(whoParam, nickParam, userId);
        } else {
            throw new IllegalStateException(
                    String.format("Didn't get HTML containing the proper values (using url %s, cookie %s)",
                            url, sessionId));
        }
        debugPrint("Got params " + params);
    }

    private void debugPrint(String string) {
        if (debug) {
            System.out.println(string);
        }
    }

    public void setUpdater(MessageUpdater messageUpdater) {
        // TODO Auto-generated method stub
        
    }

}
