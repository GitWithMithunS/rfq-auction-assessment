export function LoadingState({ label = "Loading data…" }) {
  return (
    <div className="rounded-xl border border-slate-200 bg-white p-10 text-center text-sm text-slate-500">
      {label}
    </div>
  );
}
export function ErrorState({ message, onRetry }) {
  return (
    <div className="rounded-xl border border-rose-200 bg-rose-50 p-6 text-center">
      <p className="font-medium text-rose-800">
        Could not load this information
      </p>
      <p className="mt-1 text-sm text-rose-700">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="mt-4 rounded-md border border-rose-300 bg-white px-3 py-2 text-sm font-medium text-rose-800"
        >
          Try again
        </button>
      )}
    </div>
  );
}
export function EmptyState({ title, detail }) {
  return (
    <div className="rounded-xl border border-dashed border-slate-300 bg-white p-10 text-center">
      <p className="font-semibold text-slate-800">{title}</p>
      <p className="mt-1 text-sm text-slate-500">{detail}</p>
    </div>
  );
}
