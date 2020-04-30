package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
import com.jt.common.service.BaseService;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
	
	@Autowired
	private CartMapper cartMapper;

	
	@Override
	public List<Cart> findCartByUserId(Long userId) {
		
		Cart cart = new Cart();
		cart.setUserId(userId);
		//通过通用Mapper查询购物车信息
		return cartMapper.select(cart);
	}


	@Override
	public void updateCartNum(Cart cart) {
		//添加修改时间
		cart.setUpdated(new Date());
		cartMapper.updateNum(cart);
	}


	@Override
	public void deleteCart(Long userId, Long itemId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		
		//调用父类的方法 规范:调用父类的方法时最好添加super
		super.deleteByWhere(cart);
	}

	
	/**
	 * 1.如果用户第一次添加该商品则进行入库操作
	 * 2.如果用户已经添加过该商品.则应该修改商品数据
	 *  int num = 原来的数量 + 新增数量
	 */
	@Override
	public void saveCart(Cart cart) {
		
		//根据itemId和userId判断用户是否买过该商品
		Cart cartTemp = new Cart();
		cartTemp.setUserId(cart.getUserId());
		cartTemp.setItemId(cart.getItemId());
		
		//通过数据库查询是否还有改数据
		Cart cartDB = cartMapper.findCartByUserIdAndItemId(cartTemp);
		
		//判断是否含有数据
		if(cartDB != null){
			//证明原来有该数据
			int count  = cartDB.getNum() + cart.getNum();
			//更新商品数量
			cartDB.setNum(count);
			cartDB.setUpdated(new Date());
			//数据更新 动态更新操作
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}else{
			//数据库中没有改信息 应该做新增
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
