package fi.eis.applications.chatapp.ui;

import fi.eis.applications.chatapp.actions.EnterChatHandler;
import fi.eis.applications.chatapp.actions.LoginFailedException;
import fi.eis.applications.chatapp.actions.LoginHandler;
import fi.eis.applications.chatapp.actions.RoomFetchHandler;

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

    private LoginHandler loginHandler;
    private EnterChatHandler enterChatHandler;
    private RoomFetchHandler roomFetchHandler;

    private String sessionId;
    private String selectedRoomId;

    private JButton enterChatButton;

    private LoginUI() {

    }

    private LoginUI(LoginHandler loginHandler, EnterChatHandler enterChatHandler,
                    RoomFetchHandler roomFetchHandler) {
        this.loginHandler = loginHandler;
        this.enterChatHandler = enterChatHandler;
        this.roomFetchHandler = roomFetchHandler;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static LoginUI createGUI(LoginHandler loginHandler, EnterChatHandler enterChatHandler,
                                    RoomFetchHandler roomFetchHandler) {
        LoginUI frame = new LoginUI(loginHandler, enterChatHandler,
                roomFetchHandler);

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

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createLoginHeader(), BorderLayout.NORTH);
        panel.add(createRoomSelector(), BorderLayout.CENTER);
        panel.add(createLoginFooter(), BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(600, 600));
        return panel;
    }

    private JPanel createLoginFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        enterChatButton = new JButton("Chat!");
        enterChatButton.setEnabled(false);
        enterChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                enterChatHandler.enterChat(selectedRoomId, sessionId);
            }
        });
        panel.add(enterChatButton);
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
            roomMemberListScrollPane = new JScrollPane(list);
        }

        // Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                roomListScrollPane, roomMemberListScrollPane);
        splitPane.setOneTouchExpandable(true);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        roomListScrollPane.setMinimumSize(minimumSize);
        roomMemberListScrollPane.setMinimumSize(minimumSize);

        splitPane.setDividerLocation(200);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane);
        return panel;
    }


    private JPanel createLoginHeader() {
        //GridLayout gridLayout = new GridLayout(0, 5);
        //gridLayout.setHgap(2);
        //gridLayout.setVgap(2);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new JLabel("Username: ", JLabel.CENTER));
        final JTextField userNameTextField = new JTextField(15);
        panel.add(userNameTextField);
        final JLabel passwordLabel = new JLabel("Password: ", JLabel.CENTER);
        panel.add(passwordLabel);
        final JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);
        final JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enterChatButton.setEnabled(!enterChatButton.isEnabled());
                boolean userWantsToLogIn = enterChatButton.isEnabled();
                try {
                    if (userWantsToLogIn) {
                        sessionId = loginHandler.tryLogin(userNameTextField.getText(), passwordField.getPassword());
                        // cannot change credentials while logged in!
                        userNameTextField.setEnabled(false);
                        passwordLabel.setVisible(false);
                        passwordField.setVisible(false);
                        loginButton.setText("Logout");
                    } else {
                        // user can change these when logged out
                        userNameTextField.setEnabled(true);
                        passwordLabel.setVisible(true);
                        passwordField.setVisible(true);
                        loginButton.setText("Login");
                    }
                } catch (LoginFailedException e1) {
                    // TODO implement something better
                    throw new RuntimeException(e1);
                }
            }
        });
        panel.add(loginButton);
        panel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        JPanel containerPanel = new JPanel(new BorderLayout());
        //containerPanel.setPreferredSize(new Dimension(350, 30));
        containerPanel.add(panel, BorderLayout.EAST);

        return containerPanel;
    }
}
