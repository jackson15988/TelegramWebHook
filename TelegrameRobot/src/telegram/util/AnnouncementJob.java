package telegram.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import telegram.LineNotification;

public class AnnouncementJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		String announcement = "";
		int i = 0;

		i = (int) (Math.random() * 10);
		switch (i) {
		case 0:
			announcement = "公告:我們的機器人通知都是以GMT+1為通知時間，請自行對照GMT+8(北京/台灣)時間，以下網址為對應網址:" + "https://time.is/GMT+1";
			break;
		case 1:
			announcement = "公告:剛加入的朋友請先觀看記事本的文章，並且先到記事本第一篇進行簽到的動作";
			break;


		}
		if(!announcement.isEmpty()) {
		LineNotification.callEvent("cNWEW5pf8tkvmytyhkeAh28Hmj82krq6PnxgDy3iYGG", announcement);
		}
	}

}
