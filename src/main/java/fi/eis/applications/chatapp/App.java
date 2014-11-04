package fi.eis.applications.chatapp;

import javax.swing.*;
import java.awt.event.ActionEvent;

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

                final LoginUI loginUI = LoginUI.createAndShowGUI();

                loginUI.addSuccessfulLoginHandler(new LoginHandler() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loginUI.dispose();
                        // Let's go!
                        ChatUI.createAndShowGUI();
                    }
                });

            }
        });

    }
}
