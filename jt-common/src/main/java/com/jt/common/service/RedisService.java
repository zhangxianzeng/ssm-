package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service	//spring开启包扫描时的路径为jt.com
public class RedisService {
	
	//说明:如果spring容器中有该对象则注入,如果没有改对象则不管
	@Autowired(required=false)
	private JedisSentinelPool sentinelPool;
	
	public void set(String key,String value){
		Jedis jedis = sentinelPool.getResource();
		jedis.set(key, value);
		
		sentinelPool.returnResource(jedis);
	}
	
	public String get(String key){
		Jedis jedis = sentinelPool.getResource();
		String value = jedis.get(key);
		
		sentinelPool.returnResource(jedis);
		return value;
		
	}
	
	
	
	
	/*@Autowired	//redis的spring管理中配置bean标签
	private ShardedJedisPool jedisPool;
	
	//定义set方法
	public void set(String key,String value){
		//通过池获取对象
		ShardedJedis shardedJedis = jedisPool.getResource();
		//通过jedis操作数据
		shardedJedis.set(key, value);
		//将链接还回池中
		jedisPool.returnResource(shardedJedis);
	}
	
	//定义get方法
	public String get(String key){
		ShardedJedis shardedJedis = jedisPool.getResource();
		String value = shardedJedis.get(key);
		jedisPool.returnResource(shardedJedis);
		return value;
	}	*/
}
