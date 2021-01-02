package ca.benfarhat.simplecrud.config;

import java.time.Duration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ca.benfarhat.simplecrud.CacheConstant;


/**
 * 
 * CacheConfig: Configuration pour le cache EhCache / JSR-107 
 * Le caching sous Spring est une abstraction et non une implémentation il existe plusieurs providers de cache:
 * - EhCache
 * - CouchBase
 * - Redis
 * - Pivotal Gemfire
 * - HazelCast
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-01
 * @version 1.0.0
 *
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        return new JCacheCacheManager(cacheManager());
    }

    // https://docs.spring.io/spring-framework/docs/2.5.x/reference/beans.html#beans-factory-lifecycle-disposablebean
    @Bean(value = "cacheManager", destroyMethod = "close")
    public CacheManager cacheManager() {
        CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager cacheManager = provider.getCacheManager();
        CacheConfiguration<Object, Object> cacheConfiguration1000 = getCacheConfiguration(1000, 0);
        CacheConfiguration<Object, Object> cacheConfiguration500WithExpiry3 = getCacheConfiguration(500, 3);

        createIfNotExists(cacheManager, CacheConstant.CST_CACHE_TUTORIAL, cacheConfiguration1000);
        createIfNotExists(cacheManager, CacheConstant.CST_CACHE_TUTORIAL_PUBLISHED, cacheConfiguration1000);
        createIfNotExists(cacheManager, CacheConstant.CST_CACHE_TUTORIAL_BY_TITLE, cacheConfiguration500WithExpiry3);
        return cacheManager;
    }
    
    private CacheConfiguration<Object, Object> getCacheConfiguration(int heapSize, int ttlInSeconds) {
    	ResourcePoolsBuilder rpb = ResourcePoolsBuilder.heap(heapSize);
    	CacheConfigurationBuilder<Object, Object> ccb = CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class, rpb);
    	if (ttlInSeconds > 0) {
    		ExpiryPolicy<Object, Object> epb = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ttlInSeconds));
    		ccb.withExpiry(epb);
    	}
    	return ccb.build();	
    }
    
    /**
     * Vérification lors de la création du cache
     * Lors des tests par exemple avec SpringBootTest, il arrive qu'on reload le context plusieurs fois, 
     * le soucis avec le cache c'est que cela va créer des caches qui existent déja, il y a deux solutions:
     * Soit desactiver le cache dans le fichier application.properties/yaml des tests en mettant spring.cache.type=none
     * Soit lors de la création on vérifie si le cache existe ou pas
     * 
     * @param cacheManager le gestionnaire de cache
     * @param cacheName le nom du cache
     * @param cacheConfiguration la configuration a utiliser
     */
    private void createIfNotExists(CacheManager cacheManager, String cacheName, CacheConfiguration<Object, Object> cacheConfiguration) {
        if(cacheManager.getCache(cacheName) == null) {
            cacheManager.createCache(cacheName, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration));
        }
    }

    @CacheEvict(allEntries = true, cacheNames = {
    		CacheConstant.CST_CACHE_TUTORIAL_PUBLISHED,
    		CacheConstant.CST_CACHE_TUTORIAL_BY_TITLE
    })
    @Scheduled(fixedRate = 10_000)
    public void cacheEvictSchedule() {
    	// Eviction programmé du cache toute les 10 secondes
    }
}
