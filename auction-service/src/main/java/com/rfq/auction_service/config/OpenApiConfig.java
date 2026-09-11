package com.rfq.auction_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI auctionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RFQ Auction - Auction Service API")
                        .version("1.0")
                        .description("APIs for RFQ creation, British Auction configuration, auction lifecycle, extensions and activity logs."));
    }
}