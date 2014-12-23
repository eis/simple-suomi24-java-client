package fi.eis.applications.chatapp.chat.actions.impl;


import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;
import fi.eis.applications.chatapp.chat.actions.MessageUpdater;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

public class Suomi24ChattingConnection extends SwingWorker<Void,String> implements ChattingConnection {
    
    private final int selectedRoomId;
    private final String sessionId;
    private final HTTPConnectionHandler httpHandler;
    private final SimpleLogger logger = new SimpleLogger(this.getClass());

    protected ConnectionParameters params = null;
    protected String chatConnectionUrl = null;
    protected MessageUpdater messageUpdater = null;

    public static class ConnectionParameters {
        private final String whoParam;
        private final String nickParam;
        private final String userIdParam;
        private final String cs;
        private final String uid;

        @Override
        public String toString() {
            return "ConnectionParameters{" +
                    "whoParam='" + whoParam + '\'' +
                    ", nickParam='" + nickParam + '\'' +
                    ", userIdParam='" + userIdParam + '\'' +
                    ", sessionString='" + cs + '\'' +
                    ", uid='" + uid + '\'' +
                    '}';
        }


        private ConnectionParameters(String whoParam, String userIdParam, String nickParam) {
            this.whoParam = whoParam;
            this.nickParam = nickParam;
            this.userIdParam = userIdParam;
            this.cs = null;
            this.uid = null;
        }
        private ConnectionParameters(ConnectionParameters params, String uid, String cs) {
            this.whoParam       = params.whoParam;
            this.nickParam      = params.nickParam;
            this.userIdParam    = params.userIdParam;
            this.cs             = cs;
            this.uid            = uid;
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
        public String getCs() {
            return this.cs;
        }
        public String getUid() {
            return this.uid;
        }
        static ConnectionParameters build(String whoParam, String nickParam, String userIdParam) {
            return new ConnectionParameters(whoParam, nickParam, userIdParam);
        }

        static ConnectionParameters build(
                ConnectionParameters params2, String uid, String cs) {
            return new ConnectionParameters(params2, uid, cs);
        }
    }


    public Suomi24ChattingConnection(int selectedRoomIdInput, String sessionIdInput,
            HTTPConnectionHandler httpHandlerInput) {
        this(selectedRoomIdInput, sessionIdInput,
            httpHandlerInput, LogLevel.ERROR);
    }
    public Suomi24ChattingConnection(int selectedRoomIdInput, String sessionIdInput,
            HTTPConnectionHandler httpHandlerInput, LogLevel logLevel) {
        this.selectedRoomId = selectedRoomIdInput;
        this.sessionId = sessionIdInput;
        this.httpHandler = httpHandlerInput;
        this.logger.setLogLevel(logLevel);
    }

    /* (non-Javadoc)
     * @see fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection#setUpdater(fi.eis.applications.chatapp.chat.actions.impl.MessageUpdater)
     */
    @Override
    public void setUpdater(final MessageUpdater messageUpdater) {
        this.messageUpdater = messageUpdater;
    }

    @Override
    protected Void doInBackground() throws Exception {
        final int AMOUNT_TO_READ_AT_A_TIME = 2048;
        URL url = new URL(chatConnectionUrl);
        HttpURLConnection connection = (HttpURLConnection) url
              .openConnection();
        long totalDataRead = 0;
        try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(
              connection.getInputStream())) {
           java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
           try (java.io.BufferedOutputStream bout = new BufferedOutputStream(
                 bos, AMOUNT_TO_READ_AT_A_TIME)) {
              byte[] data = new byte[AMOUNT_TO_READ_AT_A_TIME];
              int amountOfBytesRead;
              // read blocks until new data is available or end of stream reached (-1)
              while ((amountOfBytesRead = in.read(data, 0, AMOUNT_TO_READ_AT_A_TIME)) >= 0) {
                  totalDataRead = totalDataRead + amountOfBytesRead;
                  bout.write(data, 0, amountOfBytesRead);
                  publish(new String(data, 0, amountOfBytesRead, "ISO-8859-1"));
              }
           }
        }
        return null;
    }
    
    @Override
    protected void process(List<String> messages) {
        String message = concat(messages);
        logger.debug("Process! " + message);
        messageUpdater.publishMessage(message);
    }

    private String concat(List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (String message: messages) {
            builder.append(message);
        }
        return builder.toString();
    }
    /* (non-Javadoc)
     * @see fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection#getConnectionParameters()
     */
    @Override
    public ConnectionParameters getConnectionParameters() {
        return this.params;
    }

