package fi.eis.applications.chatapp.chat.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import fi.eis.applications.chatapp.chat.types.User;

public class UserListPanel extends JPanel {
    private final DefaultListModel<User> userList = createUserList();
    private UserListPanel(LayoutManager2 layout) {
        super(layout);
    }
    private UserListPanel(LayoutManager layout) {
        super(layout);
    }
    private static class UserCellRenderer extends JLabel implements ListCellRenderer<User> {

        // default renderer should not be created within the method for performance reasons
        // source: http://www.java2s.com/Tutorial/Java/0240__Swing/AddyourownListCellRenderer.htm
        protected final DefaultListCellRenderer defaultRenderer;

        public UserCellRenderer() {
            defaultRenderer = new DefaultListCellRenderer();
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User value,
                int index, boolean isSelected, boolean cellHasFocus) {
            // we prefer standard behaviour for colors etc
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            // only thing we want to change is the display text
            renderer.setText(value.getNick());
            return renderer;
        }
    }

    static UserListPanel createUserListPanel() {
        
        UserListPanel userListPanel = new UserListPanel(new GridLayout(1, 0));
        JList<User> userList = new JList<>(userListPanel.getUserList());
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new UserCellRenderer());
        JScrollPane userListScrollPane = new JScrollPane(userList);
        userListScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        userListScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        userListScrollPane.setMinimumSize(new Dimension(10, 10));


        userListPanel.add(userListScrollPane);
        userListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("User list"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return userListPanel;
    }
    private static DefaultListModel<User> createUserList() {
        DefaultListModel<User> listModel = new DefaultListModel<>();
        return listModel;
    }
    public DefaultListModel<User> getUserList() {
        return userList;
    }
}
