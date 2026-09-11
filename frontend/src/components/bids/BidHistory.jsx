import { EmptyState } from "../common/PageState";
import { formatCurrency, formatDateTime } from "../../utils/formatters";

export default function BidHistory({ bids, supplierName }) {
  if (!bids.length)
    return (
      <EmptyState
        title="No bid history"
        detail="Submitted bids will be shown here."
      />
    );
  return (
    <div className="overflow-x-auto rounded-xl border border-slate-200 bg-white">
      <table className="w-full min-w-[950px] text-sm">
        <thead className="bg-slate-50 text-left text-xs uppercase text-slate-500">
          <tr>
            <th className="p-3">Supplier</th>
            <th className="p-3">Freight</th>
            <th className="p-3">Origin</th>
            <th className="p-3">Destination</th>
            <th className="p-3">Total</th>
            <th className="p-3">Transit</th>
            <th className="p-3">Quote valid until</th>
            <th className="p-3">Submitted</th>
          </tr>
        </thead>
        <tbody>
          {bids.map((bid) => (
            <tr key={bid.id} className="border-t">
              <td className="p-3">{supplierName(bid.supplierId)}</td>
              <td className="p-3">{formatCurrency(bid.freightCharges)}</td>
              <td className="p-3">{formatCurrency(bid.originCharges)}</td>
              <td className="p-3">{formatCurrency(bid.destinationCharges)}</td>
              <td className="p-3 font-semibold">
                {formatCurrency(bid.totalAmount)}
              </td>
              <td className="p-3">{bid.transitTimeDays} days</td>
              <td className="p-3 text-slate-500">
                {formatDateTime(bid.quoteValidUntil)}
              </td>
              <td className="p-3 text-slate-500">
                {formatDateTime(bid.submittedAt)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
