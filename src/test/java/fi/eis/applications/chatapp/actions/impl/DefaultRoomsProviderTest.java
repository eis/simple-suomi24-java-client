package fi.eis.applications.chatapp.actions.impl;

import fi.eis.applications.chatapp.actions.RoomsProvider;
import fi.eis.applications.chatapp.types.ChatRoom;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Creation Date: 9.11.2014
 * Creation Time: 14:15
 *
 * @author eis
 */
public class DefaultRoomsProviderTest {

    @Test
    public void testWithMissingFile() {
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/actions/impl/rooms-file-that-does-not-exist.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile);
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
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/actions/impl/rooms-file-that-is-broken.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile);
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
        String targetFile = "target/test-classes/fi/eis/applications/chatapp/actions/impl/rooms-file-that-works.xml";
        RoomsProvider sut = new XMLBasedRoomsProvider(targetFile);
        List<ChatRoom> rooms = sut.getRooms();
        assertThat(rooms.size(), greaterThan(5));
    }
}
