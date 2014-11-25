package fi.eis.applications.chatapp.login.actions.impl;

import fi.eis.applications.chatapp.chat.actions.impl.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.impl.DefaultHTTPConnectionImpl;
import fi.eis.applications.chatapp.chat.ui.ChatUI;
import fi.eis.applications.chatapp.login.actions.EnterChatHandler;

import javax.swing.*;

/**
 * User: eis
 * Creation Date: 8.11.2014
 * Creation Time: 16:55
 */
public class DefaultEnterChatHandler implements EnterChatHandler {
    @Override
    public void enterChat(final int selectedRoomId, final String sessionId) {
        // Let's go!
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatUI.createAndShowGUI(new ChattingConnection(selectedRoomId, sessionId, new DefaultHTTPConnectionImpl()) );
            }
        });
    }
}
