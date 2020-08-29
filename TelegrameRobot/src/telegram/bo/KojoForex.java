package telegram.bo;

import com.alibaba.fastjson.JSONObject;

public class KojoForex {

    public static JSONObject getKojoForex(String message) {

        String[] split_message = message.split("/n");
        System.out.print(split_message);
        return null;
    }

    ;

}
