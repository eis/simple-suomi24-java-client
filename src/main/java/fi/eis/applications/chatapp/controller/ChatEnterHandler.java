package fi.eis.applications.chatapp.controller;

import javax.swing.SwingUtilities;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.ui.ChatUI;
import fi.eis.applications.chatapp.configuration.Configuration;
import fi.eis.libraries.di.Inject;

public class ChatEnterHandler {
    
    private final Configuration configuration;
    @Inject
    public ChatEnterHandler(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public void enterChat(final ChattingConnection chattingConnection) {
        // Let's go!
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChatUI.createAndShowGUI(chattingConnection,
                        configuration);
            }
        });
    }
}
