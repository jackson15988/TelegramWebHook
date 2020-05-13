package telegram.util;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduledExceutorRun {

	public static void rinAnnocement() throws SchedulerException {
		SchedulerFactory schedulerfactory = new StdSchedulerFactory();

		Scheduler scheduler = schedulerfactory.getScheduler();

		// 创建jobDetail实例，绑定Job实现类

		JobDetail jobDetail = JobBuilder.newJob(AnnouncementJob.class).withIdentity("myJob", "group1").build();

		// 使用JobDataMap填入想要携带的特殊信息。可以填入基本数据类型、字符串、集合，甚至是一个对象。填入方式很类似JSON

		// 定义调度触发规则，本例中使用SimpleScheduleBuilder创建了一个5s执行一次的触发器

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "triggerGroup1").startNow()

				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever())

				.build();

		// 把作业和触发器注册到任务调度中
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}

}
