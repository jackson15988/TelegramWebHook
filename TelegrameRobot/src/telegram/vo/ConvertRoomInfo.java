package telegram.vo;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import telegram.dto.ChatDto;

public class ConvertRoomInfo {

    public static ChatDto getRoomInfo(Update update) {
        Chat chat = update.getMessage().getChat();
        ChatDto data = new ChatDto();
        data.setChatId(chat.getId().toString());
        data.setChatRoomName(chat.getTitle());
        return data;
    }

    ;
}
