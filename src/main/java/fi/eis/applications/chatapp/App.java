package fi.eis.applications.chatapp;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.actions.impl.AbstractHTTPConnection;
import fi.eis.applications.chatapp.chat.actions.impl.DefaultHTTPConnectionImpl;
import fi.eis.applications.chatapp.chat.actions.impl.MessageUpdaterImpl;
import fi.eis.applications.chatapp.chat.actions.impl.Suomi24ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.impl.Suomi24ChattingConnectionFactoryImpl;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.actions.impl.Suomi24LoginHandler;
import fi.eis.applications.chatapp.login.actions.impl.XMLBasedRoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import org.boon.di.Context;
import org.boon.di.DependencyInjection;
import org.boon.di.Inject;
import org.boon.di.Module;
import org.boon.di.ProviderInfo;

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
        Module mSuppliers = DependencyInjection.suppliers(
                ProviderInfo.providerOf(DefaultHTTPConnectionImpl.class),
                ProviderInfo.providerOf(Suomi24LoginHandler.class),
                ProviderInfo.providerOf(XMLBasedRoomsProvider.class),
                ProviderInfo.providerOf(Suomi24ChattingConnectionFactoryImpl.class),
                ProviderInfo.providerOf(MessageUpdaterImpl.class),
                ProviderInfo.providerOf(ChatEnterHandler.class)
        );
        Module mClasses = DependencyInjection.classes(App.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
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
