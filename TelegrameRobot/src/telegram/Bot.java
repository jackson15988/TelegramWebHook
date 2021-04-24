package telegram;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import telegram.bo.BestForexSignalsPipsBo;
import telegram.bo.KojoForex;
import telegram.bo.ThirtyPips;
import telegram.dto.ChatDto;
import telegram.dto.MessageDto;
import telegram.util.MessageFilter;
import telegram.util.RedisUtil;
import telegram.util.TextConversion;
import telegram.vo.ConvertRoomInfo;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    HashMap<Integer, JSONObject> binaryProfitSignalsMap = new HashMap<>();
    static Socket socket = null;
    PrintWriter out;
    BufferedReader in;

  /*  {
        try {
           *//* if (socket == null) {
                System.out.print("執行");
                socket = new Socket("45.32.49.87", 9877);
            }
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));*//*
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @Override
    public void onUpdateReceived(Update update) {
        //當收到訊號之後 先行判斷是否為空值
        if (update.getMessage() != null) {
            //房間相關資訊放置於此
            ChatDto chatDto = ConvertRoomInfo.getRoomInfo(update);
            //留言訊息相關資訊
            MessageDto messageDto = ConvertRoomInfo.getMessageInfo(update);
            //圖片相關資訊
            List<PhotoSize> photos = update.getMessage().getPhoto();

            System.out.println(update.getMessage().getFrom().getFirstName() + "#: " + update.getMessage().getText());
          /*  if (socket == null) {
                try {
                    socket = new Socket("45.32.49.87", 9877);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
///
            //投資共享-FOREX/GOLD 黃金群組
            if ("-1001309319906".equals(chatDto.getChatId())) {

                // +30 pips 渠道
            } else if ("-1001469221445".equals(chatDto.getChatId())) {
                if (photos != null && photos.size() != 0) {
                    System.out.println("+ ProFxSignals 渠道接收到圖片中請稍後");
                    PhotoSize photo = photos.get(photos.size() - 1);
                    String id = photo.getFileId();
                    try {
                        GetFile getFile = new GetFile();
                        getFile.setFileId(id);
                        String filePath = getFile(getFile).getFileUrl(getBotToken());
                        URL url = new URL(filePath);
                        JSONObject sendSocketObj = ThirtyPips.getThirtyPips(url.toString());
                      /*
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));*/
                        if (sendSocketObj != null && !sendSocketObj.isEmpty()) {
                            out.println(sendSocketObj.toJSONString());
                            out.flush();
                        }
                    } catch (Exception e) {
                        System.out.print("+30 pips 發生錯誤 : " + e);
                    }
                }
                //E-KOJOFOREX 渠道
            } else if ("-1001241473030".equals(chatDto.getChatId())) {
                //收到的是
                String message = messageDto.getPictureReplyMessage();
                JSONObject sendSocketObj = KojoForex.getKojoForex(message);
                if (sendSocketObj != null && !sendSocketObj.isEmpty()) {
                    out.println(sendSocketObj.toJSONString());
                    out.flush();
                }
            } else if (update.getChannelPost().getChatId().equals("-1001292630883")) {
                    BestForexSignalsPipsBo.run(update);
            }


            // 處理外匯訊號
            if (update != null && update.getMessage().getText() != null
                    && MessageFilter.InstantProfitsFilter(update.getMessage().getText())) {
                String message = TextConversion.InstantProfitsReplce(update.getMessage().getText());

                Integer messageID = update.getMessage().getMessageId();
                System.out.println(message);
//				replyResult(update, message);

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

		/*	// 處理VIP 👑 BinaryProfitSignals 二元期權訊號
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

			}*/
		/*	String goMessage = "";
			if (update.getMessage() != null && update.getMessage().getText() != null) {
				goMessage = update.getMessage().getText().toUpperCase();
			}*/

		/*	// 處理VIP 👑 BinaryProfitSignals 二元期權訊號
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

			}*/

        }



    }

    ;

    @Override
    public String getBotUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBotToken() {
        // TODO Auto-generated method stub
        return "1158141918:AAEIhLXjVf7kU7U5WqaWo2zMyuFC9zYejjI";
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


}
