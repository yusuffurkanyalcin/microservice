package com.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator furkanBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(predicateSpec ->
						predicateSpec.path("/furkanbank/accounts/**")
						.filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/furkanbank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker").setFallbackUri("forward:/contactSupport")))
								.uri("lb://ACCOUNTS"))
				.route(predicateSpec ->
						predicateSpec.path("/furkanbank/cards/**")
								.filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/furkanbank/cards/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
								.uri("lb://CARDS"))
				.route(predicateSpec ->
						predicateSpec.path("/furkanbank/loans/**")
								.filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/furkanbank/loans/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
								.uri("lb://LOANS")).build();
	}

}
