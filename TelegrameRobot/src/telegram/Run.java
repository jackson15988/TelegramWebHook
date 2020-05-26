package telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Run {

	public static void main(String[] args) {
	
		ApiContextInitializer.init();
		System.out.println("開始進行Telegram_webHook程式");
		TelegramBotsApi teleframBotsApi = new TelegramBotsApi();
		Bot bot = new Bot();
		
			
		try {
			teleframBotsApi.registerBot(bot);		
		} catch (TelegramApiRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
