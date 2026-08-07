import Link from "next/link";
import { requireSession } from "@/lib/dal";
import { apiFetchServer } from "@/lib/api-client";
import { formatCurrency, formatDateTime } from "@/lib/format";
import { CategoryBadge, StatusBadge } from "@/components/ui/badge";
import type { AuctionCategory } from "@/lib/category";

export const dynamic = "force-dynamic";

type AuctionStatus = "PENDING" | "ACTIVE" | "CLOSED" | "CANCELLED";

type MyAuction = {
  id: string;
  title: string;
  status: AuctionStatus;
  category: AuctionCategory;
  currentPrice: number;
  bidCount: number;
  endsAt: string;
};

type MyBidAuction = {
  id: string;
  title: string;
  status: AuctionStatus;
  currentPrice: number;
  winnerId: string | null;
};

export default async function DashboardPage() {
  const session = await requireSession();

  const [myAuctionsResult, myBidsResult] = await Promise.all([
    apiFetchServer<MyAuction[]>("/api/users/me/auctions"),
    apiFetchServer<MyBidAuction[]>("/api/users/me/bids"),
  ]);

  const myAuctions = myAuctionsResult.ok ? myAuctionsResult.data : [];
  const myBids = myBidsResult.ok ? myBidsResult.data : [];

  return (
    <div className="space-y-10">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold tracking-tight text-ink">Minha área</h1>
        <Link
          href="/auctions/novo"
          className="rounded-full bg-brand-500 px-4 py-2 text-sm font-semibold text-white transition hover:bg-brand-600"
        >
          Criar leilão
        </Link>
      </div>

      <section>
        <h2 className="text-lg font-semibold text-ink">Meus leilões</h2>
        {myAuctions.length === 0 ? (
          <p className="mt-2 text-sm text-ink-muted">
            Você ainda não criou nenhum leilão.
          </p>
        ) : (
          <div className="mt-3 overflow-x-auto rounded-card border border-border bg-surface">
            <table className="w-full text-left text-sm">
              <thead className="bg-surface-muted text-ink-muted">
                <tr>
                  <th className="px-4 py-2 font-medium">Título</th>
                  <th className="px-4 py-2 font-medium">Categoria</th>
                  <th className="px-4 py-2 font-medium">Status</th>
                  <th className="px-4 py-2 font-medium">Preço atual</th>
                  <th className="px-4 py-2 font-medium">Lances</th>
                  <th className="px-4 py-2 font-medium">Encerra</th>
                </tr>
              </thead>
              <tbody>
                {myAuctions.map((auction) => (
                  <tr key={auction.id} className="border-t border-border">
                    <td className="px-4 py-2">
                      <Link
                        href={`/auctions/${auction.id}`}
                        className="text-brand-600 hover:underline"
                      >
                        {auction.title}
                      </Link>
                    </td>
                    <td className="px-4 py-2">
                      <CategoryBadge category={auction.category} />
                    </td>
                    <td className="px-4 py-2">
                      <StatusBadge status={auction.status} />
                    </td>
                    <td className="px-4 py-2 text-ink">
                      {formatCurrency(auction.currentPrice)}
                    </td>
                    <td className="px-4 py-2 text-ink">{auction.bidCount}</td>
                    <td className="px-4 py-2 text-ink">
                      {formatDateTime(auction.endsAt)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      <section>
        <h2 className="text-lg font-semibold text-ink">
          Leilões em que dei lance
        </h2>
        {myBids.length === 0 ? (
          <p className="mt-2 text-sm text-ink-muted">
            Você ainda não deu nenhum lance.
          </p>
        ) : (
          <div className="mt-3 overflow-x-auto rounded-card border border-border bg-surface">
            <table className="w-full text-left text-sm">
              <thead className="bg-surface-muted text-ink-muted">
                <tr>
                  <th className="px-4 py-2 font-medium">Título</th>
                  <th className="px-4 py-2 font-medium">Status</th>
                  <th className="px-4 py-2 font-medium">Preço atual</th>
                  <th className="px-4 py-2 font-medium">Vencendo?</th>
                </tr>
              </thead>
              <tbody>
                {myBids.map((auction) => (
                  <tr key={auction.id} className="border-t border-border">
                    <td className="px-4 py-2">
                      <Link
                        href={`/auctions/${auction.id}`}
                        className="text-brand-600 hover:underline"
                      >
                        {auction.title}
                      </Link>
                    </td>
                    <td className="px-4 py-2">
                      <StatusBadge status={auction.status} />
                    </td>
                    <td className="px-4 py-2 text-ink">
                      {formatCurrency(auction.currentPrice)}
                    </td>
                    <td className="px-4 py-2 text-ink">
                      {auction.winnerId === session.userId ? "Sim 🏆" : "—"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}
