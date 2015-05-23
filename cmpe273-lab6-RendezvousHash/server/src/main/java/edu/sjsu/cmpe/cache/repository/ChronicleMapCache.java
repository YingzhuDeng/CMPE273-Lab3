package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChronicleMapCache implements CacheInterface{

      ChronicleMapBuilder<Long, Entryi CMBuilder;
      ChronicleMap<Long, Entry> map;

      public ChronicleMapCache(String serverUrl){
        try {
        	String serverName = getServerName(serverUrl);
        	String fileName = getFileName(serverName);
        	File file = new File(fileName);
                CMBuilder = ChronicleMapBuilder.of(Long.class, Entry.class);
                map = CMBuilder.createPersistedTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 


    private String getServerName (String serverName){
      	  String[] split = serverName.split("/");
    	  String serverSplit = split[1];
    	  String[] split2 = serverSplit.split("_");
    	  String realServerName = split2[0] + "_" + split2[1];
    	  System.out.println("The Server Name is: "  + realServerName);
    	  return realServerName;
     }


   
    private String getFileName(String serverName){
    	if(serverName.equals("server_A"))
    		return "datFile_A.dat";
    	else if(serverName.equals("server_B"))
    		return "datFile_B.dat";
    	else if(serverName.equals("server_C"))
    		return "datFile_C.dat";
    	throw new IllegalArgumentException("ServerName is incorrect :" + serverName);
    }
 

    @Override
    public Entry save(Entry entry){
        checkNotNull(entry, "The entry should not be null");
        map.putIfAbsent(entry.getKey(),entry);
        return entry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected > 0 ", key);
        	return map.get(key);
    }


    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(map.values());
    }



}
