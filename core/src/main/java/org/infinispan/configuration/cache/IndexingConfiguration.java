package org.infinispan.configuration.cache;

import static org.infinispan.configuration.parsing.Element.INDEXING;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.infinispan.commons.configuration.AbstractTypedPropertiesConfiguration;
import org.infinispan.commons.configuration.ConfigurationInfo;
import org.infinispan.commons.configuration.attributes.Attribute;
import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.attributes.CollectionAttributeCopier;
import org.infinispan.commons.configuration.attributes.Matchable;
import org.infinispan.commons.configuration.elements.DefaultElementDefinition;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.commons.util.TypedProperties;

/**
 * Configures indexing of entries in the cache for searching.
 */
public class IndexingConfiguration extends AbstractTypedPropertiesConfiguration implements Matchable<IndexingConfiguration>, ConfigurationInfo {
   @Deprecated
   public static final AttributeDefinition<Index> INDEX = AttributeDefinition.builder("index", null, Index.class).immutable().build();
   public static final AttributeDefinition<Boolean> ENABLED = AttributeDefinition.builder("enabled", false).immutable().build();
   public static final AttributeDefinition<Boolean> AUTO_CONFIG = AttributeDefinition.builder("autoConfig", false).immutable().build();
   public static final AttributeDefinition<Map<Class<?>, Class<?>>> KEY_TRANSFORMERS = AttributeDefinition.builder("key-transformers", null, (Class<Map<Class<?>, Class<?>>>) (Class<?>) Map.class)
         .copier(CollectionAttributeCopier.INSTANCE)
         .initializer(HashMap::new).immutable().build();
   public static final AttributeDefinition<Set<Class<?>>> INDEXED_ENTITIES = AttributeDefinition.builder("indexed-entities", null, (Class<Set<Class<?>>>) (Class<?>) Set.class)
         .copier(CollectionAttributeCopier.INSTANCE)
         .initializer(HashSet::new).immutable().build();

   static AttributeSet attributeDefinitionSet() {
      return new AttributeSet(IndexingConfiguration.class, AbstractTypedPropertiesConfiguration.attributeSet(), INDEX, AUTO_CONFIG, KEY_TRANSFORMERS, INDEXED_ENTITIES, ENABLED);
   }

   static final ElementDefinition<IndexingConfiguration> ELEMENT_DEFINITION = new DefaultElementDefinition<>(INDEXING.getLocalName());

   private static final String DIRECTORY_PROVIDER_KEY = "directory_provider";

   /**
    * Legacy name "ram" was replaced by "local-heap"
    */
   @Deprecated
   private static final String RAM_DIRECTORY_PROVIDER = "ram";
   private static final String LOCAL_HEAP_DIRECTORY_PROVIDER = "local-heap";
   private static final String LOCAL_HEAP_DIRECTORY_PROVIDER_FQN = "org.hibernate.search.store.impl.RAMDirectoryProvider";

   /**
    * @deprecated since 11.
    */
   @Deprecated
   private final Attribute<Index> index;
   private final Attribute<Boolean> autoConfig;
   private final Attribute<Map<Class<?>, Class<?>>> keyTransformers;
   private final Attribute<Set<Class<?>>> indexedEntities;
   private final Attribute<Boolean> enabled;

   IndexingConfiguration(AttributeSet attributes) {
      super(attributes);
      index = attributes.attribute(INDEX);
      autoConfig = attributes.attribute(AUTO_CONFIG);
      keyTransformers = attributes.attribute(KEY_TRANSFORMERS);
      indexedEntities = attributes.attribute(INDEXED_ENTITIES);
      enabled = attributes.attribute(ENABLED);
   }

   @Override
   public ElementDefinition getElementDefinition() {
      return ELEMENT_DEFINITION;
   }

   /**
    * <p>
    * These properties are passed directly to the embedded Hibernate Search engine, so for the
    * complete and up to date documentation about available properties refer to the Hibernate Search
    * reference of the version you're using with Infinispan Query.
    * </p>
    *
    * @see <a
    *      href="http://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/">Hibernate
    *      Search</a>
    */
   @Override
   public TypedProperties properties() {
      // Overridden to replace Javadoc
      return super.properties();
   }

   /**
    * Returns the indexing mode of this cache.
    *
    * @deprecated Since 11. This configuration will be removed in next major version as the index mode is calculated
    * automatically.
    */
   @Deprecated
   public Index index() {
      return index.get();
   }

   /**
    * Determines if indexing is enabled for this cache configuration.
    */
   public boolean enabled() {
      return enabled.get();
   }

   /**
    * Determines if autoconfig is enabled for this IndexingConfiguration.
    */
   public boolean autoConfig() {
      return autoConfig.get();
   }

   /**
    * The currently configured key transformers.
    *
    * @return a {@link Map} in which the map key is the key class and the value is the Transformer class.
    */
   public Map<Class<?>, Class<?>> keyTransformers() {
      return keyTransformers.get();
   }

   public Set<Class<?>> indexedEntities() {
      return indexedEntities.get();
   }

   public AttributeSet attributes() {
      return attributes;
   }

   /**
    * Check if the indexes can be shared. Currently no index can be shared, so it always returns false. sharing.
    *
    * @return always false, starting with version 11
    * @deprecated Since 11 with no replacement; to be removed in next major version.
    */
   @Deprecated
   public final boolean indexShareable() {
      return false;
   }

   @Override
   public String toString() {
      return "IndexingConfiguration [attributes=" + attributes + "]";
   }
}
