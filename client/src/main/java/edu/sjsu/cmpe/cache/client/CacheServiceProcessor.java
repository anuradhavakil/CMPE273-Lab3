package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.Hashing;

public class CacheServiceProcessor {
	private SortedMap<Integer,CacheServiceInterface> cacheServerMap = new TreeMap<Integer,CacheServiceInterface>();

	
	public void add(CacheServiceInterface cacheServer) {
		
		int index = cacheServer.toString().lastIndexOf(':');
		int key = Integer.parseInt(cacheServer.toString().substring(index + 1));
		cacheServerMap.put(Hashing.md5().hashLong(key).asInt(), cacheServer);
	}
	
	public CacheServiceInterface get(int key) {
		
		int hash = Hashing.md5().hashLong(key).asInt();
		
		//choose server
		if (!cacheServerMap.containsKey(hash)) {
			SortedMap<Integer, CacheServiceInterface> tailMap = cacheServerMap.tailMap(hash);
			hash = tailMap.isEmpty() ? cacheServerMap.firstKey() : tailMap.firstKey();
		}
		
		return cacheServerMap.get(hash); 
	}
	
	public int getSize() {
		return cacheServerMap.size();
	}

}
