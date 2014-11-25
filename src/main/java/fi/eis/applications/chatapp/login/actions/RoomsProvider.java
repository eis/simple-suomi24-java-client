package fi.eis.applications.chatapp.login.actions;

import fi.eis.applications.chatapp.login.types.ChatRoom;

import java.util.List;

/**
 * User: eis
 * Creation Date: 8.11.2014
 * Creation Time: 18:41
 */
public interface RoomsProvider {
    List<ChatRoom> getRooms();
}
