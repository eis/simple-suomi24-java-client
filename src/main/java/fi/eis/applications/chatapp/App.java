package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.di.Context;
import fi.eis.applications.chatapp.di.DependencyInjection;
import fi.eis.applications.chatapp.di.Inject;
import fi.eis.applications.chatapp.di.Module;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;

import javax.swing.*;

public class App implements Runnable
{
    @Inject
    private LoginHandler loginHandler;

    @Inject
    private ChatEnterHandler enterChatHandler;

    @Inject
    private RoomsProvider roomsProvider;
    
    @Inject
    private ChattingConnectionFactory chatConnectionFactory;

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
        /*
        Module mSuppliers = DependencyInjection.classes(
            DefaultHTTPConnectionImpl.class,
            Suomi24LoginHandler.class,
            XMLBasedRoomsProvider.class,
            Suomi24ChattingConnectionFactoryImpl.class,
            MessageUpdaterImpl.class,
            ChatEnterHandler.class
        );
        Module mClasses = DependencyInjection.classes(App.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        */
        Context diContext = DependencyInjection.classScanningContext(App.class);
        return diContext.get(App.class);
    }

    @Override
    public void run() {
        System.out.println( "Hello World!" );
        System.out.println( "[1] this=" + this );

        // Schedule a job for the event dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println( "[2] this=" + this );

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
