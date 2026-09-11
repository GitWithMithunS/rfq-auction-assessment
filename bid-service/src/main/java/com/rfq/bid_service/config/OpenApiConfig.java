package com.rfq.bid_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bidServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RFQ Auction - Bid Service API")
                        .version("1.0")
                        .description("APIs for suppliers, bids, competitive bidding and supplier rankings."));
    }
}