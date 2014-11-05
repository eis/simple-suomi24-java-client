package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.actions.EnterChatHandler;
import fi.eis.applications.chatapp.actions.LoginHandler;
import fi.eis.applications.chatapp.actions.RoomFetchHandler;
import fi.eis.applications.chatapp.ui.ChatUI;
import fi.eis.applications.chatapp.ui.LoginUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

                final LoginUI loginUI = LoginUI.createGUI(
                        new LoginHandler() {
                          @Override
                          public String tryLogin(String text, char[] password) {
                              System.out.println("Login for " + text);
                              return "dummy session value";
                          }
                      }, new EnterChatHandler() {
                            @Override
                            public void enterChat(String selectedRoomId, String sessionId) {
                                // Let's go!
                                ChatUI.createAndShowGUI(selectedRoomId, sessionId);
                            }
                      }, new RoomFetchHandler() {
                      });


                loginUI.display();
            }
        });

    }
}
