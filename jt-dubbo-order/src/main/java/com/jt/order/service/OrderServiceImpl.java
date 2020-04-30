package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.dubbo.service.OrderService;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	
	/**
	 * 订单的新增
	 * 说明:订单表 tb_order
	 * 	        订单商品表 tb_order_item      一对多
	 *      订单物流 tb_order_shipping    一对一
	 */
	
	/**
	 * 消息队列的实现
	 * 1.返回值类型为String 返回orderId
	 * 2.通过模板对象 发送消息
	 */
	@Override
	public String saveOrder(Order order) {
		//准备orderId
		String orderId = order.getUserId() + "" + System.currentTimeMillis();
		
		//为rabbitMQ封装数据
		order.setOrderId(orderId);
		
		//将order信息发往消息队列rabbitMQ中 定义路由key
		rabbitTemplate.convertAndSend("save.Order",order);
		
		//返回orderId 用于查询操作
		return orderId;
		
		
		/*//封装Order对象
		order.setOrderId(orderId);
		order.setStatus(1);  //状态为1表示 未支付
		order.setCreated(new Date());
		order.setUpdated(order.getCreated());
		orderMapper.insert(order);
		
		//封装orderItem对象
		*//**
		 * 将List集合实现批量插入
		 * insert into tb_order_item values 
		 * (id,userId,itemId.....),(id,userId,itemId.....),
		 * (id,userId,itemId.....),(id,userId,itemId.....);
		 * 
		 * Sql实例:
		 *  insert into user values (null,"柳鹏林",18,"男"),
			(null,"妲己",18,"男"),
			(null,"如花",17,"男");
		 *//*
		List<OrderItem> orderItemList = order.getOrderItems();
		
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderId(orderId);
			orderItemMapper.insert(orderItem);
		}
		
		//封装OrderShipping数据
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(order.getCreated());
		orderShipping.setUpdated(order.getCreated());
		orderShippingMapper.insert(orderShipping);*/
		
		
	}

	
	//通过sql实现多表查询
	@Override
	public Order findOrderById(String orderId) {
		
		return orderMapper.findOrderById(orderId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
