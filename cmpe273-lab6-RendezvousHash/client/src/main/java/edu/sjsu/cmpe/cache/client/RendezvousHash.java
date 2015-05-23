package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;


//  Created by Yingzhu.
 
public class RendezvousHash<T> {
    /**
     * Hash function: Hashing.md5()
     */
    private final HashFunction hasher;

    private final int replicas;
    private final ConcurrentSkipListSet<T> keypairList;

    public RendezvousHash(HashFunction hasher,int replicas , Collection<T> keypair) {
        if (hasher == null) throw new NullPointerException("hasher");
        if (keypair == null) throw new NullPointerException("keypair");
        this.hasher = hasher;
        this.replicas=replicas;
        this.keypairList = new ConcurrentSkipListSet<T>(keypair);
    }

    public void remove(T node) {
        keypairList.remove(node);
    }

    public void add(T node) {
         keypairList.add(node);
    }

    public T get(Object key) {
        long maxValue = Long.MIN_VALUE;
        T max = null;
        for (T node : keypairList) {
            long nodesHash=hasher.newHasher().putString(key.toString())
                        .putString(node.toString()).hash().asLong();

            if (nodesHash > maxValue) {
                max = node;
                maxValue = nodesHash;
            }
        }
        return max;
    }
}
