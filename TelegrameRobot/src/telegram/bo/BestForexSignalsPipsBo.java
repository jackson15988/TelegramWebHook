package telegram.bo;

import org.telegram.telegrambots.api.objects.Update;

public class BestForexSignalsPipsBo {

    public static void run(Update data) {
        System.out.print("獲取到的參數邏輯" + data);

    }

}
