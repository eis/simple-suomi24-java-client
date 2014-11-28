package fi.eis.applications.chatapp.chat.actions;

import fi.eis.applications.chatapp.chat.actions.impl.Suomi24ChattingConnection.ConnectionParameters;

public interface ChattingConnection {

    public abstract ConnectionParameters getConnectionParameters();

    public abstract void connect();

    public abstract void setUpdater(MessageUpdater messageUpdater);

}