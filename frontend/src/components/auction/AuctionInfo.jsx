import StatusBadge from "../common/StatusBadge";
import { formatDateTime } from "../../utils/formatters";

export default function AuctionInfo({ auction }) {
  const timings = [
    ["Start", auction.bidStartTime],
    ["Current close", auction.currentCloseTime],
    ["Forced close", auction.forcedCloseTime],
    ["Service date", auction.serviceDate],
  ];
  return (
    <section className="rounded-xl border border-slate-200 bg-white p-5">
      <div className="flex flex-wrap justify-between gap-3">
        <div>
          <p className="text-sm font-medium text-blue-700">
            {auction.referenceId}
          </p>
          <h1 className="mt-1 text-2xl font-semibold">{auction.name}</h1>
        </div>
        <StatusBadge status={auction.state} />
      </div>
      <dl className="mt-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {timings.map(([label, value]) => (
          <div key={label}>
            <dt className="label">{label}</dt>
            <dd className="mt-1 text-sm font-medium">
              {formatDateTime(value)}
            </dd>
          </div>
        ))}
      </dl>
    </section>
  );
}
