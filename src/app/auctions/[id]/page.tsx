import { notFound } from "next/navigation";
import { apiFetchServer } from "@/lib/api-client";
import { getUser } from "@/lib/dal";
import AuctionRoom from "@/components/auction-room";
import type { AuctionCategory } from "@/lib/category";

export const dynamic = "force-dynamic";

type AuctionDetail = {
  id: string;
  title: string;
  description: string;
  imageUrl: string | null;
  startingPrice: number;
  currentPrice: number;
  status: "PENDING" | "ACTIVE" | "CLOSED" | "CANCELLED";
  category: AuctionCategory;
  endsAt: string;
  seller: { id: string; name: string };
  winner: { id: string; name: string } | null;
  bids: { id: string; amount: number; createdAt: string; bidderName: string }[];
};

export default async function AuctionPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;

  const [auctionResult, user] = await Promise.all([
    apiFetchServer<AuctionDetail>(`/api/auctions/${id}`),
    getUser(),
  ]);

  if (!auctionResult.ok) {
    notFound();
  }

  const auction = auctionResult.data;

  return (
    <AuctionRoom
      auction={auction}
      initialBids={auction.bids}
      currentUser={user ? { id: user.id, name: user.name } : null}
    />
  );
}
