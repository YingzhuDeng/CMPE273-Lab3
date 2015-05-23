package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Client...");
        ArrayList<String> cacheNodes = new ArrayList<String>();

        //store nodes in arrayList
        cacheNodes.add("http://localhost:3000");
        cacheNodes.add("http://localhost:3001");
        cacheNodes.add("http://localhost:3002");

	//User the md5() as hash function
        RendezvousHash<String> consistentH = new RendezvousHash<String>(Hashing.md5(),
                1, cacheNodes);

	//97 = ASK a
        for (int count = 1, letter = 97; count <= 10 && letter <= 106; count++, letter++) {
            String cacheNode = consistentH.get(count);
            CacheServiceInterface cache = new DistributedCacheService(cacheNode);
            cache.put(count, String.valueOf((char) letter));

            System.out.println("PUT ==> node "+cacheNode);
            System.out.println("put ==>("+count+","+String.valueOf((char) letter)+")");

            System.out.println("GET ==> node "+cacheNode);
            System.out.println("get(" + count + ") => " + cache.get(count));
        }
        System.out.println("Existing Cache Client...");


    }

}
