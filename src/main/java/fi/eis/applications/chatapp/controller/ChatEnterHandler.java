package fi.eis.applications.chatapp.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fi.eis.applications.chatapp.App;
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
                try {
                ChatUI.createAndShowGUI(chattingConnection,
                        configuration);
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Exception: " + ex.getMessage());
                    App.main(null);
                }
            }
        });

    }
}
