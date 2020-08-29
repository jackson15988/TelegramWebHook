package telegram.bo;

import com.alibaba.fastjson.JSONObject;
import telegram.util.TextConversion;

import java.io.*;

public class BinaryOpection {

  /*  if (update != null && update.getMessage().getText() != null
            && update.getMessage().getText().contains("📡")) {

        String message = TextConversion.vip240Signal(update.getMessage().getText());
        // String message = update.getMessage().getText();

        // //自己群
        // LineNotification.callEvent("cNWEW5pf8tkvmytyhkeAh28Hmj82krq6PnxgDy3iYGG",
        // message);
        // M大群
        // LineNotification.callEvent("nVxs1v7eFKEKXXV4rPsLnU4LzHLmhtqS4X3ZNbvPDD5",
        // message);

        try {

            StringBuilder sb = new StringBuilder();
            InputStream is = new ByteArrayInputStream(message.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

            // 寫資訊給客戶端
            String line = reader.readLine();
            while (!"end".equalsIgnoreCase(line) && !"null".equals(line) && line != null) {

                JSONObject obj = TextConversion.convertorderInformation(update.getMessage().getText());
                if (obj != null && !obj.isEmpty()) {
                    out.println(obj.toJSONString());
                    out.flush();
                    // 將從鍵盤獲取的資訊給到伺服器
                    // 顯示輸入的資訊
                    line = reader.readLine();
                } else {
                    break;
                }
            }
            // out.close();
            // in.close();
            // socket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
}
