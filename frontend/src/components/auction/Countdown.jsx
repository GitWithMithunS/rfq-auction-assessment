import { useEffect, useRef, useState } from "react";

const getSeconds = (closeTime) =>
  Math.max(0, Math.floor((new Date(closeTime) - Date.now()) / 1000));
const renderTime = (seconds) =>
  `${String(Math.floor(seconds / 3600)).padStart(2, "0")}:${String(Math.floor((seconds % 3600) / 60)).padStart(2, "0")}:${String(seconds % 60).padStart(2, "0")}`;

export default function Countdown({ closeTime, active, onExpired }) {
  const [seconds, setSeconds] = useState(() => getSeconds(closeTime));
  const expired = useRef(false);
  useEffect(() => {
    expired.current = false;
    const tick = () => {
      const next = getSeconds(closeTime);
      setSeconds(next);
      if (next === 0 && !expired.current) {
        expired.current = true;
        onExpired();
      }
    };
    tick();
    const id = setInterval(tick, 1000);
    return () => clearInterval(id);
  }, [closeTime, onExpired]);
  return (
    <div className="rounded-xl bg-slate-900 p-5 text-white">
      <p className="text-xs font-semibold uppercase tracking-widest text-slate-400">
        {active ? "Time remaining" : "Auction status"}
      </p>
      <p className="mt-1 font-mono text-3xl font-semibold">
        {active ? renderTime(seconds) : "—"}
      </p>
      <p className="mt-1 text-xs text-slate-400">Current close time</p>
    </div>
  );
}
