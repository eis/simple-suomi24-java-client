package fi.eis.applications.chatapp.chat.actions;

public interface ChattingConnectionFactory {
    public ChattingConnection get(int selectedRoomId, String sessionCookie);
}
