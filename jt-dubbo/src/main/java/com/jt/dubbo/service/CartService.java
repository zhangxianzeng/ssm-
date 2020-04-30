package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Cart;

public interface CartService {
	
	//根据userId查询购物车信息http://cart.jt.com/cart/query/{userId}
	public List<Cart> findCartByUserId(Long userId);
	
	//修改购物车数量
	public void updateCartNum(Long userId, Long itemId, Integer num);
	
	//删除购物车
	public void deleteCart(Long userId, Long itemId);
	
	//新增购物车
	public void saveCart(Cart cart);
	
	
}
