package telegram.util;

public class main {

	public static void main(String[] args) {
		String str = "Take Profit : 1707.45";
		String[] doubSpulit = str.split("([-a-zA-Z]\\s*)++");
		String price = doubSpulit[1];

		// 如果有冒號 則進行處理
		if (price.contains(":")) {
			price = price.replace(":", " ");
		}
		price = price.trim();
		System.out.println(price);
		
		
	}

}
