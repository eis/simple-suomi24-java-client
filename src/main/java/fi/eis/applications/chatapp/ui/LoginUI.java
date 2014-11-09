package fi.eis.applications.chatapp.ui;

import fi.eis.applications.chatapp.actions.EnterChatHandler;
import fi.eis.applications.chatapp.actions.LoginFailedException;
import fi.eis.applications.chatapp.actions.LoginHandler;
import fi.eis.applications.chatapp.actions.RoomsProvider;
import fi.eis.applications.chatapp.types.ChatRoom;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: eis
 * Date: 4.11.2014
 * Time: 20:16
 */
public class LoginUI extends JFrame {

    private LoginHandler loginHandler;
    private EnterChatHandler enterChatHandler;
    private RoomsProvider roomFetchHandler;

    private String sessionCookie;
    private String selectedRoomId;

    private JButton enterChatButton;

    private LoginUI() {

    }

    private LoginUI(LoginHandler loginHandler, EnterChatHandler enterChatHandler,
                    RoomsProvider roomFetchHandler) {
        this.loginHandler = loginHandler;
        this.enterChatHandler = enterChatHandler;
        this.roomFetchHandler = roomFetchHandler;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static LoginUI createGUI(LoginHandler loginHandler, EnterChatHandler enterChatHandler,
                                    RoomsProvider roomFetchHandler) {
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
                enterChatHandler.enterChat(selectedRoomId, sessionCookie);
            }
        });
        panel.add(enterChatButton);
        return panel;
    }

    private class ChatRoomCellRenderer extends JLabel implements ListCellRenderer<ChatRoom> {

        // default renderer should not be created within the method for performance reasons
        // source: http://www.java2s.com/Tutorial/Java/0240__Swing/AddyourownListCellRenderer.htm
        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList<? extends ChatRoom> list, ChatRoom value, int index, boolean isSelected, boolean cellHasFocus) {
            // we prefer standard behaviour for colors etc
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            // only thing we want to change is the display text
            renderer.setText(value.getRoomName());
            return renderer;
        }
    }

    private Component createRoomSelector() {
        JScrollPane roomListScrollPane;
        {
            DefaultListModel<ChatRoom> listModel = new DefaultListModel<>();
            List<ChatRoom> rooms = this.roomFetchHandler.getRooms();

            for(ChatRoom chatRoom: rooms)
                listModel.addElement(chatRoom);

            JList list = new JList(listModel);
            list.setCellRenderer(new ChatRoomCellRenderer());
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
                        // trying to log in
                        sessionCookie = loginHandler.tryLogin(userNameTextField.getText(), passwordField.getPassword());
                        // disabling login controls while logged in
                        userNameTextField.setEnabled(false);
                        passwordLabel.setVisible(false);
                        passwordField.setVisible(false);
                        loginButton.setText("Logout");
                    } else {
                        // user wants to log out, resetting session cookie
                        sessionCookie = null;
                        // enabling login controls
                        userNameTextField.setEnabled(true);
                        passwordLabel.setVisible(true);
                        passwordField.setVisible(true);
                        loginButton.setText("Login");
                    }
                    System.out.println("LoginUI Session cookie: " + sessionCookie);
                } catch (LoginFailedException e1) {
                    // TODO implement something better
                    throw new RuntimeException(e1);
                }
            }
        });
        panel.add(loginButton);
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(panel, BorderLayout.EAST);

        return containerPanel;
    }
}
