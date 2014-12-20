package fi.eis.applications.chatapp.login.actions.impl;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.actions.impl.testhelpers.IOExceptionThrowingDocumentBuilderFactory;
import fi.eis.applications.chatapp.login.actions.impl.testhelpers.MisconfiguredDocumentBuilderFactory;
import fi.eis.applications.chatapp.login.types.ChatRoom;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Creation Date: 9.11.2014
 * Creation Time: 14:15
 *
 * @author eis
 */
public class XMLBasedRoomsProviderTest {

    @Test
    public void testWithMissingFile() {
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-does-not-exist.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), containsString(targetFile));
            assertThat(ex.getMessage(), containsString(new File(".").getPath()));
        }
    }

    @Test
    public void testWithBrokenFile() {
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-is-broken.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), containsString(targetFile));
            assertThat(ex.getMessage(), containsString(new File(".").getPath()));
            assertThat(ex.getMessage(), anyOf(containsString("Parse"), containsString("parse")));
        }
    }
    @Test
    public void testWithWorkingFile() {
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        List<ChatRoom> rooms = sut.getRooms();
        assertThat(Integer.valueOf(rooms.size()), greaterThan(Integer.valueOf(5)));
    }

    @Test
    public void testUncastableDocumentBuilderConfiguration() {
        System.setProperty(DOC_BUILDER_PROPERTY_NAME, this.getClass().getCanonicalName());
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), anyOf(containsString("class"), containsString("Class")));
        }
    }
    @Test
    public void testUnfindableDocumentBuilderConfiguration() {
        System.setProperty(DOC_BUILDER_PROPERTY_NAME, "does/not/exist.class");
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), anyOf(containsString("configuration"), containsString("Configuration")));
        }
    }

    /* begin parser configuration problem test stuff */

    private final static String DOC_BUILDER_PROPERTY_NAME = "javax.xml.parsers.DocumentBuilderFactory";
    private String originalDocBuilderFactoryValue = null;

    /** store the value, since we touch it on some tests */
    @Before
    public void setUp() {
        originalDocBuilderFactoryValue = System.getProperty(DOC_BUILDER_PROPERTY_NAME);
    }

    /** ensure that we rest the status after a test */
    @After
    public void tearDown() {
        // setting to null will throw an exception
        // originally not set properties need to be cleared
        if (originalDocBuilderFactoryValue == null) {
            System.clearProperty(DOC_BUILDER_PROPERTY_NAME);
        } else {
            System.setProperty(DOC_BUILDER_PROPERTY_NAME, originalDocBuilderFactoryValue);
        }
    }
    @Test
    public void testParserConfigurationProblem() {
        System.setProperty(DOC_BUILDER_PROPERTY_NAME, MisconfiguredDocumentBuilderFactory.class.getCanonicalName());
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), containsString("ParserConfigurationException"));
        }
    }
    @Test
    public void testParserIOProblem() {
        System.setProperty(DOC_BUILDER_PROPERTY_NAME, IOExceptionThrowingDocumentBuilderFactory.class.getCanonicalName());
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/login/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile, LogLevel.NONE);
        try {
            sut.getRooms();
            fail("Line should not be reached");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), containsString("IOException"));
        }
    }
    /* end parser configuration problem test stuff */
}
