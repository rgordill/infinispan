package org.infinispan.persistence.jdbc.stringbased;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StoreConfiguration;
import org.infinispan.persistence.jdbc.ManagedConnectionFactoryTest;
import org.infinispan.persistence.jdbc.UnitTestDatabaseManager;
import org.infinispan.persistence.jdbc.common.impl.connectionfactory.ManagedConnectionFactory;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfiguration;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.infinispan.persistence.support.WaitDelegatingNonBlockingStore;
import org.infinispan.test.CacheManagerCallable;
import org.infinispan.test.TestingUtil;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

/**
 * @author Mircea.Markus@jboss.com
 */
@Test (groups = "functional", testName = "persistence.jdbc.stringbased.StringStoreWithManagedConnectionTest")
public class StringStoreWithManagedConnectionTest extends ManagedConnectionFactoryTest {

   @Override
   protected JdbcStringBasedStore createStore() throws Exception {
      return new JdbcStringBasedStore();
   }

   @Override
   protected Configuration buildConfig(ConfigurationBuilder configurationBuilder) {
      JdbcStringBasedStoreConfigurationBuilder storeBuilder = configurationBuilder
            .persistence()
            .addStore(JdbcStringBasedStoreConfigurationBuilder.class);

      storeBuilder.dataSource()
            .jndiUrl(getDatasourceLocation());
      UnitTestDatabaseManager.buildTableManipulation(storeBuilder.table());
      return configurationBuilder.build();
   }

   public void testLoadFromFile() throws Exception {
      TestingUtil.withCacheManager(new CacheManagerCallable(TestCacheManagerFactory.fromXml("configs/managed/str-managed-connection-factory.xml"), true) {
         @Override
         public void call() {
            Cache<String, String> first = cm.getCache("first");
            Cache<String, String> second = cm.getCache("second");

            StoreConfiguration firstCacheLoaderConfig = first.getCacheConfiguration().persistence().stores().get(0);
            assertNotNull(firstCacheLoaderConfig);
            assertTrue(firstCacheLoaderConfig instanceof JdbcStringBasedStoreConfiguration);
            StoreConfiguration secondCacheLoaderConfig = second.getCacheConfiguration().persistence().stores().get(0);
            assertNotNull(secondCacheLoaderConfig);
            assertTrue(secondCacheLoaderConfig instanceof JdbcStringBasedStoreConfiguration);
            WaitDelegatingNonBlockingStore loader = TestingUtil.getFirstStore(first);
            assertTrue(((JdbcStringBasedStore) loader.delegate()).getConnectionFactory() instanceof ManagedConnectionFactory);
         }
      });
   }

   @Override
   public String getDatasourceLocation() {
      return "java:/StringStoreWithManagedConnectionTest/DS";
   }

   @Override
   protected boolean storePurgesAllExpired() {
      return false;
   }
}
