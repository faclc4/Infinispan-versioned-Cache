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
package org.infinispan.quickstart.versionedcache;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.infinispan.Cache;
import org.infinispan.atomic.AtomicHashMap;
import org.infinispan.atomic.AtomicMap;
import org.infinispan.atomic.AtomicMapLookup;

import static org.infinispan.quickstart.versionedcache.util.Assert.assertEqual;
import static org.infinispan.quickstart.versionedcache.util.Assert.assertFalse;
import static org.infinispan.quickstart.versionedcache.util.Assert.assertTrue;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

public class Node0 extends AbstractNode{
    
    //EmbeddedCacheManager manager;
    private Log log = LogFactory.getLog(LoggingListener.class);
    
    Cache<Object, Object> cache;

   public static void main(String args[]) throws Exception {
       new Node0().run();
   }
   
   public void run(){
      //configuration trasnport layer for clustered cache:
      String cacheName = "distributedInfinispan"; 
      cache = getCacheManager().getCache(cacheName);
      
      waitForClusterToForm();
      
       log.info("About to put key, value into cache on node " + getNodeId());
      
      
      long version = System.currentTimeMillis();
      cache.put("key2", "value2", version);
      
      cache.put("key2", "value3", version+1);
      // Add a entry
      cache.put("key", "value");
      // Validate the entry is now in the cache
      assertEqual(cache.get("key"),"value");
      // Remove the entry from the cache
      Object v = cache.remove("key");
      // Validate the entry is no longer in the cache
      assertFalse(cache.containsKey("key"));


      cache.clear();
      assertTrue(cache.isEmpty());

      // ------------- TESTS --------------------------
      
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
      
      cache.clear();
      
      System.out.println("Testing: ... Infinispan puts #1:");
      List results = new LinkedList();
      for(int i=1; i<10000;i++){
          long temp1 = System.nanoTime();
          
          put(cache,"k"+i,"teste");
          
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
      
      //--------------------------------------------------------------
      cache.clear();
      
      System.out.println("Testing: ... Infinispan MVCC puts:");
      List results5 = new LinkedList();
      
      for(long j =1; j < 1000;j++){
        for(long i=1; i<10;i++){
            long tempx1 = System.nanoTime();

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
      
      long upTo = 10;
      
      System.out.println("Testing results inserted: "+ cache.getUpTo("k"+499, upTo));
      System.out.println("Testing results inserted: k1"+ cache.getRange("k"+1, 1, 20));
      System.out.println("Testing results inserted: k499"+ cache.getRange("k"+499, 1, 20));
      
              
   }
   
   public static void put (Cache cache, String key, Object value,long version){
       AtomicMap<Long, Object> row = AtomicMapLookup.getAtomicMap(cache, key);
       row.put(version, value);
   }
   
   public static void put(Cache cache, String key, Object value){
       AtomicMap<String, Object> row = AtomicMapLookup.getAtomicMap(cache, key);
       row.put("", value);
   }
   
   public Map<Long, Object> getRange(Object key, long versionA, long versionB) {
        if(versionB < versionA) 
          return null;
      
      Map<Long,Object> aux = (Map<Long, Object>) cache.get(key);
      
      Map<Long,Object> res = new TreeMap<Long,Object>();
      
      for(Map.Entry<Long,Object> value : aux.entrySet()){
          if(value.getKey() == versionA || value.getKey() == versionB)
              res.put(value.getKey(), value.getValue());
          if(value.getKey()< versionB && value.getKey() > versionA)
              res.put(value.getKey(), value.getValue());
      }
     
      return res;
    }  
   
    public Object getUpTo(Object key, Long version) {        
       Map<Long,Object> aux = (Map<Long, Object>) cache.get(key);
      
        long best = 0;
        for(Map.Entry<Long,Object> value : aux.entrySet()){
            if(value.getKey()< version)
                best = value.getKey();
        }  
        return aux.get(best);
    }
 
   
   protected int getNodeId(){
       return 0;
   }
   

}

