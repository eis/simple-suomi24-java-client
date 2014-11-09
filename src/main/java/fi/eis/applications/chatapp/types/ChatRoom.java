package fi.eis.applications.chatapp.types;

/**
 * User: eis
 * Creation Date: 8.11.2014
 * Creation Time: 18:46
 */
public class ChatRoom {
    private final int roomId;
    private final String roomName;

    public ChatRoom(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public String toString() {
        return String.format("%s[roomId=%s, roomName=%s]",
                this.getClass().getSimpleName(),
                String.valueOf(this.roomId),
                this.roomName);
    }

    public int getRoomId() {
        return this.roomId;
    }
    public String getRoomName() {
        return this.roomName;
    }
}
