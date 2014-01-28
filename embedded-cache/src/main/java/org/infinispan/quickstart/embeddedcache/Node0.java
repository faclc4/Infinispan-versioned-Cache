/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.quickstart.embeddedcache;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.infinispan.Cache;
import org.infinispan.atomic.AtomicHashMap;
import org.infinispan.atomic.AtomicMap;
import org.infinispan.atomic.AtomicMapLookup;
import org.infinispan.manager.DefaultCacheManager;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

public class Node0 extends AbstractNode{
    
    //EmbeddedCacheManager manager;
    private Log log = LogFactory.getLog(LoggingListener.class);
    //public static final int CLUSTER_SIZE = 2;

   public static void main(String args[]) throws Exception {
       new Node0().run();
   }
   
   public void run(){
      //configuration trasnport layer for clustered cache:
       /*
      GlobalConfiguration gc = new GlobalConfigurationBuilder().transport().defaultTransport()
                                    .addProperty("configurationFile", "jgroups.xml")
                                    .clusterName("InfinispanTestting")
                                    .machineId("Machine0")
                                    .rackId("rack1")
                                    .build();
       
      //creating the new clustered cache: 
      manager = new DefaultCacheManager(gc);
      
      Configuration conf = new ConfigurationBuilder().clustering()
                                .clustering()
                                .cacheMode(CacheMode.REPL_SYNC)
                                //.transaction().transactionMode(TransactionMode.TRANSACTIONAL)
                                .build();
      
      
      
      
      manager.defineConfiguration(cacheName, conf);
       
      */ 
      String cacheName = "distributedInfinispan"; 
      Cache<Object, Object> cache = getCacheManager().getCache(cacheName);
      
      waitForClusterToForm();
      
       log.info("About to put key, value into cache on node " + getNodeId());
      
      System.out.println(Boolean.getBoolean("infinispan.clustered"));
      
      long version = System.currentTimeMillis();
      cache.put("key2", "value2", version);
      
      cache.put("key2", "value3", version+1);
      // Add a entry
      cache.put("key", "value");
      // Validate the entry is now in the cache
      //assertEqual(2, cache.size());
      //assertTrue(cache.containsKey("key"));
      // Remove the entry from the cache
      Object v = cache.remove("key");
      // Validate the entry is no longer in the cache
      //assertEqual("value", v);
      //assertTrue(cache.isEmpty());

      // Add an entry with the key "key"
      cache.put("key", "value");
      // And replace it if missing
      cache.putIfAbsent("key", "newValue");
      // Validate that the new value was not added
      //assertEqual("value", cache.get("key"));

      //cache.clear();
      //assertTrue(cache.isEmpty());

      //By default entries are immortal but we can override this on a per-key basis and provide lifespans.
      //cache.put("key", "value", 5, SECONDS);
      //assertTrue(cache.containsKey("key"));
      //Thread.sleep(10000);
      //assertFalse(cache.containsKey("key"));
      
      
      System.out.println("Testing CacheGet:");
      Object aux = cache.get("key");
      System.out.println("key: "+ aux.toString());
      
      System.out.println("Testing CacheGet Range versions:");
      Object aux2 = cache.getRange("key2", version-1, version+2);
     
      System.out.println("key: "+ aux2.toString());
      
      System.out.println("Testing CacheGet UpTO versions:");
      Object aux3 = cache.getUpTo("key2", version+3);
     
      System.out.println("key: "+ aux3.toString());
      
      //--------------------------------------------------------------
      //--------------------------------------------------------------
      /*
      cache.clear();
      
      System.out.println("Testing: ... Infinispan puts:");
      List results = new LinkedList();
      for(int i=1; i<10000;i++){
          long temp1 = System.nanoTime();
          
          put(cache,"k"+i,"teste");
          //cache.put("k"+i, "teste");
          
          long temp2 = System.nanoTime();
          results.add(temp2-temp1);
      }
      System.out.print("Average Infinispan put: ");
      long average = 0;
      
      for (int j = 0; j< results.size(); j++){
          average = average + Long.parseLong(results.get(j).toString());
      }
      double finalresults =(average / 10000); 
      System.out.println( finalresults * 0.000001 +" ms.");
      
      AtomicHashMap<String,Object> res = (AtomicHashMap<String,Object>) cache.get("k"+9999);
      System.out.println(res.get(""));
      
      */
      //--------------------------------------------------------------
      /*
      cache.clear();
      
      System.out.println("Testing: ... Infinispan puts:");
      List results2 = new LinkedList();
      int k = 0;
      for(int i=1; i<10000;i++){
          long temp1 = System.nanoTime();
          
          //put(cache,"k"+i,value);
          cache.put("k"+i, "teste");
          
          long temp2 = System.nanoTime();
          results2.add(temp2-temp1);
      }
      System.out.print("Average Infinispan put: ");
      long average2 = 0;
      
      for (int j = 0; j< results2.size(); j++){
          average2 = average2 + Long.parseLong(results2.get(j).toString());
      }
      double finalresults2 =(average2 / 10000); 
      System.out.println( finalresults2 * 0.000001 +" ms.");
      
      System.out.println(cache.get("k"+9999));
      
      */
      //--------------------------------------------------------------
      //--------------------------------------------------------------
      
      
      cache.clear();
      
      System.out.println("Testing: ... Infinispan MVCC puts:");
      List results3 = new LinkedList();
      int k2 = 0;
      
      for(long i=1; i<10000;i++){
          long tempx1 = System.nanoTime();
          
          //cache.put(k+i, value);
          //cache.put(k2, "teste"+i, i);
          put(cache,"k","teste",i);
          
          long tempx2 = System.nanoTime();
          results3.add(tempx2-tempx1);
          //Thread.sleep(1);
      }
      System.out.print("Average Infinispan MVCC put: ");
      long average3 = 0;
      
      for (int j = 0; j< results3.size(); j++){
          average3 = average3 + Long.parseLong(results3.get(j).toString());
      }
      double finalresults3 =(average3 / 10000); 
      System.out.println( finalresults3 * 0.000001 +" ms.");
      
      long auxx = 10000;
      long auxx1= 9900;
      
      //System.out.println("Testing results inserted: "+ cache.getUpTo(k2, auxx));
      System.out.println("Testing results inserted: "+ cache.getRange("k", auxx1, auxx));
      
      
      //--------------------------------------------------------------
      /*
      cache.clear();
      
      System.out.println("Testing: ... Infinispan MVCC puts:");
      List results4 = new LinkedList();
      //int k3 = 0;
      
      for(long i=1; i<10000;i++){
          long tempx1 = System.nanoTime();
          
          //cache.put(k+i, value);
          cache.put("k", "teste"+i, i);          
          //put(cache,"k","teste"+i,i);
          
          long tempx2 = System.nanoTime();
          results4.add(tempx2-tempx1);
          //Thread.sleep(1);
      }
      System.out.print("Average Infinispan MVCC put: ");
      long average4 = 0;
      
      for (int j = 0; j< results4.size(); j++){
          average4 = average4 + Long.parseLong(results4.get(j).toString());
      }
      double finalresults4 =(average4 / 10000); 
      System.out.println( finalresults4 * 0.000001 +" ms.");
      
      long auxx = 10000;
      long auxx1= 9900;
      
      //System.out.println("Testing results inserted: "+ cache.getUpTo(k2, auxx));
      System.out.println("Testing results inserted: "+ cache.getRange("k", auxx1, auxx));
      */        
      
      //--------------------------------------------------------------
      
      cache.clear();
      
      System.out.println("Testing: ... Infinispan MVCC puts:");
      List results5 = new LinkedList();
      
      for(long j =1; j < 10000;j++){
        for(long i=1; i<20;i++){
            long tempx1 = System.nanoTime();

            //cache.put(k+i, value);
            //cache.put(k2, "teste"+i, i);
            put(cache,"k"+j,"teste",i);

            long tempx2 = System.nanoTime();
            results5.add(tempx2-tempx1);
            //Thread.sleep(1);
        }
      }
      System.out.print("Average Infinispan MVCC put: ");
      long average5 = 0;
      
      for (int j = 0; j< results5.size(); j++){
          average5 = average5 + Long.parseLong(results5.get(j).toString());
      }
      double finalresults5 =(average5 / 10000); 
      System.out.println( finalresults5 * 0.000001 +" ms.");
      
      long auxx5 = 10000;
      long auxx55= 9900;
      
      //System.out.println("Testing results inserted: "+ cache.getUpTo(k2, auxx));
      System.out.println("Testing results inserted: k1"+ cache.getRange("k"+1, 1, 20));
      System.out.println("Testing results inserted: k10000"+ cache.getRange("k"+9999, 1, 20));
      
              
   }
   
   public static void put (Cache cache, String key, Object value,long version){
       AtomicMap<Long, Object> row = AtomicMapLookup.getAtomicMap(cache, key);
       row.put(version, value);
   }
   
   public static void put(Cache cache, String key, Object value){
       AtomicMap<String, Object> row = AtomicMapLookup.getAtomicMap(cache, key);
       row.put("", value);
   }
 
   
   protected int getNodeId(){
       return 0;
   }
   

}

