import { requireSession } from "@/lib/dal";
import AuctionForm from "@/components/auction-form";

export default async function NewAuctionPage() {
  await requireSession();

  return (
    <div className="mx-auto max-w-2xl">
      <h1 className="text-2xl font-bold tracking-tight text-ink">Criar leilão</h1>
      <p className="mt-1 text-sm text-ink-muted">
        Preencha os dados do item que você quer leiloar.
      </p>
      <div className="mt-6 rounded-card border border-border bg-surface p-6 shadow-sm">
        <AuctionForm />
      </div>
    </div>
  );
}
