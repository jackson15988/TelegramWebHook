package telegram.bo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import telegram.util.OCRAsyncTask;
import telegram.util.SymbolConfirmation;
import telegram.util.TextConversion;
import telegram.vo.MultipOrderDetailVO;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            JSONObject jsobj = new JSONObject();
            long timeStampSec = System.currentTimeMillis() / 1000;
            String magicNumber = String.format("%010d", timeStampSec);
            magicNumber = magicNumber.replaceFirst("^0*", "");
            jsobj.put("orderMagicNumber", String.valueOf(magicNumber));

            String tpPrice = "";
            String symbol = "";
            String direction = "";
            String price = "";
            String slPrice = "";
            String tp1 = "";
            String tp2 = "";
            String tp3 = "";
            List<MultipOrderDetailVO> MultipOrderDetailList = new ArrayList();
            for (Object object : jsAry) {
                jsonObj = (JSONObject) jsonObj.parse(object.toString());
                MultipOrderDetailVO multipOrderDetailVO = new MultipOrderDetailVO();
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

                } else if (lineText.contains("Tpl:") || lineText.contains("Tp1:") ) {
                    lineText = lineText.substring(lineText.indexOf(":") + 1,lineText.length());
                    tp1 = TextConversion.priceConversion(lineText);
                    multipOrderDetailVO.setTp(tp1);
                    multipOrderDetailVO.setTarget("Tp1");
                    multipOrderDetailVO.setSymbol(symbol);
                    multipOrderDetailVO.setOrderMagicNumber(magicNumber);
                    System.out.println("獲取TP1價格:" + tp1);
                }else if(lineText.contains("Tp2:")){
                    lineText = lineText.substring(lineText.indexOf(":") + 1,lineText.length());
                    tp2 = TextConversion.priceConversion(lineText);
                    multipOrderDetailVO.setTp(tp2);
                    multipOrderDetailVO.setTarget("Tp2");
                    multipOrderDetailVO.setSymbol(symbol);
                    multipOrderDetailVO.setOrderMagicNumber(String.valueOf(Integer.valueOf(magicNumber) + 1) );
                    System.out.println("獲取TP2價格:" + tp2);
                } else if(lineText.contains("Tp3:")){
                    lineText = lineText.substring(lineText.indexOf(":")+1,lineText.length());
                    tp3 = TextConversion.priceConversion(lineText);
                    multipOrderDetailVO.setTp(tp3);
                    multipOrderDetailVO.setTarget("Tp3");
                    multipOrderDetailVO.setSymbol(symbol);
                    multipOrderDetailVO.setOrderMagicNumber(String.valueOf(Integer.valueOf(magicNumber) + 2) );
                    System.out.println("獲取TP3價格:" + tp3);
               }else if(lineText.toUpperCase().contains("SL")){
                    lineText = lineText.substring(lineText.indexOf(":")+1,lineText.length());
                     slPrice = TextConversion.priceConversion(lineText);
                    System.out.println("獲取SL價格:" + slPrice);
                }
                if(!objCheckIsNull(multipOrderDetailVO)) {
                    MultipOrderDetailList.add(multipOrderDetailVO);
                }
            }
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String strDate = sdFormat.format(date);


            jsobj.put("symbol", symbol);
            jsobj.put("direction", direction);
            jsobj.put("price", price);
            jsobj.put("tp", tpPrice);
            jsobj.put("sl", slPrice);
            jsobj.put("date", strDate);
            jsobj.put("strategy", "forex_A");
            jsobj.put("status", "0");
            jsobj.put("remarks", "ProFxSignals" + tpPrice);
            jsobj.put("multipOrderDetail", MultipOrderDetailList);
            jsobj.put("isMultipleChildOrder", "true");

            JSONArray jsar = new JSONArray();
            jsar.add(jsobj.toJSONString());
            resultObj.put("result", jsar);
        }
        return resultObj;
    }


    public static boolean objCheckIsNull(Object object){
        Class clazz = (Class)object.getClass(); // 得到类对象
        Field fields[] = clazz.getDeclaredFields(); // 得到所有属性
        boolean flag = true; //定义返回结果，默认为true
        for(Field field : fields){
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object); //得到属性值
                Type fieldType =field.getGenericType();//得到属性类型
                String fieldName = field.getName(); // 得到属性名
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(fieldValue != null){  //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }
}
