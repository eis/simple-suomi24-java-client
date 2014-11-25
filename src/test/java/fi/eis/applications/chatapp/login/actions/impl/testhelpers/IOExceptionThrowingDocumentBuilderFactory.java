package fi.eis.applications.chatapp.login.actions.impl.testhelpers;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Creation Date: 12.11.2014
 * Creation Time: 19:02
 *
 * @author eis
 */
public class IOExceptionThrowingDocumentBuilderFactory extends DocumentBuilderFactory {
    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

        return new DocumentBuilder() {
            @Override
            public Document parse(InputSource is) throws SAXException, IOException {
                throw new IOException("this builder will always throw an exception");
            }

            @Override
            public boolean isNamespaceAware() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public boolean isValidating() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public void setEntityResolver(EntityResolver er) {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public void setErrorHandler(ErrorHandler eh) {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public Document newDocument() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public DOMImplementation getDOMImplementation() {
                throw new UnsupportedOperationException("not implemented");
            }
        };
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
