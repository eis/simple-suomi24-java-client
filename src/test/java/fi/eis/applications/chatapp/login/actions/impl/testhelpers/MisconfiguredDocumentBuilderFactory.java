package fi.eis.applications.chatapp.login.actions.impl.testhelpers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Creation Date: 12.11.2014
 * Creation Time: 19:02
 *
 * @author eis
 */
public class MisconfiguredDocumentBuilderFactory extends DocumentBuilderFactory {
    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

        throw new ParserConfigurationException("this factory is not configured properly");
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        // no-op
    }

    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        // no-op
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        return false;
    }
}
