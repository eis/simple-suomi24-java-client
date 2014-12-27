package fi.eis.applications.chatapp.chat.actions.impl;

import java.util.regex.Matcher;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.eis.applications.chatapp.chat.actions.MessageUpdater;
import fi.eis.applications.chatapp.chat.types.User;

public class MessageUpdaterImplTest {
    
    @Mock
    private JEditorPane mockMessagePane;
    @Mock
    private DefaultListModel<User> mockListModel;
    @Mock
    private HTMLDocument mockHTMLDocument;
    @Mock
    private HTMLEditorKit mockHTMLEditorKit;
    
    private MessageUpdater sut;

    private static final String addOneUserString =
            "<script>parent.user_add(new parent.User('', 'eis', 2, 'http://mina.suomi24.fi/eis', 1, 0, 0));</script>";
    private static final String addMultipleUsersString =
            "<script>"+
            "parent.user_add(new parent.User('', 'eis', 2, 'http://mina.suomi24.fi/eis', 1, 0, 0));"+
            "parent.user_add(new parent.User('', 'testityyppi', 1, 'http://mina.suomi24.fi/testityyppi', 1, 0, 0));"+
            "parent.user_set_current('testityyppi');"+
            "</script>";
    private static final String removeOneUserString =
            "<script>user_remove('eis');</script>";
    private static final String removeMultipleUsersString =
            "<script>"+
            "user_remove('eis');"+
            "user_remove('testityyppi');"+
            "</script>"
            ;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockMessagePane.getEditorKit()).thenReturn(mockHTMLEditorKit);
        Mockito.when(mockMessagePane.getDocument()).thenReturn(mockHTMLDocument);
        sut = new MessageUpdaterImpl(mockMessagePane, mockListModel, "");
    }
    @Test
    public void testUserListUpdatedIfOneAdded() {
        sut.publishMessage(addOneUserString);
        Mockito.verify(mockListModel).addElement(new User("eis", "2", "http://mina.suomi24.fi/eis"));
    }
    @Test
    public void testUserAddRegExp() {
        
        Matcher m = MessageUpdaterImpl.userAddPattern.matcher(addOneUserString);
        Assert.assertTrue(m.find());
        Assert.assertEquals("eis", m.group(1));
        Assert.assertEquals("2", m.group(2));
        Assert.assertEquals("http://mina.suomi24.fi/eis", m.group(3));
    }
    @Test
    public void testMultipleUserAddRegExp() {
        Matcher m = MessageUpdaterImpl.userAddPattern.matcher(addMultipleUsersString);

        Assert.assertTrue(m.find());
        Assert.assertEquals("eis", m.group(1));
        Assert.assertEquals("2", m.group(2));
        Assert.assertEquals("http://mina.suomi24.fi/eis", m.group(3));
        
        Assert.assertTrue(m.find());
        Assert.assertEquals("testityyppi", m.group(1));
        Assert.assertEquals("1", m.group(2));
        Assert.assertEquals("http://mina.suomi24.fi/testityyppi", m.group(3));
    }
    @Test
    public void testUserRemoveRegExp() {
        Matcher m = MessageUpdaterImpl.userRemovePattern.matcher(removeOneUserString);
        Assert.assertTrue(m.find());
        Assert.assertEquals("eis", m.group(1));
    }
    @Test
    public void testMultipleUserRemoveRegExp() {
        Matcher m = MessageUpdaterImpl.userRemovePattern.matcher(removeMultipleUsersString);
        Assert.assertTrue(m.find());
        Assert.assertEquals("eis", m.group(1));
        Assert.assertTrue(m.find());
        Assert.assertEquals("testityyppi", m.group(1));
    }
}
