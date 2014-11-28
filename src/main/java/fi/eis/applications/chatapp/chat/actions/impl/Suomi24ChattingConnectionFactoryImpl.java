package fi.eis.applications.chatapp.chat.actions.impl;

import javax.inject.Inject;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.actions.HTTPConnectionHandler;

public class Suomi24ChattingConnectionFactoryImpl implements ChattingConnectionFactory {

    private HTTPConnectionHandler httpHandler;
    
    @Inject
    public Suomi24ChattingConnectionFactoryImpl(HTTPConnectionHandler httpHandler) {
        this.httpHandler = httpHandler;
    }
    @Override
    public ChattingConnection get(int selectedRoomId, String sessionCookie) {
        return new Suomi24ChattingConnection(selectedRoomId, sessionCookie, httpHandler);
    }

    
}
