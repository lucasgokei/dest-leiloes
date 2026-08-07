import Link from "next/link";
import { requireAdmin } from "@/lib/dal";
import { apiFetchServer } from "@/lib/api-client";
import { formatCurrency, formatDateTime } from "@/lib/format";
import AdminAuctionActions from "@/components/admin-auction-actions";
import { CategoryBadge, StatusBadge } from "@/components/ui/badge";
import type { AuctionCategory } from "@/lib/category";

export const dynamic = "force-dynamic";

type AdminAuction = {
  id: string;
  title: string;
  sellerName: string;
  status: "PENDING" | "ACTIVE" | "CLOSED" | "CANCELLED";
  category: AuctionCategory;
  currentPrice: number;
  bidCount: number;
  endsAt: string;
};

export default async function AdminPage() {
  await requireAdmin();

  const result = await apiFetchServer<AdminAuction[]>("/api/admin/auctions");
  const auctions = result.ok ? result.data : [];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold tracking-tight text-ink">
          Painel de administração
        </h1>
        <Link href="/admin/usuarios" className="text-sm text-brand-600 hover:underline">
          Gerenciar usuários →
        </Link>
      </div>

      <div className="overflow-x-auto rounded-card border border-border bg-surface">
        <table className="w-full text-left text-sm">
          <thead className="bg-surface-muted text-ink-muted">
            <tr>
              <th className="px-4 py-2 font-medium">Título</th>
              <th className="px-4 py-2 font-medium">Categoria</th>
              <th className="px-4 py-2 font-medium">Vendedor</th>
              <th className="px-4 py-2 font-medium">Status</th>
              <th className="px-4 py-2 font-medium">Preço atual</th>
              <th className="px-4 py-2 font-medium">Lances</th>
              <th className="px-4 py-2 font-medium">Encerra</th>
              <th className="px-4 py-2 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody>
            {auctions.map((auction) => (
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
                <td className="px-4 py-2 text-ink">{auction.sellerName}</td>
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
                <td className="px-4 py-2">
                  <AdminAuctionActions auctionId={auction.id} status={auction.status} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
