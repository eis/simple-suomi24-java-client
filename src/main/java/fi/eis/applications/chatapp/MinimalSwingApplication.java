package fi.eis.applications.chatapp;

/**
 * Created with IntelliJ IDEA.
 * User: Blackstorm
 * Date: 1.11.2014
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Simple harness for testing GUI code.
 * To use this class, edit the code to suit your needs.
 *
 * @see http://www.javapractices.com/topic/TopicAction.do?Id=231
 */
public final class MinimalSwingApplication {

    /**
     * Build and display minimal GUI.
     * <p/>
     * <P>The GUI has a label and an OK button.
     * The OK button launches a simple message dialog.
     * No menu is included.
     */

    public void buildAndDisplayGui() {
        JFrame frame = new JFrame("Test Frame");
        buildContent(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void buildContent(final Container pane) {
        //pane.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Hello"));
        pane.add(panel, BorderLayout.PAGE_START);
        JTextField input = new JTextField("", 20);
        //input.setPreferredSize();
        /*
        ok.addActionListener(new ActionListener(){
            private volatile boolean alreadyDisposed = false;
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                if (aFrame.isDisplayable()) {
                    alreadyDisposed = true;
                    aFrame.dispose();
                }
            }
        });
        */
        panel = new JPanel();
        panel.add(input);
        pane.add(panel, BorderLayout.PAGE_END);
    }

}
