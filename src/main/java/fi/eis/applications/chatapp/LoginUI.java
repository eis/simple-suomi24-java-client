package fi.eis.applications.chatapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: eis
 * Date: 4.11.2014
 * Time: 20:16
 */
public class LoginUI extends JFrame {

    private LoginHandler successfulLoginHandler;
    private JButton loginButton;

    public static LoginUI createGUI() {
        LoginUI frame = new LoginUI();

        frame.add(frame.createLoginPanel());

        return frame;
    }

    public void display() {
        // pack makes size correspond to content
        pack();
        // Center the window
        setLocationRelativeTo(null);
        // Show
        setVisible(true);
    }

    public void addSuccessfulLoginHandler(LoginHandler successfulLoginHandler) {
        loginButton.addActionListener(successfulLoginHandler);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        //panel.setPreferredSize(new Dimension(500, 500));
        panel.add(createLoginHeader(), BorderLayout.NORTH);
        panel.add(createRoomSelector(), BorderLayout.CENTER);
        panel.add(createLoginFooter(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLoginFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        loginButton = new JButton("Login");
        panel.add(loginButton);
        return panel;
    }

    private static Component createRoomSelector() {
        JScrollPane roomListScrollPane;
        {
            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement("Seurahuone");
            listModel.addElement("Hautausmaa");
            listModel.addElement("Helsinki");

            JList list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            roomListScrollPane = new JScrollPane(list);
        }

        JScrollPane roomMemberListScrollPane;
        {
            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement("user1");
            listModel.addElement("user2");
            listModel.addElement("user3");

            JList list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            roomMemberListScrollPane = new JScrollPane(list);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(roomListScrollPane, BorderLayout.WEST);
        panel.add(roomMemberListScrollPane, BorderLayout.EAST);
        return panel;
    }


    private JPanel createLoginHeader() {
        JPanel panel = new JPanel();
        return panel;
    }
}
