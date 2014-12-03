package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import fi.eis.libraries.di.Context;
import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.Inject;

import javax.swing.*;

public class App implements Runnable
{
    private LoginHandler loginHandler;
    private ChatEnterHandler enterChatHandler;
    private RoomsProvider roomsProvider;
    private ChattingConnectionFactory chatConnectionFactory;

    @Inject
    public App(LoginHandler loginHandler,
               ChatEnterHandler enterChatHandler,
               RoomsProvider roomsProvider,
               ChattingConnectionFactory chatConnectionFactory) {
        this.loginHandler = loginHandler;
        this.enterChatHandler = enterChatHandler;
        this.roomsProvider = roomsProvider;
        this.chatConnectionFactory = chatConnectionFactory;
    }

    @Override
    public String toString() {
        return "App{" +
                "loginHandler=" + loginHandler +
                ", enterChatHandler=" + enterChatHandler +
                ", roomsProvider=" + roomsProvider +
                ", chatConnectionFactory=" + chatConnectionFactory +
                '}';
    }

    public static void main(String args[] )
    {
        App thisApp = createInjectedApp();
        thisApp.run();
    }

    public static App createInjectedApp() {
        Context diContext = DependencyInjection.deploymentUnitContext(App.class);
        return diContext.get(App.class);
    }

    @Override
    public void run() {
        System.out.println( "Hello World!" );
        System.out.println( "this=" + this );

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
