package fi.eis.applications.chatapp.chat.actions.impl;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;
import fi.eis.applications.chatapp.di.Inject;

public class Suomi24ChattingConnectionFactoryImpl implements ChattingConnectionFactory {

    private HTTPConnectionHandler httpHandler;

    @Inject
    public Suomi24ChattingConnectionFactoryImpl(HTTPConnectionHandler httpHandler) {
        if (httpHandler == null) {
            throw new IllegalStateException("httpHandler should be initialized");
        }
        this.httpHandler = httpHandler;
    }

    @Override
    public ChattingConnection get(int selectedRoomId, String sessionCookie) {
        return new Suomi24ChattingConnection(selectedRoomId, sessionCookie, httpHandler);
    }

    @Override
    public String toString() {
        return "Suomi24ChattingConnectionFactoryImpl{" +
                "httpHandler=" + httpHandler +
                '}';
    }
}
