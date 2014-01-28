package org.infinispan.quickstart.embeddedcache;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.IOException;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;

@SuppressWarnings("unused")
public abstract class AbstractNode {
   
   private static EmbeddedCacheManager createCacheManagerProgramatically() {
      return new DefaultCacheManager(
            GlobalConfigurationBuilder.defaultClusteredBuilder()
                  .transport().addProperty("configurationFile", "jgroups.xml")
                  .build(),
            new ConfigurationBuilder()
                  .clustering().cacheMode(CacheMode.DIST_ASYNC)
                  .transaction().transactionMode(TransactionMode.TRANSACTIONAL)
                  .autoCommit(true)
                    .transactionManagerLookup(new GenericTransactionManagerLookup())
                  .build()
      );
   }

   private static EmbeddedCacheManager createCacheManagerFromXml() throws IOException {
      return new DefaultCacheManager("infinispan-replication.xml");
   }
   
   public static final int CLUSTER_SIZE = 2;

   private final EmbeddedCacheManager cacheManager;
   
   public AbstractNode() {
      this.cacheManager = createCacheManagerProgramatically();
      // Uncomment to create cache from XML
      // try {
      //    this.cacheManager = createCacheManagerFromXml();
      // } catch (IOException e) {
      //    throw new RuntimeException(e);
      // }
   }
   
   protected EmbeddedCacheManager getCacheManager() {
      return cacheManager;
   }
   
   protected void waitForClusterToForm() {
      // Wait for the cluster to form, erroring if it doesn't form after the
      // timeout
      if (!ClusterValidation.waitForClusterToForm(getCacheManager(), getNodeId(), CLUSTER_SIZE)) {
         throw new IllegalStateException("Error forming cluster, check the log");
      }
   }
   
   protected abstract int getNodeId();
   
}