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
 * @url http://www.javapractices.com/topic/TopicAction.do?Id=231
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
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Hello");
        //panel.add(label);
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;

        c.ipady = 40;      //make this component tall
        c.weightx = 1.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1.0;  // when window is resized, this will consume all the space

        pane.add(label, c);//BorderLayout.PAGE_START);
        JTextField input = new JTextField();//"", 20);
        //JButton input = new JButton("Long-Named Button 4");
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

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 1;       //reset to default
        c.weighty = 0.0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;
        c.gridy = 1;

        /*
        panel = new JPanel();
        panel.add(input, c);
        pane.add(panel, c);//BorderLayout.PAGE_END);
        */

        pane.add(input, c);
    }

}
