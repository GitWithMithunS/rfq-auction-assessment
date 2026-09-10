# API

Auction service exposes `POST /api/auctions`, `GET /api/auctions`, `GET /api/auctions/{id}`, and `GET /api/auctions/{id}/activity`. Its internal bid endpoints are `GET /internal/auctions/{id}/bid-context` and `POST /internal/auctions/{id}/events`.

Bid service exposes `POST /api/suppliers`, `POST /api/bids`, `GET /api/auctions/{id}/bids`, `GET /api/auctions/{id}/rankings`, and `GET /api/auctions/{id}/summary`.

All timestamps are ISO-8601 UTC instants. Bid totals are the exact decimal sum of freight, origin, and destination charges.
