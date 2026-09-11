import { EmptyState } from "../common/PageState";
import { formatCurrency } from "../../utils/formatters";

export default function RankingTable({ rankings, supplierName }) {
  if (!rankings.length)
    return (
      <EmptyState
        title="No ranked bids yet"
        detail="Rankings appear after suppliers submit bids."
      />
    );
  return (
    <div className="overflow-x-auto rounded-xl border border-slate-200 bg-white">
      <table className="w-full text-sm">
        <thead className="bg-slate-50 text-left text-xs uppercase text-slate-500">
          <tr>
            <th className="p-3">Rank</th>
            <th className="p-3">Supplier</th>
            <th className="p-3">Total</th>
            <th className="p-3">Transit</th>
          </tr>
        </thead>
        <tbody>
          {rankings.map((bid) => (
            <tr key={bid.id} className="border-t">
              <td className="p-3 font-bold">L{bid.rank}</td>
              <td className="p-3">{supplierName(bid.supplierId)}</td>
              <td className="p-3 font-semibold">
                {formatCurrency(bid.totalAmount)}
              </td>
              <td className="p-3">{bid.transitTimeDays} days</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
