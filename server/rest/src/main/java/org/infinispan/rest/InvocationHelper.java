package org.infinispan.rest;

import java.util.concurrent.Executor;

import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.counter.impl.manager.EmbeddedCounterManager;
import org.infinispan.marshall.core.EncoderRegistry;
import org.infinispan.rest.cachemanager.RestCacheManager;
import org.infinispan.rest.configuration.RestServerConfiguration;
import org.infinispan.server.core.ServerManagement;

/**
 * @since 10.0
 */
public class InvocationHelper {
   private final ParserRegistry parserRegistry = new ParserRegistry();
   private final RestCacheManager<Object> restCacheManager;
   private final EmbeddedCounterManager counterManager;
   private final RestServerConfiguration configuration;
   private final ServerManagement server;
   private final Executor executor;
   private final RestServer protocolServer;
   private final EncoderRegistry encoderRegistry;

   InvocationHelper(RestServer protocolServer, RestCacheManager<Object> restCacheManager, EmbeddedCounterManager counterManager,
                    RestServerConfiguration configuration, ServerManagement server, Executor executor) {
      this.protocolServer = protocolServer;
      this.restCacheManager = restCacheManager;
      this.counterManager = counterManager;
      this.configuration = configuration;
      this.server = server;
      this.executor = executor;
      this.encoderRegistry = restCacheManager.getInstance().getGlobalComponentRegistry().getComponent(EncoderRegistry.class);
   }

   public ParserRegistry getParserRegistry() {
      return parserRegistry;
   }

   public RestCacheManager<Object> getRestCacheManager() {
      return restCacheManager;
   }

   public RestServerConfiguration getConfiguration() {
      return configuration;
   }

   public Executor getExecutor() {
      return executor;
   }

   public ServerManagement getServer() {
      return server;
   }

   public EmbeddedCounterManager getCounterManager() {
      return counterManager;
   }

   public String getContext() {
      return configuration.contextPath();
   }

   public RestServer getProtocolServer() {
      return protocolServer;
   }

   public EncoderRegistry getEncoderRegistry() {
      return encoderRegistry;
   }
}
