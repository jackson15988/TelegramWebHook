package telegram.dto;

public class MessageDto {

    private String message;

    private String PictureReplyMessage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPictureReplyMessage() {
        return PictureReplyMessage;
    }

    public void setPictureReplyMessage(String pictureReplyMessage) {
        PictureReplyMessage = pictureReplyMessage;
    }


}
