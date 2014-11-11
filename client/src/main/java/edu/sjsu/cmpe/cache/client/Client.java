package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.Hashing;

public class Client {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		CacheServiceInterface cache1 = new DistributedCacheService(
				"http://localhost:3000");
		CacheServiceInterface cache2 = new DistributedCacheService(
				"http://localhost:3001");
		CacheServiceInterface cache3 = new DistributedCacheService(
				"http://localhost:3002");
		
	//	System.out.println("Cache Client.cache1"+cache1.toString());
			
		CacheServiceProcessor cacheServiceChooser = new CacheServiceProcessor();

		cacheServiceChooser.add(cache1);

		cacheServiceChooser.add(cache2);

		cacheServiceChooser.add(cache3);

		Map<Integer, String> data = new HashMap<Integer, String>();
		data.put(1, "a");
		data.put(2, "b");
		data.put(3, "c");
		data.put(4, "d");
		data.put(5, "e");
		data.put(6, "f");
		data.put(7, "g");
		data.put(8, "h");
		data.put(9, "i");
		data.put(10, "j");
		
		String dataValue = "";

		CacheServiceInterface cache = null;

		int bucket = 0;

		for (int i = 1; i < 11; i++) {
			dataValue = data.get(i);
			bucket = Hashing.consistentHash(
					Hashing.md5().hashString(Integer.toString(i)),
					cacheServiceChooser.getSize());
			cache = cacheServiceChooser.get(bucket);

			System.out.println("Put value=" + dataValue
					+ " into cacheInterface => " + cache.toString());
			cache.put(i, dataValue);

		}

		for (int j = 1; j <= 10; j++) {
			dataValue = data.get(j);
			bucket = Hashing.consistentHash(
					Hashing.md5().hashString(Integer.toString(j)),
					cacheServiceChooser.getSize());
			cache = cacheServiceChooser.get(bucket);

			System.out.println("Get data from cacheInterface => "
					+ cache.toString() + ", value = " + cache.get(j));
		}

		System.out.println("Exiting Cache Client...");
	}

}
