/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.infinispan.quickstart.embeddedcache;

import java.util.Map;
import org.infinispan.Cache;
import org.infinispan.atomic.AtomicHashMap;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.TransactionMode;

/**
 *
 * @author Fábio Coelho
 */
public class Node1 extends AbstractNode{
    
   // EmbeddedCacheManager manager;
   // public static final int CLUSTER_SIZE = 2;
    
    public static void main (String []args) throws InterruptedException{
        new Node1().run();
    }
    
    public void run() throws InterruptedException{
        /*
        //configuration trasnport layer for clustered cache:
      GlobalConfiguration gc = new GlobalConfigurationBuilder().transport().defaultTransport()
                                    .addProperty("configurationFile", "jgroups.xml")
                                    .clusterName("InfinispanTestting")
                                    .machineId("Machine1")
                                    .rackId("rack1")
                                    .build();
       
      //creating the new clustered cache: 
      manager = new DefaultCacheManager(gc);
      
      Configuration conf = new ConfigurationBuilder().clustering()
                                .clustering()
                                .cacheMode(CacheMode.REPL_SYNC)
                              //  .transaction().transactionMode(TransactionMode.TRANSACTIONAL)
                                .build();
      
      
     
      
      manager.defineConfiguration(cacheName, conf);
       
      */          
      String cacheName = "distributedInfinispan";
      Cache<Object, Object> cache = getCacheManager().getCache(cacheName);
      //VersionedCacheImpl<Object,Object> cache = new VersionedCacheImpl<Object,Object>();
      
      
      cache.addListener(new LoggingListener());

      waitForClusterToForm();
      
      
      
      
      
      
      Thread.sleep(20000);
      
      System.out.println("Testing Cache for key presence...");
//      System.out.println(cache.get("k"+9999).toString());
      //AtomicHashMap<String,Object> res = (AtomicHashMap<String,Object>) cache.get("k"+9999);
      //System.out.println(res.get(""));
      
      System.out.println(cache.get("key"));
      //System.out.println("Testing results inserted: "+ cache.getRange("k", 9000, 9999));
      //System.out.println("Testing results inserted: "+ cache.getUpTo("k", Long.parseLong("2")));
      System.out.println("Testing results inserted: k1"+ cache.getRange("k"+1, 1, 20));
      System.out.println("Testing results inserted: k9999"+ cache.getRange("k"+9999, 1, 20));
      
      Thread.sleep(20000);
      
      System.out.println("Testing Cache for key presence...");
      System.out.println(cache.get("k"+9999));
      System.out.println(cache.get("key"));
      //System.out.println("Testing results inserted: "+ cache.getRange("k", 9000, 9999));

    }
    
   
   protected int getNodeId(){
       return 1;
   }
            
            
    
}

