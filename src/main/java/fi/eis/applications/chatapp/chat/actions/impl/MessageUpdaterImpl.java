package fi.eis.applications.chatapp.chat.actions.impl;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import fi.eis.applications.chatapp.chat.actions.MessageUpdater;

public class MessageUpdaterImpl implements MessageUpdater {

    private JEditorPane target;
    public MessageUpdaterImpl(JEditorPane messagesTarget) {
        if (messagesTarget == null) {
            throw new IllegalArgumentException("target must be defined");
        }
        this.target = messagesTarget;
    }
    
    // http://www.java2s.com/Code/JavaAPI/javax.swing.text/DocumentinsertStringintoffsetStringstrAttributeSeta.htm
    @Override
    public void publishMessage(String message) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, false);
        StyleConstants.setItalic(attributes, false);
        Document document = target.getDocument(); 
        try {
            document.insertString(
                    document.getLength(), message, attributes);
        } catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }

}
