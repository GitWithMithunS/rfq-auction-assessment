import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { getAuctions } from "../services/auctionService";
import { getAuctionSummary } from "../services/bidService";
import {
  apiErrorMessage,
  formatCurrency,
  formatDateTime,
} from "../utils/formatters";
import StatusBadge from "../components/common/StatusBadge";
import {
  EmptyState,
  ErrorState,
  LoadingState,
} from "../components/common/PageState";

export default function AuctionListPage() {
  const [auctions, setAuctions] = useState([]);
  const [summaries, setSummaries] = useState({});
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await getAuctions();
      setAuctions(data);
      const values = await Promise.all(
        data.map(async (auction) => {
          try {
            return [auction.id, { data: await getAuctionSummary(auction.id) }];
          } catch (summaryError) {
            return [auction.id, { error: apiErrorMessage(summaryError) }];
          }
        }),
      );
      setSummaries(Object.fromEntries(values));
    } catch (err) {
      setError(apiErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    load();
  }, []);
  if (loading) return <LoadingState label="Loading auctions…" />;
  if (error) return <ErrorState message={error} onRetry={load} />;
  return (
    <>
      <div className="mb-7 flex flex-wrap items-end justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-blue-700">
            RFQ auction management
          </p>
          <h1 className="mt-1 text-3xl font-semibold text-slate-900">
            Auctions
          </h1>
          <p className="mt-2 text-slate-600">
            Track timings, lowest bids, and supplier competition.
          </p>
        </div>
        {/* <Link
          to="/auctions/new"
          className="rounded-md bg-blue-700 px-4 py-2.5 text-sm font-semibold text-white"
        >
          Create auction
        </Link> */}
      </div>
      {!auctions.length ? (
        <EmptyState
          title="No auctions yet"
          detail="Create an RFQ auction to start collecting supplier bids."
        />
      ) : (
        <div className="overflow-x-auto rounded-xl border border-slate-200 bg-white">
          <table className="w-full min-w-[800px] text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
              <tr>
                <th className="px-4 py-3">RFQ / auction</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Start time</th>
                <th className="px-4 py-3">Current close</th>
                <th className="px-4 py-3">Forced close</th>
                <th className="px-4 py-3">Lowest bid</th>
                <th className="px-4 py-3" />
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {auctions.map((auction) => {
                const summary = summaries[auction.id];
                return (
                  <tr key={auction.id}>
                    <td className="px-4 py-4">
                      <p className="font-semibold text-slate-900">
                        {auction.name}
                      </p>
                      <p className="text-xs text-slate-500">
                        {auction.referenceId}
                      </p>
                    </td>
                    <td className="px-4 py-4">
                      <StatusBadge status={auction.state} />
                    </td>
                    <td className="px-4 py-4 text-slate-600">
                      {formatDateTime(auction.bidStartTime)}
                    </td>
                    <td className="px-4 py-4 text-slate-600">
                      {formatDateTime(auction.currentCloseTime)}
                    </td>
                    <td className="px-4 py-4 text-slate-600">
                      {formatDateTime(auction.forcedCloseTime)}
                    </td>
                    <td className="px-4 py-4 font-semibold text-slate-900">
                      {summary?.error ? (
                        <span title={summary.error} className="text-amber-700">
                          Unavailable
                        </span>
                      ) : (
                        formatCurrency(summary?.data?.currentLowestBid)
                      )}
                    </td>
                    <td className="px-4 py-4">
                      <Link
                        to={`/auctions/${auction.id}`}
                        className="font-semibold text-blue-700 hover:text-blue-900"
                      >
                        View details →
                      </Link>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
