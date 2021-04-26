package telegram.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class TextConversion {

	public static HashMap<String, String> onlyJson = new HashMap<>();

	public static void main(String[] args) {

	}

	public static String vip240Signal(String str) {

		if (str.contains("via")) {
			str = str.substring(0, str.lastIndexOf("via"));
		}
		str = str.substring(str.lastIndexOf("📡"), str.length());

		// 處理分鐘邏輯
		if (str.contains("(15mins expiry)")) {
			str = str.replace("(15mins expiry)", "(15分鐘之後到期)");
		} else if (str.contains("(30mins expiry)")) {
			str = str.replace("(30mins expiry)", "(30分鐘之後到期)");
		} else if (str.contains("(1 hour expiry)")) {
			str = str.replace("(1 hour expiry)", "(1小時之後到期)");
		}

		// 處理馬丁格爾訊號
		if (str.contains("1st martingale")) {
			str = str.replace("1st martingale", "進行第一次馬丁");
		} else if (str.contains("2nd martingale")) {
			str = str.replace("2nd martingale", "進行第二次馬丁");
		} else if (str.contains("3rd martingale")) {
			str = str.replace("3rd martingale", "進行第三次馬丁");
		} else if (str.contains("first martingale")) {
			str = str.replace("first martingale", "進行第一次馬丁");
		}

		System.out.println(str);
		return str;

	}

	public static JSONObject convertorderInformation(String str) {
		JSONObject resultObj = new JSONObject();
		JSONObject jsobj = new JSONObject();
		// A條件囊括在內
		boolean filterstr_A = FilterStr.filteBinary_A(str);
		if (filterstr_A) {
			String martingale = "0";
			if (str.contains("1st martingale") || str.contains("first martingale")
					|| str.contains("First Martingale")) {
				martingale = "1";
			} else if (str.contains("2nd martingale")) {
				martingale = "2";
			} else if (str.contains("3rd martingale")) {
				martingale = "3";
			}

			str = str.substring(0, str.lastIndexOf("👍"));
			str = str.substring(str.lastIndexOf("📡"), str.length());

			String symbol = str.substring(str.lastIndexOf("📡") + 2, str.lastIndexOf("@") - 4);
			symbol = symbol.trim();
			System.out.println("取得該下單的商品:" + symbol);

			String time = str.substring(str.lastIndexOf("@") + 1, str.lastIndexOf("GMT+1"));
			time = time.trim();
			System.out.println("取得該下單的時間:" + time);

			String direction = str.substring(str.lastIndexOf("@") - 3, str.lastIndexOf("@"));
			if (direction.contains("⬆️")) {
				direction = "CALL";
			} else if (direction.contains("⬇️")) {
				direction = "PUT";
			}
			System.out.println("取得該下單的方向:" + direction);

			String expireMinuteTime = "0";
			String expireHourTime = "0";
			if (str.contains("(15mins expiry)") || str.contains("(15分鐘之後到期)")) {
				expireMinuteTime = "15";
			} else if (str.contains("(30mins expiry)") || str.contains("(30分鐘之後到期)")) {
				expireMinuteTime = "30";
			} else if (str.contains("(1 hour expiry)") || str.contains("(1小時之後到期)") || str.contains("(1hour expiry)")) {
				expireHourTime = "1";
			}
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String strDate = sdFormat.format(date);

			System.out.println("取得該下單的分鐘:" + expireMinuteTime);
			System.out.println("取得該下單的小時:" + expireHourTime);
			System.out.println("取得該下單的馬丁次數:" + martingale);

			jsobj.put("symbol", symbol);
			jsobj.put("time", time);
			jsobj.put("direction", direction);
			jsobj.put("expireMinuteTime", expireMinuteTime);
			jsobj.put("expireHourTime", expireHourTime);
			jsobj.put("martingale", martingale);
			jsobj.put("strategy", "binaryOption_A");
			jsobj.put("strDate", strDate);

			resultObj.put("result", jsobj);
			if (onlyJson.get(jsobj.toString()) == null) {
				System.out.println("判斷訊號為唯一乾淨訊號");
				onlyJson.put(jsobj.toString(), time);
			} else {
				System.out.println("ERROR !! 訊號有錯誤的重複的訊號,強制把訊號轉為null處理");
				resultObj = null;
			}
			// 以下稱為三部丁策略 Ｂ策略
		} else if (str.contains("📡") && str.contains(" Session Signals ") && str.contains("3 step martingale")) {

			HashMap<String, String> Symbol = new HashMap<>();
			HashMap<String, String> Direction = new HashMap<>();

			str = str.substring(str.indexOf("📡") + 1, str.lastIndexOf("("));
			String[] symbolList = str.split("⬆️|⬇️| ");
			Integer countKey = 0;
			for (String string : symbolList) {
				String regularSymbolStr = SymbolConfirmation.checkSymbol(string);
				if (regularSymbolStr != "") {
					Symbol.put(String.valueOf(countKey), regularSymbolStr);
					countKey += 1;
				}
			}

			String condition = "";
			for (Entry<String, String> entry : Symbol.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
				condition += entry.getValue() + "|";

			}
			condition = condition.substring(0, condition.length() - 1);
			String[] directionList = str.split(condition);

			Integer directionCount = 0;
			for (String string : directionList) {
				string = string.trim();
				boolean isCheckOk = SymbolConfirmation.directionArrow(string);
				if (isCheckOk) {
					if ("⬆️".equals(string)) {
						string = "CALL";
					} else if ("⬇️".equals(string)) {
						string = "PUT";
					}
					Direction.put(String.valueOf(directionCount), string);
					directionCount += 1;
				}
			}

			JSONArray jsar = new JSONArray();

			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String strDate = sdFormat.format(date);
			for (Entry<String, String> entry : Symbol.entrySet()) {

				jsobj.put("symbol", entry.getValue());
				jsobj.put("time", "");
				jsobj.put("direction", Direction.get(entry.getKey()));
				jsobj.put("expireMinuteTime", "0");
				jsobj.put("expireHourTime", "1");
				jsobj.put("martingale", "0");
				jsobj.put("strategy", "binaryOption_B");
				jsobj.put("strDate", strDate);
				jsar.add(jsobj.toJSONString());

			}
			resultObj.put("result", jsar);

			if (onlyJson.get(jsar.toString()) == null) {
				System.out.println("判斷訊號為唯一乾淨訊號");
				onlyJson.put(jsar.toString(), "");
			} else {
				System.out.println("ERROR !! 訊號有錯誤的重複的訊號,強制把訊號轉為null處理");
				resultObj = null;
			}
		} else {
			resultObj = null;
		}
		return resultObj;

	}

	public static String InstantProfitsReplce(String message) {
		message = message.substring(message.indexOf("Signal"), message.length());

		if (message.contains("♻️SWING TRADE♻️")) {
			message = message.substring(0, message.indexOf("♻️SWING TRADE♻️"));
		}

		if (message.contains("🛳 MID TERM TRADE")) {
			message = message.substring(0, message.indexOf("♻️SWING TRADE♻️"));
		}

		return message;

	}

	public static JSONObject InstantProfitsJsobject(String str, Integer messageID) {
		JSONObject jsobj = new JSONObject();
		JSONObject resultObj = new JSONObject();
		// {"result":["{\"symbol\":\"EURUSD\",\"price\":\"1.15445\",\"tp\":\"1.15445\",\"sl\":\"1.15554\",\"date\":\"2020/05/20\",\"strategy\":\"forex\",\"remarks\":\"這是一筆測試單\",\"direction\":\"3\"}"]}
		String symbol = SymbolConfirmation.checkSymbol(str);
		jsobj.put("symbol", symbol);
		if (str.contains("BUY NOW") || str.contains("BUY AGAIN")) {
			jsobj.put("direction", "0"); // 0 buy 1 sell 2 buystop 3 sellstop 4
			// // buylimit 5 selllimit
		} else if (str.contains("SELL NOW") || str.contains("SELL AGAIN")) {
			jsobj.put("direction", "1");
		} else if (str.contains("BUY STOP")) {
			jsobj.put("direction", "4");
			String priceStr = str.substring(str.indexOf("BUY STOP"), str.indexOf("Take Profit"));
			priceStr = priceConversion(priceStr);
			jsobj.put("price", priceStr);
		} else if (str.contains("SELL STOP")) {
			jsobj.put("direction", "5");
			String priceStr = str.substring(str.indexOf("SELL STOP"), str.indexOf("Take Profit"));
			priceStr = priceConversion(priceStr);
			jsobj.put("price", priceStr);
		} else if (str.contains("BUY LIMIT")) {
			jsobj.put("direction", "2");
			String priceStr = str.substring(str.indexOf("BUY LIMIT"), str.indexOf("Take Profit"));
			priceStr = priceConversion(priceStr);
			jsobj.put("price", priceStr);
		} else if (str.contains("SELL LIMIT")) {
			jsobj.put("direction", "3");
			String priceStr = str.substring(str.indexOf("SELL LIMIT"), str.indexOf("Take Profit"));
			priceStr = priceConversion(priceStr);
			jsobj.put("price", priceStr);
		}
		// 處理價格
		String tpStr = str.substring(str.indexOf("Take Profit"), str.indexOf("Stop Loss"));
		String tp = priceConversion(tpStr);
		System.out.println("取得tp價格:" + tp);
		jsobj.put("tp", tp);
		int slIndex;
		String slStr = "";
		if (str.contains("🛥")) {
			slIndex = str.indexOf("🛥");
			slStr = str.substring(str.indexOf("Stop Loss"), slIndex);
		} else if (str.contains("♻️")) {
			slIndex = str.indexOf("♻️");
			slStr = str.substring(str.indexOf("Stop Loss"), slIndex);
		} else if (str.contains("🚀")) {
			slIndex = str.indexOf("🚀");
			slStr = str.substring(str.indexOf("Stop Loss"), slIndex);
		} else {
			slStr = str.substring(str.indexOf("Stop Loss"), str.indexOf("Stop Loss") + 20);
		}
		String sl = priceConversion(slStr);
		System.out.println("取得sl價格:" + sl);
		jsobj.put("sl", sl);

		JSONArray jsar = new JSONArray();

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		jsobj.put("date", strDate);
		jsobj.put("strategy", "forex_A");
		jsobj.put("status", "0");
		jsobj.put("remarks", "Instant_Profits");

		long timeStampSec = System.currentTimeMillis() / 1000;
		String magicNumber = String.format("%010d", timeStampSec);
		magicNumber = magicNumber.replaceFirst("^0*", "");

		jsobj.put("orderMagicNumber", String.valueOf(magicNumber));

		jsar.add(jsobj.toJSONString());

		// 處理價格
		resultObj.put("result", jsar);

		String ms5Str = MD5Tools.MD5(resultObj.toJSONString());
		if (onlyJson.get(ms5Str) == null) {
			onlyJson.put(ms5Str, jsobj.toString());
			RedisUtil.setRedis(String.valueOf(messageID), resultObj);

		} else {
			System.out.println("ERROR !! 訊號有錯誤的重複的訊號,強制把訊號轉為null處理");
			resultObj = null;
		}
		return resultObj;

	}

	public static JSONObject binaryProfitSignals(String str) {

		JSONObject jsobj = new JSONObject();
		JSONObject resultObj = new JSONObject();

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String strDate = sdFormat.format(date);

		jsobj.put("time", "");
		str = str.toUpperCase();
		if (str.contains("5 MIN")) {
			jsobj.put("expireMinuteTime", "5");
			jsobj.put("expireHourTime", "0");
		} else if (str.contains("10 MIN")) {
			jsobj.put("expireMinuteTime", "10");
			jsobj.put("expireHourTime", "0");
		} else if (str.contains("15 MIN")) {
			jsobj.put("expireMinuteTime", "15");
			jsobj.put("expireHourTime", "0");
		} else if (str.contains("30 MIN")) {
			jsobj.put("expireMinuteTime", "30");
			jsobj.put("expireHourTime", "0");
		} else if (str.contains("60 MIN")) {
			jsobj.put("expireMinuteTime", "0");
			jsobj.put("expireHourTime", "1");
		} else if (str.contains("7 MIN")) {
			jsobj.put("expireMinuteTime", "7");
			jsobj.put("expireHourTime", "0");
		} else if (str.contains("8 MIN")) {
			jsobj.put("expireMinuteTime", "8");
			jsobj.put("expireHourTime", "0");
		}

		int symbolindexEnd;
		if (str.contains("PUT")) {
			symbolindexEnd = str.indexOf("PUT");
			jsobj.put("direction", "PUT");
		} else {
			symbolindexEnd = str.indexOf("CALL");
			jsobj.put("direction", "CALL");
		}
		String symbol = str.substring(0, symbolindexEnd);
		symbol = symbol.replace(" ", "").trim();

		symbol = SymbolConfirmation.checkSymbol(symbol);
		jsobj.put("symbol", symbol);

		jsobj.put("martingale", "0");
		jsobj.put("status", "wait");
		jsobj.put("strategy", "binaryOption_C");
		jsobj.put("strDate", strDate);

		JSONArray jsar = new JSONArray();
		jsar.add(jsobj.toJSONString());
		// 處理價格
		resultObj.put("result", jsar);
//		GBP USD PUT 5 MIN WAIT CONFIRM

		return resultObj;

	}

	/**
	 * @author admin 丟一個模糊價格近來 如果包含英文也近來進行轉換
	 * @param strMessage
	 * @return
	 */
	public static String priceConversion(String strMessage) {
		strMessage = strMessage.trim();
		String price = "";
		String[] doubSpulit = strMessage.split("([-a-zA-Z]\\s*)++");
		if(doubSpulit.length != 0) {
			 price = doubSpulit[1];
			if (price.contains(":")) {
				price = price.replace(":", " ");
			}
		}
		price = price.trim();
		return price;
	}

	/**
	 * 計算止損價格，例如SL 50: 計算當前價格 1.6511
	 * @param sl    止損
	 * @param price 價格
	 * @param decimalPlaces 小數點幾位數
	 * @return
	 */
	public static BigDecimal  calculateStopLossPrice(Integer sl , BigDecimal price  ,Integer decimalPlaces ){

		if(decimalPlaces == 5){
			return price.subtract(BigDecimal.valueOf(sl*0.0001)).setScale(5, RoundingMode.HALF_DOWN);
		}else if(decimalPlaces == 6){
			return price.subtract(BigDecimal.valueOf(sl*0.001)).setScale(5, RoundingMode.HALF_DOWN);
		}
		return null;
	}

}
