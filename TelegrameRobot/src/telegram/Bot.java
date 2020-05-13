package telegram;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.alibaba.fastjson.JSONObject;

import telegram.util.TextConversion;

public class Bot extends TelegramLongPollingBot {
	Socket socket = null;

	@Override
	public void onUpdateReceived(Update update) {
		System.out.println(update.getMessage().getFrom().getFirstName() + "#: " + update.getMessage().getText());

		if (socket == null) {
			try {
				socket = new Socket("45.32.49.87", 9877);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (update != null && update.getMessage().getText() != null && update.getMessage().getText().contains("📡")) {
	
			String message = TextConversion.vip240Signal(update.getMessage().getText());
//			String message = update.getMessage().getText();
			
//			//自己群
			LineNotification.callEvent("cNWEW5pf8tkvmytyhkeAh28Hmj82krq6PnxgDy3iYGG", message);
//			//M大群
			LineNotification.callEvent("nVxs1v7eFKEKXXV4rPsLnU4LzHLmhtqS4X3ZNbvPDD5", message);


			try {

				
				StringBuilder sb = new StringBuilder();
				InputStream is = new ByteArrayInputStream(message.getBytes());
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));

				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

				// 寫資訊給客戶端
				String line = reader.readLine();
				while (!"end".equalsIgnoreCase(line) && !"null".equals(line) && line != null) {

					JSONObject obj = TextConversion.convertorderInformation(update.getMessage().getText());
					if (obj != null && !obj.isEmpty()) {
						out.println(obj.toJSONString());
						out.flush();
						// 將從鍵盤獲取的資訊給到伺服器
						// 顯示輸入的資訊
						line = reader.readLine();
					} else {
						break;
					}
				}
//				out.close();
//				in.close();
//				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
//		SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
//		sendMessage.setText(
//				"Hello " + update.getMessage().getFrom().getFirstName() + "\n" + update.getMessage().getText());
//		try {
//			sendMessage(sendMessage);
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	};

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "889507584:AAHsoTN22rdIznIVre6MZI05cPT47AuoZEs";
//		return "967307466:AAEOhsdpXtIQWeLx8pHJbiDzw3VEFZKxQpM";
	}

	private static boolean isjson(String string) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
