package telegram;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;

import telegram.util.MessageFilter;
import telegram.util.TextConversion;

public class Bot extends TelegramLongPollingBot {
	Socket socket = null;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage() != null) {
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

			List<PhotoSize> photos = update.getMessage().getPhoto();
			if (photos != null && photos.size() != 0) {
				System.out.println("接收到圖片-請稍後");
				PhotoSize photo = photos.get(photos.size() - 1);
				String id = photo.getFileId();
				try {
					GetFile getFile = new GetFile();
					getFile.setFileId(id);
					String filePath = getFile(getFile).getFileUrl(getBotToken());
					URL url = new URL(filePath);
					System.out.println(url);
				} catch (MalformedURLException e1) {
					System.out.println("轉換圖片上出現錯誤狀況:" + e1);
				} catch (TelegramApiException e) {
					System.out.println("轉換圖片上出現錯誤狀況:" + e);
				}
			}

			if (update != null && update.getMessage().getText() != null
					&& update.getMessage().getText().contains("📡")) {

				String message = TextConversion.vip240Signal(update.getMessage().getText());
//			String message = update.getMessage().getText();

//			//自己群
//			LineNotification.callEvent("cNWEW5pf8tkvmytyhkeAh28Hmj82krq6PnxgDy3iYGG", message);
				// M大群
//			LineNotification.callEvent("nVxs1v7eFKEKXXV4rPsLnU4LzHLmhtqS4X3ZNbvPDD5", message);

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

			// 處理外匯訊號
			if (update != null && update.getMessage().getText() != null
					&& MessageFilter.InstantProfitsFilter(update.getMessage().getText())) {
				String message = TextConversion.InstantProfitsReplce(update.getMessage().getText());

				System.out.println(message);
				replyResult(update, message);

				try {

					StringBuilder sb = new StringBuilder();
					InputStream is = new ByteArrayInputStream(message.getBytes());
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));

					PrintWriter out = new PrintWriter(socket.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

					// 寫資訊給客戶端
					String line = reader.readLine();
					while (!"end".equalsIgnoreCase(line) && !"null".equals(line) && line != null) {

						JSONObject obj = TextConversion.InstantProfitsJsobject(update.getMessage().getText());
//					if (obj != null && !obj.isEmpty()) {
						out.println(message);

						out.flush();
						// 將從鍵盤獲取的資訊給到伺服器
						// 顯示輸入的資訊
						line = reader.readLine();
						break;
					}
//				out.close();
//				in.close();
//				socket.close();
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}

			}
		}
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

	/**
	 * @author admin 機器人回復特定群組
	 * @param update
	 * @param message
	 * @return
	 */
	public boolean replyResult(Update update, String message) {

		SendMessage sendMessage = new SendMessage().setChatId("-1001377728083");
		String messages =
				  "💹Signal Alert\r\n" 
				+ "EURUSD - BUY NOW(市價執行) 1.410724\r\n"
				+ "Take Profit(止盈): 1.41015\r\n" 
				+ "Stop Loss(止損): 1.4040\r\n" + "\r\n" 
				+ "🚀 SHORT TERM TRAD" + "\r\n"
				+ "投资一定有风险，外汇投资有赚有赔，不得作为下单之依据。";

		sendMessage.setText(messages);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			System.out.println("回傳TG群時候，發生錯誤" + e);
		}
		return false;

	}

}
