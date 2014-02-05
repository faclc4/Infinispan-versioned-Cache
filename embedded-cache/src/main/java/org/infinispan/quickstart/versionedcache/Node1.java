/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.infinispan.quickstart.versionedcache;

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
    
   // EmbeddedCacheManager manager:
    Cache<Object, Object> cache;
    
    public static void main (String []args) throws InterruptedException{
        new Node1().run();
    }
    
    public void run() throws InterruptedException{
      String cacheName = "distributedInfinispan";
      cache = getCacheManager().getCache(cacheName);
     
      cache.addListener(new LoggingListener());

      waitForClusterToForm();
      
      System.out.println("Testing Cache for key presence...");
      
      System.out.println(cache.get("key"));
      System.out.println("Testing results inserted: k1"+ cache.getRange("k"+1, 1, 20));
      System.out.println("Testing results inserted: k499"+ cache.getRange("k"+499, 1, 20));
      
      System.out.println("Testing Cache for key presence...");
      System.out.println(cache.get("key"));

    }
    
   
   protected int getNodeId(){
       return 1;
   }
            
            
    
}

