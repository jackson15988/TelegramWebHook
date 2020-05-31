package telegram.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class RedisUtil {

	public static void setRedis(String key, JSONObject json) {
		JedisShardInfo shardInfo = new JedisShardInfo("redis://45.32.49.87:6379/0");// 这里是连接的本地地址和端口
		shardInfo.setPassword("asBP43Mg");// 这里是密码
		Jedis jedis = new Jedis(shardInfo);
		try {
			jedis.connect();
			HashMap<String, String> hsdata = new HashMap<>();
			hsdata.put(key, json.toJSONString());
			jedis.hmset("ORDER", hsdata);
			System.out.println("连接成功");
		} catch (Exception e) {
			System.out.println("Redis Connection Error !!" + e);
		} finally {
			jedis.close();
		}
	}

	public static JSONObject getRedis(String key) {
		JSONObject jsOBj = new JSONObject();
		JedisShardInfo shardInfo = new JedisShardInfo("redis://45.32.49.87:6379/0");// 这里是连接的本地地址和端口
		shardInfo.setPassword("asBP43Mg");// 这里是密码
		Jedis jedis = new Jedis(shardInfo);
		try {
			jedis.connect();
			String jsonStr = jedis.hgetAll("ORDER").get(key);

			if (jsonStr != null && !jsonStr.isEmpty()) {
				jsOBj = (JSONObject) jsOBj.parse(jsonStr);
				JSONArray jsonAry = new JSONArray();
				jsonAry = (JSONArray) jsOBj.get("result");
				String jrOBJ = (String) jsonAry.get(0);
				jsOBj = (JSONObject) jsOBj.parse(jrOBJ);
			}
		} catch (Exception e) {
			System.out.println("Redis Connection Error !!" + e);
		} finally {
			jedis.close();
		}
		return jsOBj;

	}
}
