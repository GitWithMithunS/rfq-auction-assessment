import { EmptyState } from "../common/PageState";
import { formatDateTime } from "../../utils/formatters";

export default function ActivityLog({ activity }) {
  if (!activity?.length)
    return (
      <EmptyState
        title="No activity recorded"
        detail="Auction events and extensions will appear here."
      />
    );
  return (
    <ol className="rounded-xl border border-slate-200 bg-white divide-y">
      {activity.map((item, index) => {
        const type = item?.type ? String(item.type) : "ACTIVITY";
        const isTimeExtended = type === "TIME_EXTENDED";
        return (
          <li
            key={`${item?.occurredAt || "activity"}-${index}`}
            className="p-4"
          >
            <p className="font-medium">{type.replaceAll("_", " ")}</p>
            <p className="text-sm text-slate-500">{item?.reason || "—"}</p>
            <p className="mt-1 text-xs text-slate-400">
              Event time: {formatDateTime(item?.occurredAt)}
            </p>
            {isTimeExtended && (
              <p className="mt-1 text-xs font-medium text-blue-700">
                Resulting close time: {formatDateTime(item?.resultingCloseTime)}
              </p>
            )}
          </li>
        );
      })}
    </ol>
  );
}
