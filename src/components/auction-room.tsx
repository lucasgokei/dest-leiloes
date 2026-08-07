"use client";

import { useEffect, useMemo, useState } from "react";
import { subscribeToAuction } from "@/lib/stomp-client";
import { apiFetch } from "@/lib/api-client";
import { formatCurrency, formatDateTime } from "@/lib/format";
import CountdownTimer from "@/components/countdown-timer";
import { CategoryBadge, StatusBadge } from "@/components/ui/badge";
import type { AuctionCategory } from "@/lib/category";

type AuctionStatus = "PENDING" | "ACTIVE" | "CLOSED" | "CANCELLED";

type Auction = {
  id: string;
  title: string;
  description: string;
  imageUrl: string | null;
  startingPrice: number;
  currentPrice: number;
  status: AuctionStatus;
  category: AuctionCategory;
  endsAt: string;
  seller: { id: string; name: string };
  winner: { id: string; name: string } | null;
};

type Bid = {
  id: string;
  amount: number;
  createdAt: string;
  bidderName: string;
};

export default function AuctionRoom({
  auction,
  initialBids,
  currentUser,
}: {
  auction: Auction;
  initialBids: Bid[];
  currentUser: { id: string; name: string } | null;
}) {
  const [currentPrice, setCurrentPrice] = useState(auction.currentPrice);
  const [status, setStatus] = useState<AuctionStatus>(auction.status);
  const [bids, setBids] = useState<Bid[]>(initialBids);
  const [winnerName, setWinnerName] = useState<string | null>(
    auction.winner?.name ?? null
  );
  const [amount, setAmount] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [pending, setPending] = useState(false);

  useEffect(() => {
    const unsubscribe = subscribeToAuction(auction.id, (event) => {
      if (event.auctionId !== auction.id) return;

      switch (event.type) {
        case "BID_NEW":
          setCurrentPrice(Number(event.currentPrice));
          setBids((prev) => [
            {
              id: `${event.createdAt}-${event.bidderName}`,
              amount: Number(event.amount),
              createdAt: event.createdAt as string,
              bidderName: event.bidderName as string,
            },
            ...prev,
          ]);
          break;
        case "AUCTION_CLOSED":
          setStatus("CLOSED");
          setWinnerName((event.winnerName as string | null) ?? null);
          break;
        case "AUCTION_CANCELLED":
          setStatus("CANCELLED");
          break;
      }
    });

    return unsubscribe;
  }, [auction.id]);

  const isOwner = currentUser?.id === auction.seller.id;
  const isActive = status === "ACTIVE";
  const minNextBid = useMemo(
    () => (currentPrice + 0.01).toFixed(2),
    [currentPrice]
  );

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const parsedAmount = Number(amount);
    if (!Number.isFinite(parsedAmount) || parsedAmount <= 0) {
      setError("Informe um valor válido.");
      return;
    }

    setPending(true);
    const result = await apiFetch<{ error?: string }>(
      `/api/auctions/${auction.id}/bids`,
      {
        method: "POST",
        body: JSON.stringify({ amount: parsedAmount }),
      }
    );
    setPending(false);

    if (!result.ok) {
      setError(result.data.error ?? "Não foi possível registrar o lance.");
      return;
    }

    setAmount("");
  }

  return (
    <div className="grid grid-cols-1 gap-8 lg:grid-cols-5">
      <div className="lg:col-span-3">
        <div className="flex h-72 items-center justify-center overflow-hidden rounded-card bg-surface-muted">
          {auction.imageUrl ? (
            // eslint-disable-next-line @next/next/no-img-element
            <img
              src={auction.imageUrl}
              alt={auction.title}
              className="h-full w-full object-cover"
            />
          ) : (
            <span className="text-6xl">🔨</span>
          )}
        </div>

        <div className="mt-5 flex items-center gap-2">
          <CategoryBadge category={auction.category} />
        </div>
        <h1 className="mt-2 text-2xl font-bold tracking-tight text-ink">
          {auction.title}
        </h1>
        <p className="mt-1 text-sm text-ink-muted">
          Vendido por {auction.seller.name}
        </p>
        <p className="mt-4 whitespace-pre-wrap text-sm text-ink-muted">
          {auction.description}
        </p>
      </div>

      <div className="lg:col-span-2">
        <div className="sticky top-20 rounded-card border border-border bg-surface p-5 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-sm text-ink-muted">Status</span>
            <StatusBadge status={status} />
          </div>

          <div className="mt-3">
            <span className="text-sm text-ink-muted">Lance atual</span>
            <p className="text-3xl font-bold text-brand-600">
              {formatCurrency(currentPrice)}
            </p>
          </div>

          <div className="mt-3 flex items-center justify-between text-sm">
            <span className="text-ink-muted">
              {isActive ? "Encerra em" : "Encerrado em"}
            </span>
            {isActive ? (
              <CountdownTimer
                endsAt={auction.endsAt}
                onExpire={() => setStatus("CLOSED")}
              />
            ) : (
              <span className="text-ink">{formatDateTime(auction.endsAt)}</span>
            )}
          </div>

          {status === "CLOSED" && (
            <p className="mt-3 rounded-lg bg-emerald-600/10 p-3 text-sm text-emerald-700 dark:text-emerald-400">
              {winnerName
                ? `Arrematado por ${winnerName}`
                : "Leilão encerrado sem lances."}
            </p>
          )}

          {status === "CANCELLED" && (
            <p className="mt-3 rounded-lg bg-red-600/10 p-3 text-sm text-red-700 dark:text-red-400">
              Este leilão foi cancelado por um administrador.
            </p>
          )}

          {isActive && !isOwner && currentUser && (
            <form onSubmit={handleSubmit} className="mt-5 space-y-2">
              <label htmlFor="amount" className="block text-sm font-medium text-ink">
                Seu lance (mínimo {formatCurrency(minNextBid)})
              </label>
              <div className="flex gap-2">
                <input
                  id="amount"
                  type="number"
                  step="0.01"
                  min={minNextBid}
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                  className="w-full rounded-lg border border-border bg-surface px-3 py-2 text-sm text-ink outline-none focus:border-brand-500"
                />
                <button
                  type="submit"
                  disabled={pending}
                  className="shrink-0 rounded-lg bg-brand-500 px-4 py-2 text-sm font-semibold text-white transition hover:bg-brand-600 disabled:opacity-60"
                >
                  {pending ? "Enviando..." : "Dar lance"}
                </button>
              </div>
              {error && <p className="text-xs text-red-600 dark:text-red-400">{error}</p>}
            </form>
          )}

          {isActive && isOwner && (
            <p className="mt-5 text-sm text-ink-muted">
              Você é o vendedor deste leilão e não pode dar lances nele.
            </p>
          )}

          {isActive && !currentUser && (
            <p className="mt-5 text-sm text-ink-muted">
              <a href="/login" className="text-brand-600 hover:underline">
                Entre na sua conta
              </a>{" "}
              para dar um lance.
            </p>
          )}
        </div>

        <div className="mt-5">
          <h2 className="text-sm font-semibold text-ink">Histórico de lances</h2>
          {bids.length === 0 ? (
            <p className="mt-2 text-sm text-ink-muted">Nenhum lance ainda.</p>
          ) : (
            <ul className="mt-2 max-h-72 space-y-1 overflow-y-auto text-sm">
              {bids.map((bid) => (
                <li
                  key={bid.id}
                  className="flex items-center justify-between rounded-lg px-2 py-1.5 odd:bg-surface-muted"
                >
                  <span className="text-ink">{bid.bidderName}</span>
                  <span className="font-medium text-ink">
                    {formatCurrency(bid.amount)}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
