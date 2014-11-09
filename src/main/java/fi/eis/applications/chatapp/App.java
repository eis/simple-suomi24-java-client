package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.actions.LoginHandler;
import fi.eis.applications.chatapp.actions.impl.DefaultEnterChatHandler;
import fi.eis.applications.chatapp.actions.impl.DefaultRoomsProvider;
import fi.eis.applications.chatapp.actions.impl.Suomi24LoginHandler;
import fi.eis.applications.chatapp.ui.LoginUI;

import javax.swing.*;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        // Schedule a job for the event dispatching thread:
        // creating and showing this application's GUI.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                LoginHandler loginHandler = new Suomi24LoginHandler();
                loginHandler.setDebug(false);
                final LoginUI loginUI = LoginUI.createGUI(
                        loginHandler,
                        new DefaultEnterChatHandler(),
                        new DefaultRoomsProvider()
                    );

                loginUI.display();
            }
        });

    }
}
