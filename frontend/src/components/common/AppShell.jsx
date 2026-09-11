import { Link, NavLink } from "react-router-dom";

export default function AppShell({ children }) {
  return (
    <div className="min-h-screen bg-slate-50">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-4 md:px-8">
          <Link to="/" className="flex items-center gap-3">
            <span className="grid h-9 w-9 place-items-center rounded-lg bg-blue-700 font-bold text-white">
              RFQ
            </span>
            <span>
              <span className="block font-semibold text-slate-900">
                British Auction in RFQ System{" "}
              </span>
              <span className="block text-xs text-slate-500">
                Reverse Auction Desk
              </span>
            </span>
          </Link>
          <nav className="flex items-center gap-2 text-sm font-medium">
            <NavLink
              to="/"
              end
              className={({ isActive }) =>
                `rounded-md px-3 py-2 ${isActive ? "bg-blue-50 text-blue-700" : "text-slate-600 hover:text-slate-900"}`
              }
            >
              Auctions
            </NavLink>
            <NavLink
              to="/auctions/new"
              className="rounded-md bg-blue-700 px-3 py-2 text-white hover:bg-blue-800"
            >
              Create auction
            </NavLink>
          </nav>
        </div>
      </header>
      <main className="mx-auto max-w-7xl px-5 py-7 md:px-8 md:py-9">
        {children}
      </main>
    </div>
  );
}
