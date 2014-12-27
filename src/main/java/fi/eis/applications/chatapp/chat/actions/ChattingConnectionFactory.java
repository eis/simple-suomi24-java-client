package fi.eis.applications.chatapp.chat.actions;

public interface ChattingConnectionFactory {
    public ChattingConnection get(String selectedRoomId, String sessionCookie,
            String textualRoomIdentifier);
}
