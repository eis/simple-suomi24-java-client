package fi.eis.applications.chatapp.chat.actions;

public interface MessageUpdater {
    public void publishMessage(String message);

    public void setLogToFile(boolean value);

    public void close();
}