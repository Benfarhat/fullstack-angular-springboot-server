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

        cacheManager.createCache(CacheConstant.CST_CACHE_TUTORIAL, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration1000));

        cacheManager.createCache(CacheConstant.CST_CACHE_TUTORIAL_PUBLISHED, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration1000));
        cacheManager.createCache(CacheConstant.CST_CACHE_TUTORIAL_BY_TITLE, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration500WithExpiry3));
        
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

    @CacheEvict(allEntries = true, cacheNames = {
    		CacheConstant.CST_CACHE_TUTORIAL_PUBLISHED,
    		CacheConstant.CST_CACHE_TUTORIAL_BY_TITLE
    })
    @Scheduled(fixedRate = 10_000)
    public void cacheEvictSchedule() {
    	// Eviction programmé du cache toute les 10 secondes
    }
}
