package fi.eis.applications.chatapp.chat.actions.impl;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;
import fi.eis.libraries.di.Inject;

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
    public ChattingConnection get(String selectedRoomId, String sessionCookie,
            String textualRoomIdentifier) {
        return new Suomi24ChattingConnection(selectedRoomId, sessionCookie,
                textualRoomIdentifier, httpHandler);
    }

    @Override
    public String toString() {
        return "Suomi24ChattingConnectionFactoryImpl{" +
                "httpHandler=" + httpHandler +
                '}';
    }
}
