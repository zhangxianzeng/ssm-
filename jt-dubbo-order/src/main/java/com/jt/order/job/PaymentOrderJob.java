package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;
//spring整合Quartz
public class PaymentOrderJob extends QuartzJobBean{
	
	/**
	 * 通过Spring容器获取OrderMapper对象,通过Ordermapper对象修改
	 * 超时的状态
	 * 根据超时时间将状态修改为6
	 * 问题:如何判断时间已经超时
	 * 超时的时间节点 = 当前时间-24小时(1天)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//从定时任务管理器中获取Spring容器
		ApplicationContext applicationContext = 
(ApplicationContext) context.getJobDetail().getJobDataMap().get("applicationContext");
		
		OrderMapper orderMapper = 
				applicationContext.getBean(OrderMapper.class);
		DateTime dateTime = new DateTime();
		//time表示超时时间节点 一天超时
		Date time = dateTime.minusDays(1).toDate();
		System.out.println("定义任务执行!!!!!!!!!!");
		//将状态由未支付1 改为交易关闭6
		orderMapper.updateStatusByDate(time);
	}
}
