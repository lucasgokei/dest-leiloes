import Link from "next/link";
import { apiFetchServer } from "@/lib/api-client";
import { formatCurrency } from "@/lib/format";
import CountdownTimer from "@/components/countdown-timer";
import { CategoryBadge } from "@/components/ui/badge";
import {
  AUCTION_CATEGORIES,
  CATEGORY_LABELS,
  isAuctionCategory,
  type AuctionCategory,
} from "@/lib/category";

export const dynamic = "force-dynamic";

type AuctionSummary = {
  id: string;
  title: string;
  description: string;
  imageUrl: string | null;
  startingPrice: number;
  currentPrice: number;
  status: string;
  category: AuctionCategory;
  endsAt: string;
  bidCount: number;
};

export default async function HomePage({
  searchParams,
}: {
  searchParams: Promise<{ category?: string }>;
}) {
  const { category: categoryParam } = await searchParams;
  const activeCategory =
    categoryParam && isAuctionCategory(categoryParam) ? categoryParam : null;

  const result = await apiFetchServer<AuctionSummary[]>("/api/auctions");
  const allAuctions = result.ok ? result.data : [];
  const auctions = activeCategory
    ? allAuctions.filter((a) => a.category === activeCategory)
    : allAuctions;
  const totalBids = allAuctions.reduce((sum, a) => sum + a.bidCount, 0);

  return (
    <div className="space-y-12">
      <section className="overflow-hidden rounded-card bg-gradient-to-br from-brand-500 to-brand-700 px-6 py-12 text-white sm:px-10 sm:py-16">
        <p className="text-sm font-semibold uppercase tracking-wide text-brand-100">
          Leilões ao vivo
        </p>
        <h1 className="mt-2 max-w-xl text-3xl font-bold tracking-tight sm:text-4xl">
          Dê seu lance. Acompanhe em tempo real. Arremate.
        </h1>
        <p className="mt-3 max-w-lg text-sm text-white/90 sm:text-base">
          Compre e venda com segurança: lances atualizados na hora e
          encerramento automático quando o prazo termina.
        </p>
        <div className="mt-6 flex flex-wrap gap-3">
          <a
            href="#leiloes"
            className="rounded-full bg-white px-5 py-2.5 text-sm font-semibold text-brand-700 shadow-sm transition hover:bg-brand-50"
          >
            Ver leilões ativos
          </a>
          <Link
            href="/auctions/novo"
            className="rounded-full border border-white/40 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-white/10"
          >
            Criar leilão
          </Link>
        </div>
        <p className="mt-6 text-xs text-white/80">
          {allAuctions.length} leilões ativos agora · {totalBids} lances
          registrados
        </p>
      </section>

      <section id="leiloes" className="scroll-mt-24 space-y-6">
        <div>
          <h2 className="text-2xl font-bold tracking-tight text-ink">
            Leilões em andamento
          </h2>
          <p className="mt-1 text-sm text-ink-muted">
            Dê seu lance em tempo real antes que o tempo acabe.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          <CategoryTab
            href="/"
            label="Todos"
            count={allAuctions.length}
            active={!activeCategory}
          />
          {AUCTION_CATEGORIES.map((category) => (
            <CategoryTab
              key={category}
              href={`/?category=${category}`}
              label={CATEGORY_LABELS[category]}
              count={allAuctions.filter((a) => a.category === category).length}
              active={activeCategory === category}
            />
          ))}
        </div>

        {auctions.length === 0 ? (
          <p className="rounded-card border border-dashed border-border p-10 text-center text-sm text-ink-muted">
            Nenhum leilão ativo {activeCategory ? "nesta categoria" : "no momento"}.
          </p>
        ) : (
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {auctions.map((auction) => (
              <Link
                key={auction.id}
                href={`/auctions/${auction.id}`}
                className="group flex flex-col overflow-hidden rounded-card border border-border bg-surface transition hover:-translate-y-0.5 hover:shadow-lg"
              >
                <div className="flex h-40 items-center justify-center overflow-hidden bg-surface-muted">
                  {auction.imageUrl ? (
                    // eslint-disable-next-line @next/next/no-img-element
                    <img
                      src={auction.imageUrl}
                      alt={auction.title}
                      className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
                    />
                  ) : (
                    <span className="text-4xl">🔨</span>
                  )}
                </div>
                <div className="flex flex-1 flex-col gap-2 p-4">
                  <CategoryBadge category={auction.category} className="self-start" />
                  <h3 className="line-clamp-1 font-semibold text-ink group-hover:text-brand-600">
                    {auction.title}
                  </h3>
                  <p className="line-clamp-2 flex-1 text-sm text-ink-muted">
                    {auction.description}
                  </p>
                  <div className="flex items-center justify-between pt-2 text-sm">
                    <span className="font-semibold text-brand-600">
                      {formatCurrency(auction.currentPrice)}
                    </span>
                    <span className="text-ink-muted">
                      {auction.bidCount} lance(s)
                    </span>
                  </div>
                  <CountdownTimer endsAt={auction.endsAt} className="text-xs" />
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

function CategoryTab({
  href,
  label,
  count,
  active,
}: {
  href: string;
  label: string;
  count: number;
  active: boolean;
}) {
  return (
    <Link
      href={href}
      className={`rounded-full border px-4 py-1.5 text-sm font-medium transition ${
        active
          ? "border-brand-500 bg-brand-500 text-white"
          : "border-border bg-surface text-ink-muted hover:border-brand-300 hover:text-brand-600"
      }`}
    >
      {label}{" "}
      <span className={active ? "text-white/80" : "text-ink-muted/70"}>
        ({count})
      </span>
    </Link>
  );
}
