package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;

public class App
{
    @Inject
    private LoginHandler loginHandler;
    
    @Inject
    private ChatEnterHandler enterChatHandler;

    @Inject
    private RoomsProvider roomsProvider;
    
    @Inject
    private ChattingConnectionFactory chatConnectionFactory;
    
    public void main(@SuppressWarnings("unused") @Observes ContainerInitialized event )
    {
        System.out.println( "Hello World!" );

        // Schedule a job for the event dispatching thread:
        // creating and showing this application's GUI.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                loginHandler.setDebug(false);
                final LoginUI loginUI = LoginUI.createGUI(
                        loginHandler,
                        enterChatHandler,
                        roomsProvider,
                        chatConnectionFactory
                    );

                loginUI.display();
            }
        });

    }
}
