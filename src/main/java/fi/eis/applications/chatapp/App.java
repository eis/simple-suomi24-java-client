package fi.eis.applications.chatapp;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.ui.ChatUI.ChatReturn;
import fi.eis.applications.chatapp.configuration.Configuration;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import fi.eis.libraries.di.Context;
import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.Inject;

public class App implements Runnable
{
    private LoginHandler loginHandler;
    private ChatEnterHandler enterChatHandler;
    private RoomsProvider roomsProvider;
    private ChattingConnectionFactory chatConnectionFactory;
    private Configuration configuration;

    @Inject
    public App(LoginHandler loginHandler,
               ChatEnterHandler enterChatHandler,
               RoomsProvider roomsProvider,
               ChattingConnectionFactory chatConnectionFactory,
               Configuration configuration) {
        this.loginHandler = loginHandler;
        this.enterChatHandler = enterChatHandler;
        this.roomsProvider = roomsProvider;
        this.chatConnectionFactory = chatConnectionFactory;
        this.configuration = configuration;
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
            @Override
            public void run() {

                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                final LoginUI loginUI = LoginUI.createGUI(
                        loginHandler,
                        enterChatHandler,
                        roomsProvider,
                        chatConnectionFactory
                );

                ChatReturn returnValue = ChatReturn.NORMAL_EXIT;
                do {
                    int result = JOptionPane.showConfirmDialog(null, loginUI, null, JOptionPane.OK_CANCEL_OPTION);
                    //ChattingConnection connection = loginUI.display();
                    if (result == JOptionPane.OK_OPTION) {
                        returnValue = enterChatHandler.enterChat(loginUI.getConnection());
                    }
                    System.out.println("returnValue: "+ returnValue);
                } while (returnValue != ChatReturn.NORMAL_EXIT); 
                
            }
        });
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    configuration.save();
                } catch (IllegalStateException e) {
                    e.printStackTrace(System.err);
                }
            }
        }, "Shutdown-thread"));
    }
}
