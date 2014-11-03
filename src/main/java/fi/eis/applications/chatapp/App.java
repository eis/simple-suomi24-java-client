package fi.eis.applications.chatapp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MinimalSwingApplication().buildAndDisplayGui();
            }
        });
    }
}
