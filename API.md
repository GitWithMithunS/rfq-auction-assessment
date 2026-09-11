# API

All timestamps are ISO-8601 UTC instants. Monetary fields are decimals; `totalAmount` is calculated as freight + origin + destination.

## Auction service (`:8081`)

### `POST /api/auctions` — create RFQ and auction

Required body fields are `referenceId`, `name`, `serviceDate`, `bidStartTime`, `bidCloseTime`, `forcedBidCloseTime`, positive `triggerWindowMinutes`, positive `extensionDurationMinutes`, and one or more enabled trigger booleans.

```json
{"referenceId":"RFQ-100","name":"Ocean freight","serviceDate":"2026-10-01T00:00:00Z","bidStartTime":"2026-09-11T10:00:00Z","bidCloseTime":"2026-09-11T11:00:00Z","forcedBidCloseTime":"2026-09-11T12:00:00Z","triggerWindowMinutes":10,"extensionDurationMinutes":5,"extendOnBidReceived":true,"extendOnRankChange":true,"extendOnL1Change":true}
```

Returns `201` with auction `id`, RFQ data, state, and timings. Returns `400` for invalid/missing values, a non-later forced close, a non-later bid close, duplicate reference, or no enabled trigger.

### Read endpoints

- `GET /api/auctions` — list created auctions.
- `GET /api/auctions/{id}` — auction/RFQ configuration and state.
- `GET /api/auctions/{id}/activity` — activity entries with type, event time, resulting close time, and reason.

Each returns `404` when the requested auction is absent. Typical auction response fields are `id`, `rfqId`, `referenceId`, `name`, `state`, `bidStartTime`, `currentCloseTime`, `forcedCloseTime`, `triggerWindowMinutes`, and `extensionDurationMinutes`.

### Internal bid endpoints

- `GET /internal/auctions/{id}/bid-context` — bid service reads authoritative state/start/current close/forced close.
- `POST /internal/auctions/{id}/events` — accepts `eventId`, `occurredAt`, `bidReceived`, `rankChanged`, and `l1Changed`; returns `{ "extended": true, "currentCloseTime": "..." }` or `{ "extended": false, "reason": "..." }`.

These are internal service-to-service APIs, not intended as public client APIs. Duplicate event IDs are ignored.

## Bid service (`:8082`)

### `POST /api/suppliers` — create supplier

```json
{"name":"Carrier A"}
```

Returns `201` with `id`, `name`, and `active`. Blank or duplicate names return `400`.

### `POST /api/bids` — submit quote

```json
{"auctionId":"00000000-0000-0000-0000-000000000001","supplierId":"00000000-0000-0000-0000-000000000002","freightCharges":100.00,"originCharges":10.00,"destinationCharges":5.00,"transitTimeDays":7,"quoteValidUntil":"2026-10-01T00:00:00Z"}
```

Returns `201` with the stored bid, calculated `totalAmount`, and supplier `rank`. Returns `400` for inactive suppliers, unavailable/inactive/closed auctions, non-positive total, or equal/worse than that supplier's latest bid; `404` for an unknown supplier. A successful bid causes bid service to notify auction service with bid/rank/L1 change information.

### Bid reads

- `GET /api/auctions/{id}/bids` — all bid history sorted by total amount.
- `GET /api/auctions/{id}/rankings` — latest bid per supplier, ordered L1 onward, with `rank`.
- `GET /api/auctions/{id}/summary` — `auctionId`, `currentLowestBid`, and (when present) `supplierId`.
