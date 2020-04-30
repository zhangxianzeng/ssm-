package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.common.service.BaseService;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.CartService;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	//根据Dubbo接口实现查询方法
	//如果需要对象进行远程传输,则必须序列化
	@Override
	public List<Cart> findCartByUserId(Long userId) {
		
		Cart cart = new Cart();
		cart.setUserId(userId);

		return cartMapper.select(cart);
	}
	
	//修改数量
	@Override
	public void updateCartNum(Long userId,Long itemId, Integer num) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		cart.setUpdated(new Date());
		
		cartMapper.updateCartNum(cart);
	}

	//删除购物车信息
	@Override
	public void deleteCart(Long userId, Long itemId) {
		
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		super.deleteByWhere(cart);
	}

	@Override
	public void saveCart(Cart cart) {
		//先查询购物车信息 根据userId 和itemId
		Cart cartDB = cartMapper.findCartByUserIdAndItemId(cart);	
		if(cartDB !=null){
			//如果数据库不为null,则表示数据库含有该数据,应该将数量新增
			int count = cartDB.getNum() + cart.getNum();
			cartDB.setNum(count);
			cartDB.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}else{
			//数据库中没有改数据
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
	}
	
	
	
	
	
	
	

}
