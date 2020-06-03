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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import telegram.util.MessageFilter;
import telegram.util.OCRAsyncTask;
import telegram.util.RedisUtil;
import telegram.util.TextConversion;

public class Bot extends TelegramLongPollingBot {
	HashMap<Integer, JSONObject> binaryProfitSignalsMap = new HashMap<>();
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
					try {
						// 開始OCR
						String ocrStr = OCRAsyncTask.sendPost(true, url.toString(), "eng");
						JSONObject jsonObj = new JSONObject();
						JSONArray jsAry = new JSONArray();

						jsonObj = (JSONObject) jsonObj.parse(ocrStr);
//						jsAry = (JSONArray) jsonObj.get("ParsedResults");
//						jsonObj = (JSONObject) jsAry.get(0);
//
//						String LineStr = (String) jsonObj.get("ParsedText");
//						if (LineStr != null) {
//							// 如果裡面包含 .... 才做
//							jsonObj = (JSONObject) jsonObj.get("TextOverlay");
//							jsAry = (JSONArray) jsonObj.get("Lines");
//							
//							
//							String dir = "";
//							
//							for (Object object : jsAry) {
//								jsonObj = (JSONObject) jsonObj.parse(object.toString());
//								String lineText = (String) jsonObj.get("LineText");
//								if (lineText.toUpperCase().contains("BUY")) {
//
//								} else if (lineText.toUpperCase().contains("SELL")) {
//
//								}
//								System.out.println(object);
//							}
//
//						}

					} catch (Exception e) {
						System.out.println("辨別結果發生錯誤:" + e);
					}

				} catch (MalformedURLException e1) {
					System.out.println("轉換圖片上出現錯誤狀況:" + e1);
				} catch (TelegramApiException e) {
					System.out.println("轉換圖片上出現錯誤狀況:" + e);
				}
			}

			if (update != null && update.getMessage().getText() != null
					&& update.getMessage().getText().contains("📡")) {

				String message = TextConversion.vip240Signal(update.getMessage().getText());
				// String message = update.getMessage().getText();

				// //自己群
				// LineNotification.callEvent("cNWEW5pf8tkvmytyhkeAh28Hmj82krq6PnxgDy3iYGG",
				// message);
				// M大群
				// LineNotification.callEvent("nVxs1v7eFKEKXXV4rPsLnU4LzHLmhtqS4X3ZNbvPDD5",
				// message);

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
					// out.close();
					// in.close();
					// socket.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// 處理外匯訊號
			if (update != null && update.getMessage().getText() != null
					&& MessageFilter.InstantProfitsFilter(update.getMessage().getText())) {
				String message = TextConversion.InstantProfitsReplce(update.getMessage().getText());

				Integer messageID = update.getMessage().getMessageId();
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

					JSONObject obj = TextConversion.InstantProfitsJsobject(update.getMessage().getText(), messageID);
					if (obj != null && !obj.isEmpty()) {
						out.println(obj.toJSONString());
						out.flush();
						line = reader.readLine();
					}

				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}

			}

			// 處理外匯訊號修改InstantProfitsModifyFilter
			if (update != null && update.getMessage().getText() != null
					&& MessageFilter.InstantProfitsModifyFilter(update.getMessage().getText())) {
				Message messageObj = update.getMessage().getReplyToMessage();
				String message = update.getMessage().getText();

				JSONObject resultObj = new JSONObject();
				// 代表有

				try {

					if (messageObj != null) {

						StringBuilder sb = new StringBuilder();
						InputStream is = new ByteArrayInputStream(message.getBytes());
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));

						PrintWriter out = new PrintWriter(socket.getOutputStream());
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
						update.getMessage().getMessageId();

						// 寫資訊給客戶端
						String line = reader.readLine();

						Integer messageID = messageObj.getMessageId();
						JSONObject jsOBj = RedisUtil.getRedis(String.valueOf(messageID));

						String orderMagicNumber = (String) jsOBj.get("orderMagicNumber");
						String symbol = (String) jsOBj.get("symbol");
						String strategy = (String) jsOBj.get("strategy");

						String status = "1";

						// 先進行清空之前的資料
						jsOBj.clear();

						jsOBj.put("symbol", symbol);
						jsOBj.put("orderMagicNumber", orderMagicNumber);
						jsOBj.put("status", status); // 0執行下單 1 關閉訂單 2 修改訂單
						jsOBj.put("strategy", strategy);
						jsOBj.put("tp", "");
						jsOBj.put("sl", "");

						JSONArray jsar = new JSONArray();
						jsar.add(jsOBj.toJSONString());
						// 處理價格
						resultObj.put("result", jsar);
						System.out.println("查看回復訊息ID為:" + messageObj.getMessageId() + ":" + messageObj.getText());
						if (resultObj != null && !resultObj.isEmpty()) {
							out.println(resultObj.toJSONString());
							out.flush();
							line = reader.readLine();
						}
					}
				} catch (Exception e) {
					System.out.println("修改訂單狀態發生錯誤:" + e);
				}
			}

			// 處理VIP 👑 BinaryProfitSignals 二元期權訊號
			if (update != null && update.getMessage().getText() != null
					&& MessageFilter.binaryProfitSignals(update.getMessage().getText())) {

				String message = update.getMessage().getText();
				try {

					StringBuilder sb = new StringBuilder();
					InputStream is = new ByteArrayInputStream(message.getBytes());
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));

					PrintWriter out = new PrintWriter(socket.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
					update.getMessage().getMessageId();

					// 寫資訊給客戶端
					String line = reader.readLine();

					JSONObject obj = TextConversion.binaryProfitSignals(update.getMessage().getText());
					// 意思是 先把一筆進入暫存
					binaryProfitSignalsMap.put(update.getMessage().getMessageId(), obj);
					if (obj != null && !obj.isEmpty()) {
						out.println(obj.toJSONString());
						out.flush();
						line = reader.readLine();
					}

				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}

			}
			String goMessage = "";
			if (update.getMessage() != null && update.getMessage().getText() != null) {
				goMessage = update.getMessage().getText().toUpperCase();
			}

			// 處理VIP 👑 BinaryProfitSignals 二元期權訊號
			if (update != null && update.getMessage().getText() != null && goMessage.contains("GO")
					|| goMessage.contains("NO")) {

				String message = update.getMessage().getText();
				try {

					StringBuilder sb = new StringBuilder();
					InputStream is = new ByteArrayInputStream(message.getBytes());
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));

					PrintWriter out = new PrintWriter(socket.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

					int mesid = update.getMessage().getMessageId();
					mesid = mesid - 1;

					JSONObject catchMapOBJ = binaryProfitSignalsMap.get(mesid);

					JSONArray jsonAry = new JSONArray();
					JSONObject obj = new JSONObject();
					JSONObject outData = new JSONObject();

					if (catchMapOBJ != null) {
						jsonAry = (JSONArray) catchMapOBJ.get("result");

						JSONObject jsOBj = new JSONObject();
						jsOBj = (JSONObject) jsOBj.parse(jsonAry.get(0).toString());

						String symbol = (String) jsOBj.get("symbol");
						String direction = (String) jsOBj.get("direction");
						outData.put("symbol", symbol);
						outData.put("direction", direction);
						outData.put("strategy", "binaryOption_C");

						JSONArray jsar = new JSONArray();
						jsar.add(outData.toJSONString());
						// 處理價格
						obj.put("result", jsar);
					}
					// 寫資訊給客戶端
					String line = reader.readLine();

					if (obj != null && !obj.isEmpty()) {
						out.println(obj.toJSONString());
						out.flush();
						line = reader.readLine();
					}

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
		// 以下是 二元机器人
//		return "1110609497:AAH_tlzhgpyQrT_u3yf0yxS38abRKW_xXyc";
		// return "967307466:AAEOhsdpXtIQWeLx8pHJbiDzw3VEFZKxQpM";
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
		String messages = "💹Signal Alert\r\n" + "EURUSD - BUY NOW(市價執行) 1.410724\r\n" + "Take Profit(止盈): 1.41015\r\n"
				+ "Stop Loss(止損): 1.4040\r\n" + "\r\n" + "🚀 SHORT TERM TRAD" + "\r\n" + "投资一定有风险，外汇投资有赚有赔，不得作为下单之依据。";

		sendMessage.setText(messages);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			System.out.println("回傳TG群時候，發生錯誤" + e);

		}
		return false;

	}

}
