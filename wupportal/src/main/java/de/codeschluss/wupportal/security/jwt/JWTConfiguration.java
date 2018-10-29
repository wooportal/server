package de.codeschluss.wupportal.security.jwt;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JWTConfiguration {
	
	@DurationUnit(ChronoUnit.HOURS)
	private Duration expirationTime = Duration.ofHours(12);
	
	private String secret;
	private String claimSuperuser;
	private String claimProviders;
	private String claimAdminOrgas;
}