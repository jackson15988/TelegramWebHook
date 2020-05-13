package telegram.util;

public class FilterStr {

	public static boolean filteBinary_A(String str) {

		boolean filteBol = false;

		if (str.contains("📡") && str.contains("GMT+1") && str.contains("@")) {
			filteBol = true;
		} else if (str.contains("✅") || str.contains("4th") || str.contains("If lost") ) {
			filteBol = false;
		}

		return filteBol;

	}

}
