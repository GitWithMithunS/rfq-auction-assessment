import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AppShell from './components/common/AppShell'
import AuctionDetailsPage from './pages/AuctionDetailsPage'
import AuctionListPage from './pages/AuctionListPage'
import CreateAuctionPage from './pages/CreateAuctionPage'
import NotFoundPage from './pages/NotFoundPage'

export default function App() {
  return <BrowserRouter><AppShell><Routes>
    <Route path="/" element={<AuctionListPage />} />
    <Route path="/auctions/new" element={<CreateAuctionPage />} />
    <Route path="/auctions/:id" element={<AuctionDetailsPage />} />
    <Route path="*" element={<NotFoundPage />} />
  </Routes></AppShell></BrowserRouter>
}
