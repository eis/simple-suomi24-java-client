package fi.eis.applications.chatapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Blackstorm
 * Date: 4.11.2014
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class LoginUI extends JFrame {

    private LoginHandler successfulLoginHandler;
    private JButton loginButton;

    public static LoginUI createAndShowGUI() {
        LoginUI frame = new LoginUI();

        frame.add(frame.createLoginPanel());
        // pack makes size correspond to content
        frame.pack();
        // Center the window
        frame.setLocationRelativeTo(null);
        // Show
        frame.setVisible(true);

        return frame;
    }

    public void addSuccessfulLoginHandler(LoginHandler successfulLoginHandler) {
        loginButton.addActionListener(successfulLoginHandler);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        loginButton = new JButton("Login");
        panel.add(loginButton);
        return panel;
    }

}
