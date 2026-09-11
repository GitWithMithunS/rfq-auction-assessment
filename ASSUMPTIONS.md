# Assumptions

- A supplier is the carrier named in the quote.
- Equal bid totals rank by earlier submission, then ID.
- The extension window is inclusive at `currentClose - X` and exclusive at `currentClose`.
- Quote validity is stored as supplier-provided quote metadata; the PDF does not define a validity acceptance rule.
- The PDF does not specify authentication, currency, RFQ editing/cancellation, or bid withdrawal; none is implemented.
- `BID_RECEIVED`, `RANK_CHANGE`, and `L1_CHANGE` may all be included in one internal bid event. Auction service evaluates enabled triggers once and performs at most one extension for that event.
