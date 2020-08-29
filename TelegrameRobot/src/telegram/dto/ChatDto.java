package telegram.dto;

public class ChatDto {

    //房間id
    private String chatId;

    //房間類型
    private String ChatType;

    //房間名稱
    private String chatRoomName;


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatType() {
        return ChatType;
    }

    public void setChatType(String chatType) {
        ChatType = chatType;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "chatId='" + chatId + '\'' +
                ", ChatType='" + ChatType + '\'' +
                ", chatRoomName='" + chatRoomName + '\'' +
                '}';
    }
}
