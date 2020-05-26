package telegram.util;

public class MessageFilter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @author admin 處理 InstantProfitsFilter 訊號判斷是否包含 如有才繼續往下走
	 * @param text
	 * @return
	 */
	public static boolean InstantProfitsFilter(String text) {
		boolean isContains = false;

		if (text.contains("Signal Alert")) {
			isContains = true;
		}
		// TODO Auto-generated method stub
		return isContains;
	}

	public static boolean binaryProfitSignals(String text) {
		boolean isContains = false;
		text = text.toUpperCase();
		if (!text.contains("WIN") || !text.contains("LOST")) {
			if (text.contains("WAIT CONFIRM") && text.contains("CALL")) {
				isContains = true;
			} else if (text.contains("WAIT CONFIRM") && text.contains("PUT")) {
				isContains = true;
			}
		}
		// TODO Auto-generated method stub
		return isContains;
	}
}
