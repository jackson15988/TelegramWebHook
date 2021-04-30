package telegram.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSONObject;

public class Client {
    public static void main(String[] args) {
        try {
            WebSocetClient();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static boolean isConnection = false;

    public static boolean WebSocetClient() throws UnknownHostException, IOException {

        String amountlist[] = {"2", "5", "11", "25", "60", "140", "320"};
        // 建立連線指定Ip和埠的socket
        Socket socket = new Socket("78.141.241.32", 9877);
        // 獲取系統標準輸入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
        // 建立一個執行緒用於讀取伺服器的資訊
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        JSONObject json = new JSONObject();


                        JSONObject jsOBj = (JSONObject) json.parse(in.readLine());
                        String berIndexStr = jsOBj.getString("betCount");
                        int betIndexInt = Integer.valueOf(berIndexStr);
                        berIndexStr = amountlist[betIndexInt];
//							System.out.println(jsOBj);
//							OnclickPageBet ock = new OnclickPageBet();
//							System.out.println("------即將開始執行點選活動-----------");
////							ock.RunIqOpCtion(berIndexStr, jsOBj.getString("OrderType"));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // 寫資訊給客戶端
        String line = reader.readLine();
        while (!"end".equalsIgnoreCase(line)) {
            // 將從鍵盤獲取的資訊給到伺服器
            out.println(line);
            out.flush();
            // 顯示輸入的資訊
            line = reader.readLine();
        }
        out.close();
        in.close();
        socket.close();

        return isConnection;
    }

    private static boolean isjson(String string) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}