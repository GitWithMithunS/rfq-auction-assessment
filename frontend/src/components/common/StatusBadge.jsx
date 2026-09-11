const styles = {
  ACTIVE: "bg-emerald-100 text-emerald-800",
  SCHEDULED: "bg-blue-100 text-blue-800",
  CLOSED: "bg-slate-200 text-slate-700",
  FORCE_CLOSED: "bg-rose-100 text-rose-800",
};
export default function StatusBadge({ status }) {
  return (
    <span
      className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${styles[status] || "bg-slate-100 text-slate-700"}`}
    >
      {(status || "Unknown").replace("_", " ")}
    </span>
  );
}
