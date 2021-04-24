package telegram.bo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import telegram.util.OCRAsyncTask;
import telegram.util.SymbolConfirmation;
import telegram.util.TextConversion;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.telegram.telegrambots.api.methods.GetFile;

// + 30 pips 渠道 邏輯
public class ThirtyPips {

    public static JSONObject getThirtyPips(String picturePath) {
        JSONObject resultObj = new JSONObject();

        // 開始OCR
        String ocrStr = null;
        try {
            ocrStr = OCRAsyncTask.sendPost(true, picturePath, "eng");
        } catch (Exception e) {
            System.out.print("圖片識別發生錯誤:" + e);
        }
        JSONObject jsonObj = new JSONObject();
        JSONArray jsAry = new JSONArray();
        jsonObj = (JSONObject) jsonObj.parse(ocrStr);
        jsAry = (JSONArray) jsonObj.get("ParsedResults");
        jsonObj = (JSONObject) jsAry.get(0);
        String LineStr = (String) jsonObj.get("ParsedText");

        if (LineStr != null) {
            jsonObj = (JSONObject) jsonObj.get("TextOverlay");
            jsAry = (JSONArray) jsonObj.get("Lines");

            String tpStr = "";
            String symbol = "";
            String direction = "";
            String price = "";
            String slStr = "";
            for (Object object : jsAry) {
                jsonObj = (JSONObject) jsonObj.parse(object.toString());
                String lineText = (String) jsonObj.get("LineText");
                if (lineText.toUpperCase().contains("BUY")) {
                    direction = "0";
                    lineText = lineText.replace("/", "");
                    symbol = SymbolConfirmation.checkSymbol(lineText);
                    price = lineText.toUpperCase();
                    price = price.substring(price.indexOf("BUY:"), price.length());
                    price = TextConversion.priceConversion(price);
                    System.out.println("獲取到價格:" + price);
                    System.out.println("獲取到方向:" + direction);
                    System.out.println("獲取商品:" + symbol);
                } else if (lineText.toUpperCase().contains("SELL")) {
                    direction = "1";
                    lineText = lineText.replace("/", "");
                    symbol = SymbolConfirmation.checkSymbol(lineText);
                    price = lineText.toUpperCase();
                    price = price.substring(price.indexOf("SELL:"), price.length());
                    price = TextConversion.priceConversion(price);
                    System.out.println("獲取到價格:" + price);
                    System.out.println("獲取到方向:" + direction);
                    System.out.println("獲取商品:" + symbol);

                } else if (lineText.toUpperCase().contains("TP") || lineText.toUpperCase().contains("rp") ) {
                    lineText = lineText.toUpperCase();

                    if (lineText.contains("SI")) {
                        tpStr = lineText.substring(lineText.indexOf("TP:"), lineText.indexOf("SI:"));
                        tpStr = TextConversion.priceConversion(tpStr);

                        slStr = lineText.substring(lineText.indexOf("SI:"), lineText.length());
                        slStr = TextConversion.priceConversion(slStr);

                        System.out.println("獲取TP價格:" + tpStr);
                        System.out.println("獲取SL價格:" + slStr);
                    }
                }
            }
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String strDate = sdFormat.format(date);

            JSONObject jsobj = new JSONObject();

            jsobj.put("symbol", symbol);
            jsobj.put("direction", direction);
            jsobj.put("price", price);
            jsobj.put("tp", tpStr);
            jsobj.put("sl", slStr);
            jsobj.put("date", strDate);
            jsobj.put("strategy", "forex_B");
            jsobj.put("status", "0");
            jsobj.put("remarks", "+30pip");

            long timeStampSec = System.currentTimeMillis() / 1000;
            String magicNumber = String.format("%010d", timeStampSec);
            magicNumber = magicNumber.replaceFirst("^0*", "");
            jsobj.put("orderMagicNumber", String.valueOf(magicNumber));

            JSONArray jsar = new JSONArray();
            jsar.add(jsobj.toJSONString());
            resultObj.put("result", jsar);
        }
        return resultObj;
    }
}
