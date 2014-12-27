package fi.eis.applications.chatapp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fi.eis.applications.chatapp.chat.ui.ChatUI;
import fi.eis.applications.chatapp.configuration.Configuration;
import fi.eis.libraries.di.Context;
import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.Inject;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

public class App implements Runnable
{
    private final ChatUI chatUI;
    private final Configuration configuration;

    @Inject
    public App(ChatUI chatUI, Configuration configuration) {
        this.chatUI = chatUI;
        this.configuration = configuration;
    }



    @Override
    public String toString() {
        return "App [chatUI=" + chatUI + ", configuration=" + configuration
                + "]";
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

                chatUI.setLogLevel(LogLevel.DEBUG);
                chatUI.display();
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
