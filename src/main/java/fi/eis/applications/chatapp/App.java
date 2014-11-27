package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.impl.DefaultEnterChatHandler;
import fi.eis.applications.chatapp.login.actions.impl.Suomi24LoginHandler;
import fi.eis.applications.chatapp.login.actions.impl.XMLBasedRoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;

public class App
{
    @Inject
    LoginHandler loginHandler;

    public void main(@Observes ContainerInitialized event )
    {
        System.out.println( "Hello World!" );

        // Schedule a job for the event dispatching thread:
        // creating and showing this application's GUI.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                //LoginHandler loginHandler = new Suomi24LoginHandler();
                loginHandler.setDebug(false);
                final LoginUI loginUI = LoginUI.createGUI(
                        loginHandler,
                        new DefaultEnterChatHandler(),
                        new XMLBasedRoomsProvider()
                    );

                loginUI.display();
            }
        });

    }
}
