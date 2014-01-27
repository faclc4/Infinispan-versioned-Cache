/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.infinispan.quickstart.embeddedcache;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 *
 * @author Fábio Coelho
 */
public class Node1 {
    
    public static void main (String []args){
        //configuration trasnport layer for clustered cache:
      GlobalConfiguration gc = new GlobalConfigurationBuilder().transport().defaultTransport()
                                    .addProperty("configurationFile", "jgroups.xml")
                                    .clusterName("InfinispanTestting")
                                    .machineId("Machine1")
                                    .rackId("rack1")
                                    .build();
       
      //creating the new clustered cache: 
      EmbeddedCacheManager manager = new DefaultCacheManager(gc);
      
      Configuration conf = new ConfigurationBuilder().clustering()
                                .clustering().cacheMode(CacheMode.DIST_ASYNC).build();
      
      
      String cacheName = "distributedInfinispan";
      
      manager.defineConfiguration(cacheName, conf);
       
      Cache<Object, Object> cache = manager.getCache(cacheName);
      //VersionedCacheImpl<Object,Object> cache = new VersionedCacheImpl<Object,Object>();
    }
            
            
    
}
