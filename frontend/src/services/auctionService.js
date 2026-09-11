import axios from 'axios'
import { AUCTION_API } from '../constants/endpoints'

export const getAuctions = async () => (await axios.get(`${AUCTION_API}/api/auctions`)).data
export const getAuctionById = async (auctionId) => (await axios.get(`${AUCTION_API}/api/auctions/${auctionId}`)).data
export const getAuctionActivity = async (auctionId) => (await axios.get(`${AUCTION_API}/api/auctions/${auctionId}/activity`)).data
export const createAuction = async (auctionData) => (await axios.post(`${AUCTION_API}/api/auctions`, auctionData)).data
