package fi.eis.applications.chatapp.login.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import javax.swing.SwingWorker;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.types.ChatRoom;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Date: 4.11.2014
 * Time: 20:16
 * @author eis
 */
public class LoginUI extends JFrame {

    protected static final String LOGINWINDOW_TITLE = "";

    protected final SimpleLogger logger = new SimpleLogger(this.getClass());

    protected LoginHandler loginHandler;
    protected ChatEnterHandler chatEnterHandler;
    protected RoomsProvider roomFetchHandler;
    protected ChattingConnectionFactory chatConnectionFactory;

    protected String sessionCookie;
    protected JList<ChatRoom> chatRoomList;
    protected JList<String> userList;
    
    protected JButton enterChatButton;

    protected LoginUI() {

    }

    protected LoginUI(LoginHandler loginHandler, ChatEnterHandler chatEnterHandler,
                    RoomsProvider roomFetchHandler, ChattingConnectionFactory chatConnectionFactory) {
        this.loginHandler = loginHandler;
        this.chatEnterHandler = chatEnterHandler;
        this.roomFetchHandler = roomFetchHandler;
        this.chatConnectionFactory = chatConnectionFactory;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(LOGINWINDOW_TITLE);
    }

    public static LoginUI createGUI(LoginHandler loginHandler, ChatEnterHandler chatEnterHandler,
                                    RoomsProvider roomFetchHandler, ChattingConnectionFactory chatConnectionFactory) {
        if ((loginHandler == null) || (chatEnterHandler == null) ||
                (roomFetchHandler == null) || (chatConnectionFactory == null)) {
            throw new IllegalStateException(String.format("initialization has failed [%s,%s,%s,%s]",
                loginHandler, chatEnterHandler , roomFetchHandler , chatConnectionFactory));
        }
        LoginUI frame = new LoginUI(loginHandler, chatEnterHandler,
                roomFetchHandler, chatConnectionFactory);

        frame.add(frame.createLoginPanel());

        return frame;
    }

    public static LoginUI createGUI(LoginHandler loginHandler,
            ChatEnterHandler chatEnterHandler, RoomsProvider roomFetchHandler,
            ChattingConnectionFactory chatConnectionFactory, LogLevel logLevel) {
        LoginUI frame = createGUI(loginHandler, chatEnterHandler, roomFetchHandler, chatConnectionFactory);
        frame.logger.setLogLevel(logLevel);
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

    protected JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createLoginHeader(), BorderLayout.NORTH);
        panel.add(createRoomSelector(), BorderLayout.CENTER);
        panel.add(createLoginFooter(), BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(600, 600));
        return panel;
    }

    protected JPanel createLoginFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        this.enterChatButton = new JButton("Chat!");
        this.enterChatButton.setEnabled(false);
        this.enterChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChattingConnection connection = chatConnectionFactory.get(
                        LoginUI.this.getSelectedRoomId(),
                        LoginUI.this.sessionCookie
                        );
                LoginUI.this.chatEnterHandler.enterChat(connection);
            }
        });
        panel.add(this.enterChatButton);
        return panel;
    }

    protected int getSelectedRoomId() {
        return this.chatRoomList.getSelectedValue().getRoomId();
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
            Collections.sort(rooms, ChatRoom.ROOM_NAME_COMPARATOR);

            for(ChatRoom chatRoom: rooms)
                listModel.addElement(chatRoom);

            this.chatRoomList = new JList<ChatRoom>(listModel);
            chatRoomList.setCellRenderer(new ChatRoomCellRenderer());
            chatRoomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            chatRoomList.setSelectedIndex(0);
            roomListScrollPane = new JScrollPane(chatRoomList);
        }

        JScrollPane roomMemberListScrollPane;
        {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            listModel.addElement("user1");
            listModel.addElement("user2");
            listModel.addElement("user3");

            this.userList = new JList<>(listModel);
            userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            roomMemberListScrollPane = new JScrollPane(userList);
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


    private JTextField userNameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;

    private JPanel createLoginHeader() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new JLabel("Username: ", JLabel.CENTER));
        userNameTextField = new JTextField(15);
        panel.add(userNameTextField);
        passwordLabel = new JLabel("Password: ", JLabel.CENTER);
        panel.add(passwordLabel);
        passwordField = new JPasswordField(15);
        panel.add(passwordField);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loginButtonPressed();
                }
            }
        );
        panel.add(loginButton);
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(panel, BorderLayout.EAST);

        return containerPanel;
    }

    void loginButtonPressed() {
        boolean userWantsToLogIn = !enterChatButton.isEnabled();
        if (userWantsToLogIn) {
            setLoadingStateWithText("Logging in...");
            // trying to log in
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

                @Override
                protected String doInBackground() throws Exception {
                    logger.debug("doInBackGround");
                    return loginHandler.tryLogin(
                            userNameTextField.getText(),
                            passwordField.getPassword());
                }
                @Override
                public void done() {
                    logger.debug("done");
                    try {
                        sessionCookie = get();
                        setLoggedInState();
                        // exceptions should be passed though doInBackground() and handled here
                        // http://stackoverflow.com/questions/6523623/gracefull-exception-handling-in-swing-worker
                    } catch (InterruptedException e) {
                        handleException(e);
                    } catch (ExecutionException e) {
                        handleException(e.getCause());
                    }
                }
                
            };
            worker.execute();

        } else {
            resetLoginStatus();
        }
        logger.debug("LoginUI Session cookie: " + sessionCookie);
        LoginUI.this.setTitle(LOGINWINDOW_TITLE);
    }

    protected void handleException(Throwable t) {
        logger.debug("Handling exception for " + t);
        this.setTitle(
                (LOGINWINDOW_TITLE.length() > 0)
                    ? String.format("%s - %s", LOGINWINDOW_TITLE, t.getMessage())
                    : t.getMessage()
            );
        logger.debug("this.getTitle " + this.getTitle());
        resetLoginStatus();
    }

    protected void resetLoginStatus() {
        sessionCookie = null;

        // enabling login controls
        userNameTextField.setEnabled(true);
        passwordLabel.setVisible(true);
        passwordField.setVisible(true);
        loginButton.setText("Login");
        enterChatButton.setEnabled(false);
        loginButton.setEnabled(true);
    }

    protected void setLoadingStateWithText(String msg) {
        userNameTextField.setEnabled(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        loginButton.setText(msg);
        loginButton.setEnabled(false);
    }

    private void setLoggedInState() {
        loginButton.setText("Logout");
        loginButton.setEnabled(true);
        enterChatButton.setEnabled(true);
    }

}
