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

		if (text.toUpperCase().equals("SWING")) {
			isContains = false;
		}
		// TODO Auto-generated method stub
		return isContains;
	}

	/**
	 * @author admin 這裡新增一些事件 用來很明確的關閉訂單
	 * @param text
	 * @return
	 */
	public static boolean InstantProfitsModifyFilter(String text) {
		boolean isContains = false;
		text = text.toUpperCase();

		if (text.contains("CLOSE") && text.contains("IN") && text.contains("PROFIT") && !text.contains("ENTRY")) {
			isContains = true;
		}

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
