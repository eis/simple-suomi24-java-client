package fi.eis.applications.chatapp.login.actions;

import java.util.List;

import fi.eis.applications.chatapp.login.types.ChatRoom;

/**
 * Creation Date: 8.11.2014
 * Creation Time: 18:41
 * 
 * @author eis
 */
public interface RoomsProvider {
    List<ChatRoom> getRooms();
}
