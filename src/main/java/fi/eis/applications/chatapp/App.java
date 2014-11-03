package fi.eis.applications.chatapp;

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
                // Let's go!
                new AnotherSwingUI().createAndShowGUI();
            }
        });

    }
}
