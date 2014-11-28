package fi.eis.applications.chatapp.controller;

import javax.swing.SwingUtilities;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.ui.ChatUI;

public class ChatEnterHandler {
    

    public void enterChat(final ChattingConnection chattingConnection) {
        // Let's go!
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatUI.createAndShowGUI(chattingConnection);
            }
        });
    }
}