    final static Pattern firstPattern = Pattern.compile(
            ".*name=\"nick\" value=\"(.+?)\".*name=\"name\" value=\"(.+?)\".*name=\"who\" value=\"([a-f0-9]+?)\".*",
            Pattern.DOTALL);
    final static Pattern secondPattern = Pattern.compile(
            ".*top.window.location=\\('(.+?)'\\).*",
            Pattern.DOTALL);
    final static Pattern thirdPattern = Pattern.compile(
            ".+/chat/\\?uid=([0-9]+?)&cid=[0-9]+&cs=([0-9]+)",
            Pattern.DOTALL);
    final static Pattern fourthPattern = Pattern.compile(
            ".*var chatBodyUrl = '(.+?)';.*",
            Pattern.DOTALL);
    /* 
     * Implementation:
     * Connects in four steps.
     * 
     * <ul>
     *  <li>http://chat.suomi24.fi/login.cgi?cid=%d</li>
     *  <li>http://chat1.suomi24.fi:8080/login?cid=%d&nick=%s&name=%s&who=%s</li>
     *  <li>http://chat1.suomi24.fi:8080/chat/?uid=%d&cid=%d&cs=%d</li>
     *  <li>http://chat1.suomi24.fi:8080/body/?uid=%d&cid=%d&cs=%d</li>
     * </ul>
     * 
     * The last one will be run in a background thread by doInBackground()
     * 
     * @see fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection#connect()
     */
    @Override
    public void connect() {

        String url1 = String.format("http://chat.suomi24.fi/login.cgi?cid=%d", Integer.valueOf(selectedRoomId));

        try {
            String HTML1 = httpHandler.getHTMLFromURLWithCookie(url1, sessionId);
            logger.debug("Got HTML " + HTML1);
            Matcher matcher1 = firstPattern.matcher(HTML1);
            params = buildConnectionParameters(matcher1);

        } catch (IllegalStateException ex) {
            throw new IllegalStateException(
                    String.format("%s (using url %s, cookie %s)",
                            ex.getMessage(), url1, sessionId), ex);
            
        }
        logger.error("Got params (1): " + params);
        
        String url2 = String.format("http://chat1.suomi24.fi:8080/login?cid=%d&nick=%s&name=%s&who=%s",
                Integer.valueOf(selectedRoomId),
                params.getNick(),
                params.getUserId(),
                params.getWho());
        String HTML2 = httpHandler.getHTMLFromURLWithCookie(url2, sessionId);
        logger.debug("Got HTML " + HTML2);
        Matcher matcher2 = secondPattern.matcher(HTML2);
        if (!matcher2.matches()) {
            throw new IllegalStateException(
                    String.format("Failed to get proper content (using url %s, cookie %s)",
                            url2, sessionId));
        }
        String url3 = matcher2.group(1);
        Matcher matcher3 = thirdPattern.matcher(url3);
        logger.error("Got url (1): " + url3);
        if (!matcher3.matches()) {
            if (url3.contains("message=nickInUse")) {
                throw new IllegalStateException("Nick already in use");
            }
            throw new IllegalStateException(
                    String.format("Failed to get proper content (using url %s, cookie %s)",
                            url3, sessionId));
        }
        String uid = matcher3.group(1);
        String cs = matcher3.group(2);
        params = ConnectionParameters.build(params, uid, cs);
        logger.error("Got params (2): " + params);
        
        String HTML3 = httpHandler.getHTMLFromURLWithCookie(url3, sessionId);
        Matcher matcher4 = fourthPattern.matcher(HTML3);
        if (!matcher4.matches()) {
            throw new IllegalStateException(
                    String.format("Failed to get proper content (using url %s, cookie %s)",
                            url3, sessionId));
        }
        
        String url4 = matcher4.group(1);
        logger.error("Got url (2): " + url4);
        
        chatConnectionUrl = String.format("%s?uid=%s&cid=%s&cs=%s",
                url4,
                params.getUid(),
                Integer.valueOf(selectedRoomId),
                params.getCs()
                );
    }

    private ConnectionParameters buildConnectionParameters(Matcher matcher1) {
        final String whoParam;
        final String nickParam;
        final String userId;

        if (matcher1.matches()) {
            nickParam = matcher1.group(1);
            userId = matcher1.group(2);
            whoParam = matcher1.group(3);
            return ConnectionParameters.build(whoParam, nickParam, userId);
        }
        
        throw new IllegalStateException("Didn't get HTML containing the proper values");
    }
    @Override
    public void sendMessage(String text) {
        String baseUrl = chatConnectionUrl.replace("body/", "tell");
        String url = String.format("%s?uid=%s&cid=%s&cs=%s&ac=tell&who=kaikille&how=0&priv=false&baseTarget=empty&tl=%s&tell=%s",
                baseUrl,
                params.getUid(),
                String.valueOf(selectedRoomId),
                params.getCs(),
                text,
                text
                );
        logger.debug("Sending " + url);
        httpHandler.getHTMLFromURLWithCookie(url, sessionId);
    }

}
