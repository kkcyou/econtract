package com.yaoan.module.econtract.util;


/**
 * @author Pele
 */
public class RedisUtil {
    //把Redis连接池封装成静态的
//    private static JedisPool jedisPool = null;
//
//    //编写静态代码块
//    //1、配置连接池信息
//    //2、根据配置信息创建连接池
//    static {
//        //1、配置连接池信息
//        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//        //最大连接数量
//        config.setMaxTotal(100);
//        //最大空闲连接数量
//        config.setMaxIdle(10);
//        //最小空闲连接数量
//        config.setMinIdle(5);
//        //设置等待获取连接的时间
//        config.setMaxWaitMillis(3000);
//        //2、根据配置信息创建连接池
//        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
//    }
//
//    //到连接池获取连接的方法
//    public static Jedis getJedis() {
//        return jedisPool.getResource();
//    }
}