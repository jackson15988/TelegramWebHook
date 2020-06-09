package telegram.util;

public class SymbolConfirmation {

	public static String checkSymbol(String sybol) {

		String returnStr = "";
		if (sybol.contains("EURUSD")) {
			returnStr = "EURUSD";
		} else if (sybol.contains("EURJPY")) {
			returnStr = "EURJPY";
		} else if (sybol.contains("AUDNZD")) {
			returnStr = "AUDNZD";
		} else if (sybol.contains("GBPAUD")) {
			returnStr = "GBPAUD";
		} else if (sybol.contains("GBPUSD")) {
			returnStr = "GBPUSD";
		} else if (sybol.contains("AUDJPY")) {
			returnStr = "AUDJPY";
		} else if (sybol.contains("GBPJPY")) {
			returnStr = "GBPJPY";
		} else if (sybol.contains("USDCHF")) {
			returnStr = "USDCHF";
		} else if (sybol.contains("EURGBP")) {
			returnStr = "EURGBP";
		} else if (sybol.contains("USDJPY")) {
			returnStr = "USDJPY";
		} else if (sybol.contains("EURAUD")) {
			returnStr = "EURAUD";
		} else if (sybol.contains("EURNZD")) {
			returnStr = "EURNZD";
		} else if (sybol.contains("NZDUSD")) {
			returnStr = "NZDUSD";
		} else if (sybol.contains("USDCAD")) {
			returnStr = "USDCAD";
		} else if (sybol.contains("NZDCAD")) {
			returnStr = "NZDCAD";
		} else if (sybol.contains("AUDCAD")) {
			returnStr = "AUDCAD";
		} else if (sybol.contains("GOLD")) {
			returnStr = "GOLD";
		} else if (sybol.contains("NZDJPY")) {
			returnStr = "NZDJPY";
		} else if (sybol.contains("AUDUSD")) {
			returnStr = "AUDUSD";
		} else if (sybol.contains("AUDCHF")) {
			returnStr = "AUDCHF";
		} else if (sybol.contains("CADCHF")) {
			returnStr = "CADCHF";
		} else if (sybol.contains("EURCHF")) {
			returnStr = "EURCHF";
		} else if (sybol.contains("EURCAD")) {
			returnStr = "EURCAD";
		} else if (sybol.contains("EURNZD")) {
			returnStr = "EURNZD";
		} else if (sybol.contains("GBPCAD")) {
			returnStr = "GBPCAD";
		} else if (sybol.contains("GBPCHF")) {
			returnStr = "GBPCHF";
		} else if (sybol.contains("NZDCHF")) {
			returnStr = "NZDCHF";
		} else if (sybol.contains("XAUUSD")) {
			returnStr = "XAUUSD";
		} else if (sybol.contains("CADJPY")) {
			returnStr = "CADJPY";
		}

		return returnStr;

	}

	public static boolean directionArrow(String direction) {
		boolean isCheckOk = false;

		switch (direction) {
		case "⬆️":
			isCheckOk = true;
			break;
		case "⬇️":
			isCheckOk = true;
			break;
		default:
			break;
		}

		return isCheckOk;

	}

}
