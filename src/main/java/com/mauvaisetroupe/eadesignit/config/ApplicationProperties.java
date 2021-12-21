package com.mauvaisetroupe.eadesignit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to EA Design It.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private boolean securityAnonymousReader = false;

    public boolean isSecurityAnonymousReader() {
        return securityAnonymousReader;
    }

    public void setSecurityAnonymousReader(boolean securityAnonymousReader) {
        this.securityAnonymousReader = securityAnonymousReader;
    }
}
