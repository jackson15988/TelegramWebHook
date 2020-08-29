package telegram.vo;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import telegram.dto.ChatDto;
import telegram.dto.MessageDto;

public class ConvertRoomInfo {

    public static ChatDto getRoomInfo(Update update) {
        Chat chat = update.getMessage().getChat();
        ChatDto data = new ChatDto();
        data.setChatId(chat.getId().toString());
        data.setChatRoomName(chat.getTitle());
        return data;
    }


    public static MessageDto getMessageInfo(Update update) {
        Message mesObj = update.getMessage();
        MessageDto mesDto = new MessageDto();
        mesDto.setPictureReplyMessage(mesObj.getCaption());
        return mesDto;
    }

    ;;
}
