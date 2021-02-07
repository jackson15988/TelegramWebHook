package telegram;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class LineNotification {
	private static final String strEndpoint = "https://notify-api.line.me/api/notify";

	public static boolean callEvent(String token, String message) {
		boolean result = false;
		try {
			message = replaceProcess(message);
			message = URLEncoder.encode(message, "UTF-8");
			String strUrl = strEndpoint;
			URL url = new URL(strUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			String parameterString = new String("message=" + message);
			PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
			printWriter.print(parameterString);

			printWriter.close();
			connection.connect();

			int statusCode = connection.getResponseCode();
			if (statusCode == 200) {
				result = true;
			} else {
				throw new Exception("Error:(StatusCode)" + statusCode + ", " + connection.getResponseMessage());

			}
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static String replaceProcess(String txt) {
		txt = replaceAllRegex(txt, "\\\\", "￥"); // \
		return txt;
	}

	private static String replaceAllRegex(String value, String regex, String replacement) {
		if (value == null || value.length() == 0 || regex == null || regex.length() == 0 || replacement == null)
			return "";
		return Pattern.compile(regex).matcher(value).replaceAll(replacement);
	}

	public static void main(String[] args) {

		callEvent("4MAoJUWyFHeL41rpUqHqJ5E2OY6l6MHUNxo3zooB9Nx",
				"烙史的功能 哀  領22K的我 好可憐");

	}
}
