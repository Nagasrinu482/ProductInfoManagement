package com.example.demo.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronScheduler {

	/*
	 * @Scheduled(cron = "0 * 9 * * ?") public void cronJobSch() { SimpleDateFormat
	 * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); Date now = new Date();
	 * String strDate = sdf.format(now);
	 * System.out.println("Java cron job expression:: " + strDate); }
	 */
	
	/*
	 * @Scheduled(fixedRate = 1000) public void fixedRateSch() { SimpleDateFormat
	 * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	 * 
	 * Date now = new Date(); String strDate = sdf.format(now);
	 * System.out.println("Fixed Rate scheduler:: " + strDate); }
	 */
}