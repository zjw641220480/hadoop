package cn.itcast.tom.stormlog.orderMonitor.utils;

import java.util.List;

import cn.itcast.tom.stormlog.orderMonitor.constant.RedisConstant;
import cn.itcast.tom.stormlog.orderMonitor.constant.RuleField;
import cn.itcast.tom.stormlog.orderMonitor.domain.Condition;
import cn.itcast.tom.stormlog.orderMonitor.domain.PaymentInfo;
import cn.itcast.tom.stormlog.orderMonitor.domain.Product;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <p>Titile:ConditionMatch</p>
 * <p>Description:规则匹配核心类 </p>
 * @author TOM
 * @date 2017年6月28日 上午9:37:30
 */
public class ConditionMatch {
	public static boolean match(PaymentInfo paymentInfo, List<Condition> conditions) {
		boolean isTrigger = false;
		for (Condition condition : conditions) {
			String field = condition.getField();
			isTrigger = executeMethod(field, condition, paymentInfo);
			// 如果当前执行的条件不匹配，就不在执行
			if (!isTrigger) {
				break;
			}
		}
		return isTrigger;
	}
	private static boolean executeMethod(String field,Condition condition,PaymentInfo paymentInfo){
		if(RuleField.IS_NORMAL_IP.equals(field)){
			//检查当前用户是否在常用IP地址下单
			return isNormalIp(paymentInfo.getUser(), paymentInfo.getIp());
		} else if(RuleField.IS_NORMAL_DEVICE.equals(field)){
			//检查当前用户是否在常用的设备上下单
			return isNormalDevice(paymentInfo.getUser(),paymentInfo.getDevice());
		} else if(RuleField.IS_NORMAL_ADDRESS.equals(field)){
			//检查当前用户的收货地址是否常用收货地址
			return isNormalAddress(paymentInfo.getUser(),paymentInfo.getAddressCode());
		} else if(RuleField.IS_NORMAL_MOBILE.equals(field)){
			//检查当前用户的收货手机号是否是常用手机号
			return isNormalMobile(paymentInfo.getUser());
		} else if(RuleField.IS_CHANGE_MOBILE.equals(field)){
			//检查当前用户是否在一定时间范围内修改过手机号码
			return isChangeMobile(paymentInfo.getUser());
		} else if(RuleField.IS_CHANGE_ACCOUT_PASSWORD.equals(field)){
			//检查当前用户是否在一定时间范围内修改过账户密码
			return isChangeAccountPassword(paymentInfo.getUser());
		} else if (RuleField.IS_CHANGE_PAYMENT_PASSWORD.equals(field)) {
            //检查当前用户是否在一定时间范围内修改过支付密码
            return isChangePaymentPassword(paymentInfo.getUser());
        } else if (RuleField.IS_CASH_ON_DELIVERY.equals(field)) {
            //检查当前当前订单是否是货到付款
            return isCashOnDelivery(paymentInfo.getOrderType());
        } else if (RuleField.ORDER_PRODUCT_NUM_BY_PRICE.equals(field)) {
            //检查当前订单中商品价格大于阈值的商品数量是否符合业务指定的数量
            String value = condition.getValue();
            String[] arrValue = value.split("\\|");
            if (arrValue.length != 2) {
                return false;
            }
            return orderProductNumByPrice(paymentInfo, Integer.parseInt(arrValue[0]), Integer.parseInt(arrValue[1]));
        } else if (RuleField.ORDER_PROCUDT_NUM_BY_CATEGORY.equals(field)) {
            //价差当前订单中商品价格大于阈值，并且商品类别在高价值分类中的商品数量是否满足业务指定的数量
            String value = condition.getValue();
            String[] arrValue = value.split("\\|");
            if (arrValue.length != 2) {
                return false;
            }
            return orderProductNumByHighValue(paymentInfo, Integer.parseInt(arrValue[1]), Integer.getInteger(arrValue[2]));
        } 
		return false;
	}
	 /**
     * 价差当前订单中商品价格大于阈值，并且商品类别在高价值分类中的商品数量是否满足业务指定的数量
     *
     * @param paymentInfo 订单支付信息
     * @param productNum  大于{price}的商品数量
     * @param price       业务指定的价格
     * @return
     */
    private static boolean orderProductNumByHighValue(PaymentInfo paymentInfo, int productNum, int price) {
        //从支付信息中获取商品的列表
        List<Product> productList = paymentInfo.getProducts();
        int tmpNum = 0;
        for (Product product : productList) {
            if (product.getPrice().toBigInteger().intValue() > price && isHighValueCatagory(product.getCatagory())) {
                tmpNum += product.getNum();
            }
        }
        return tmpNum > productNum;
    }
    /**
     * 校验商品目录是否是高价值易变现
     *
     * @param catagory 商品目录 在实际的产品中，商品的目录可以分为多个级别的目录，这里的目录是最终目录
     * @return
     */
    private static boolean isHighValueCatagory(String catagory) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
        String redisKey = getHighValueCatagoryKey();
        //sismember(key, member) ：测试member是否是名称为key的set的元素
        return jedis.sismember(redisKey, catagory);
    }
    /**
     * 验证当前品类是否是高价值易变现的品类
     * key：om:catagory:highvalue
     * value:set:catagory1,catagory2,catagory3
     *
     * @return
     */
    private static String getHighValueCatagoryKey() {
        return RedisConstant.ORDER_MONITOR + RedisConstant.HIGH_VALUE_CATAGORY;
    }
	private static boolean orderProductNumByPrice(PaymentInfo paymentInfo, int productNum, int price) {
		List<Product> productList = paymentInfo.getProducts();
        int tmpNum = 0;
        for (Product product : productList) {
            if (product.getPrice().toBigInteger().intValue() > price) {
                tmpNum += product.getNum();
            }
        }
        return tmpNum > productNum;
	}
	private static boolean isCashOnDelivery(String orderType) {
		//订单类型有很多种，其中122是货到付款
        return "122".equals(orderType);
	}
	private static boolean isChangePaymentPassword(String user) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserChangePaymentPasswordKey(user);
		//判断用户是否修改过密码
        return jedis.exists(redisKey);
	}
	private static boolean isChangeAccountPassword(String user) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserChangeAccountPasswordKey(user);
		//判断用户是否修改过密码
        return jedis.exists(redisKey);	}
	private static String getUserChangeAccountPasswordKey(String user) {
		return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.CHANGE_ACCOUNT_PASSWORD;
	}
	private static boolean isChangeMobile(String user) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserChangeMobileKey(user);
		//判断用户是否修改过密码
        return jedis.exists(redisKey);
	}
	/**
	 * 
	 * @MethodName:getUserChangeMobileKey
	 * @Description:验证用户是否修改过手机号码,
	 * key：om：{user}:isChangeMoible
     * value:string:null
     * 如果用户近期修改过密码，会按照规则生成redisKey存放在redis中。并设置过期时间为指定阈值。
     * 该阈值根据业务人员的经验来设置，各公司不一样，属于商业机密
	 * @param user
	 * @return
	 * @Time: 2017年6月28日 上午10:30:48
	 * @author: TOM
	 */
	private static String getUserChangeMobileKey(String user) {
		return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.CHANGE_MOBILE;
	}
	private static boolean isNormalMobile(String user) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserChangePaymentPasswordKey(user);
		//判断用户是否修改过密码
        return jedis.exists(redisKey);
	}
	/**
	 * 
	 * @MethodName:getUserChangePaymentPasswordKey
	 * @Description:验证用户是否修改过支付密码;
	 * key：om：{user}:isChaPayPsd
	 * value:string:null
	 * 如果近期修改过密码，会按照规则生成redisKey存放在redis中。bong设置过期时间为指定阈值。
	 * 该阈值根据业务人员的经验来设置，各公司不一样，属于商业机密;
	 * @param user 用户注册名
	 * @return
	 * @Time: 2017年6月28日 上午10:26:23
	 * @author: TOM
	 */
	private static String getUserChangePaymentPasswordKey(String user) {
		return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.CHANGE_PAYMENT_PASSWORD;
	}
	private static boolean isNormalAddress(String user, String addressCode) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
        String redisKey = getUserNormalAddressKey(user);
        return jedis.sismember(redisKey, addressCode);
	}
	 
	private static boolean isNormalDevice(String user, String device) {
		ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserNormalDeviceKey(user);
		return jedis.sismember(redisKey, device);
	}
	
	/**
	 * 
	 * @MethodName:getUserNormalDeviceKey
	 * @Description:保存用户常用的设备信息,将所有的设备名称存放在redis的set中
	 * key：om:{user}:normal:device
	 * value:set:device1,device2,device3,device4
	 * @param user 用户注册名
	 * @return 返回代表当前用户所有设备信息的redisKey
	 * @Time: 2017年6月28日 上午10:12:37
	 * @author: TOM
	 */
	private static String getUserNormalDeviceKey(String user) {
		return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.NORMAL_DEIVICE;
	}
	/**
	 * 
	 * @MethodName:isNormalIp
	 * @Description:检查当前用户是否在常用IP地址下单
	 * @param user 	用户名
	 * @param id	当前下单IP地址
	 * @return
	 * @Time: 2017年6月28日 上午9:44:25
	 * @author: TOM
	 */
	private static boolean isNormalIp(String user,String ip){
		ShardedJedis shardedJedis = MyShardedJedisPool.getShardedJedisPool().getResource();
		String redisKey = getUserNormalIPKey(user);
		return shardedJedis.sismember(redisKey, ip);
	}
	/**
     * 保存用户常用的收货地址，将所有的收货地址code存放在redis的set中
     * key：om:{user}:normal:addressCode
     * value set:code1,code2,code3,code4,code5
     *
     * @param user
     * @return
     */
    private static String getUserNormalAddressKey(String user) {
        return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.NORMAL_ADDRESSS;
    }
	/**
	 * 
	 * @MethodName:getUserNormalIPKey
	 * @Description:保存用户常用的Ip地址,将所有的Ip地址存放在redis的set中,返回Redis中保存的用户Ip地址的key
	 * Key：om:{user}:normail:ip
	 * value: set:ip1,ip2,ip3,ip3
	 * @param user
	 * @return
	 * @Time: 2017年6月28日 上午9:52:00
	 * @author: TOM
	 */
	private static String getUserNormalIPKey(String user) {
		return RedisConstant.ORDER_MONITOR + RedisConstant.COLON + user + RedisConstant.NORMAL_IP;
	}
}
