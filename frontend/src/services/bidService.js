import axios from 'axios'
import { BID_API } from '../constants/endpoints'

export const getBids = async (auctionId) => (await axios.get(`${BID_API}/api/auctions/${auctionId}/bids`)).data
export const getRankings = async (auctionId) => (await axios.get(`${BID_API}/api/auctions/${auctionId}/rankings`)).data
export const getAuctionSummary = async (auctionId) => (await axios.get(`${BID_API}/api/auctions/${auctionId}/summary`)).data
export const getSuppliers = async () => (await axios.get(`${BID_API}/api/suppliers`)).data
export const createSupplier = async (supplierData) => (await axios.post(`${BID_API}/api/suppliers`, supplierData)).data
export const submitBid = async (bidData) => (await axios.post(`${BID_API}/api/bids`, bidData)).data
