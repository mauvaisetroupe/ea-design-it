package com.mauvaisetroupe.eadesignit.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mauvaisetroupe.eadesignit.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mauvaisetroupe.eadesignit.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.User.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Authority.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.LandscapeView.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.LandscapeView.class.getName() + ".flows");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.LandscapeView.class.getName() + ".capabilities");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Owner.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Owner.class.getName() + ".users");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FunctionalFlow.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FunctionalFlow.class.getName() + ".steps");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FunctionalFlow.class.getName() + ".landscapes");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FunctionalFlow.class.getName() + ".dataFlows");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FlowInterface.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FlowInterface.class.getName() + ".dataFlows");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FlowInterface.class.getName() + ".steps");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Application.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Application.class.getName() + ".categories");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Application.class.getName() + ".technologies");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Application.class.getName() + ".capabilities");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Application.class.getName() + ".applicationsLists");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFlow.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFlow.class.getName() + ".items");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFlow.class.getName() + ".functionalFlows");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationComponent.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationComponent.class.getName() + ".categories");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationComponent.class.getName() + ".technologies");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationImport.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FlowImport.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Protocol.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFlowItem.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFormat.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationCategory.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationCategory.class.getName() + ".applications");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.ApplicationCategory.class.getName() + ".components");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.DataFlowImport.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Technology.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Technology.class.getName() + ".applications");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Technology.class.getName() + ".components");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Capability.class.getName());
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Capability.class.getName() + ".subCapabilities");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Capability.class.getName() + ".applications");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.Capability.class.getName() + ".landscapes");
            createCache(cm, com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
