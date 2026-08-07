"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api-client";

export default function AdminAuctionActions({
  auctionId,
  status,
}: {
  auctionId: string;
  status: string;
}) {
  const router = useRouter();
  const [pending, setPending] = useState(false);

  async function handleCancel() {
    setPending(true);
    await apiFetch(`/api/admin/auctions/${auctionId}/cancel`, { method: "POST" });
    setPending(false);
    router.refresh();
  }

  async function handleDelete() {
    setPending(true);
    await apiFetch(`/api/admin/auctions/${auctionId}`, { method: "DELETE" });
    setPending(false);
    router.refresh();
  }

  return (
    <div className="flex gap-2">
      {status === "ACTIVE" && (
        <button
          type="button"
          onClick={handleCancel}
          disabled={pending}
          className="rounded-full border border-border px-2.5 py-1 text-xs text-ink transition hover:bg-surface-muted disabled:opacity-50"
        >
          Cancelar
        </button>
      )}
      <button
        type="button"
        onClick={handleDelete}
        disabled={pending}
        className="rounded-full border border-red-600/30 px-2.5 py-1 text-xs text-red-600 transition hover:bg-red-600/10 disabled:opacity-50 dark:text-red-400"
      >
        Excluir
      </button>
    </div>
  );
}
