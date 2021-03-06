package fi.eis.applications.chatapp.login.types;

import java.util.Comparator;

/**
 * Creation Date: 8.11.2014
 * Creation Time: 18:46
 * 
 * @author eis
 */
public class ChatRoom {
    private final String roomId;
    private final String roomName;

    public static final Comparator<? super ChatRoom> ROOM_NAME_COMPARATOR = new Comparator<ChatRoom>() {
        @Override
        public int compare(ChatRoom room1, ChatRoom room2) {
            return room1.getRoomName().compareTo(room2.getRoomName());
        }
    };

    public ChatRoom(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return String.format("%s[roomId=%s, roomName=%s]",
                this.getClass().getSimpleName(),
                String.valueOf(this.roomId),
                this.roomName);
    }

    public String getRoomId() {
        return this.roomId;
    }
    public String getRoomName() {
        return this.roomName;
    }
}
