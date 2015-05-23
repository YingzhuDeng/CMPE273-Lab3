package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;


// Created by Yingzhu
 
public class ConsistentHash<T> {

    private final HashFunction hashF;
    private final int replicas;
    private final SortedMap<Long, T> ring = new TreeMap<Long, T>();


    public ConsistentHash(HashFunction hashF, int replicas, Collection<T> nodes) {
        this.hashF = hashF;
        this.replicas = replicas;

        for (T node : nodes) {
            add(node);
        }
    }


    public void add(T node) {
        for (int i = 0; i < replicas; i++) {
            Long l=hashF.hashString(node.toString() + i).asLong();
            ring.put(l, node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < replicas; i++) {
            ring.remove(hashF.hashString(node.toString() + i).asLong());
        }
    }


    public T get(Object key) {

        if (ring.isEmpty()) {
            return null;
        }

        long hash = hashF.hashString(key.toString()).asLong();

        if (!ring.containsKey(hash)) {
            SortedMap<Long, T> tailMap = ring.tailMap(hash);
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }

        return ring.get(hash);
    }
}
