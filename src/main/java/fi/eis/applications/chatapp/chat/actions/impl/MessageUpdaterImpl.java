package fi.eis.applications.chatapp.chat.actions.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import fi.eis.applications.chatapp.chat.actions.MessageUpdater;
import fi.eis.applications.chatapp.chat.types.User;

public class MessageUpdaterImpl implements MessageUpdater {

    private final JEditorPane messagePane;
    private final DefaultListModel<User> userList;
    private final String roomName;
    
    private boolean logToFile = false;
    private OutputStream logStream = null;
    private String logFileName;
    
    public MessageUpdaterImpl(JEditorPane messagesTarget, DefaultListModel<User> defaultListModel,
            String roomName) {
        if (messagesTarget == null) {
            throw new IllegalArgumentException("message target must be defined");
        }
        if (defaultListModel == null) {
            throw new IllegalArgumentException("user list target must be defined");
        }
        this.messagePane = messagesTarget;
        this.userList = defaultListModel;
        this.roomName = roomName;
    }
    
    // http://www.java2s.com/Code/JavaAPI/javax.swing.text/DocumentinsertStringintoffsetStringstrAttributeSeta.htm
    // http://stackoverflow.com/questions/5133240/add-html-content-to-document-associated-with-jtextpane
    @Override
    public void publishMessage(String message) {
        handleUserEntries(message);
        HTMLDocument doc = (HTMLDocument)messagePane.getDocument();
        HTMLEditorKit kit = (HTMLEditorKit)messagePane.getEditorKit();
        try {
            kit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
            logToFile(message);
        } catch (BadLocationException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public void setLogToFile(boolean value) {
        this.logToFile = value;
    }

    private void logToFile(String message) {
        if (!logToFile) {
            return;
        }
        try {
            if (logStream == null) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                
                Files.createDirectories(Paths.get("logs")); // no exception if already exists

                this.logFileName = String.format("logs/%s-%s.html",
                        date, this.roomName);
                Path file = Paths.get(this.logFileName);
                // if log file name is already used, we start counting
                // and use the first one available
                int counter = 1;
                while (Files.exists(file)) {
                    this.logFileName = String.format("logs/%s-%s-%d.html",
                            date, this.roomName, Integer.valueOf(++counter));
                    file = Paths.get(this.logFileName);
                }
                Files.createFile(file);
                logStream = Files.newOutputStream(file);
            }
            logStream.write(message.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    //TODO handle exit: <script>parent.loadLoginpage('exit', 'currentnick',
    //  '&cid=%sgid=%s&uid=%s&cs=%s');</script>
    final static Pattern userAddPattern = Pattern.compile(
            "parent.user_add\\(new parent.User\\('', '(.+?)', ([0-9]), '(.+?)', ., ., .\\)\\);",
            Pattern.DOTALL);
    final static Pattern userRemovePattern = Pattern.compile(
            "user_remove\\('(.+?)'\\);",
            Pattern.DOTALL);
    private void handleUserEntries(String message) {
        Matcher matcher = userAddPattern.matcher(message);
        while (matcher.find()) {
            addUser(new User(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
        matcher = userRemovePattern.matcher(message);
        while (matcher.find()) {
            removeUser(matcher.group(1));
        }
    }

    private void addUser(User user) {
        this.userList.addElement(user);
    }
    private void removeUser(String userNick) {
        Enumeration<User> users = this.userList.elements();
        while(users.hasMoreElements()) {
            User user = users.nextElement();
            if (userNick.equals(user.getNick())) {
                this.userList.removeElement(user);
                return;
            }
        }
    }

    @Override
    public void close() {
        if (this.logStream != null) {
            try {
                this.logStream.flush();
                this.logStream.close();
                this.logStream = null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
