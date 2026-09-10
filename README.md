# RFQ British Auction Backend

Three Spring Boot 4.1.1 services implement the assessment backend: Eureka (`8761`), auction service (`8081`, `auction_db`), and bid service (`8082`, `bid_db`). Start Eureka, auction service, then bid service. Each service has its own Maven wrapper.

Set `AUCTION_DB_URL`, `BID_DB_URL`, credentials, and `EUREKA_URL` as needed. The default URLs target local MySQL 8 databases. `AUCTION_SERVICE_URL` defaults to the Eureka service id `http://auction-service`.

No frontend is included.
