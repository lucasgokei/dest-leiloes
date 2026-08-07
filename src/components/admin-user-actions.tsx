"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api-client";

export default function AdminUserActions({
  userId,
  role,
  isSelf,
}: {
  userId: string;
  role: "USER" | "ADMIN";
  isSelf: boolean;
}) {
  const router = useRouter();
  const [pending, setPending] = useState(false);
  const nextRole = role === "ADMIN" ? "USER" : "ADMIN";

  async function handleToggleRole() {
    setPending(true);
    await apiFetch(`/api/admin/users/${userId}/role`, {
      method: "PATCH",
      body: JSON.stringify({ role: nextRole }),
    });
    setPending(false);
    router.refresh();
  }

  async function handleDelete() {
    setPending(true);
    await apiFetch(`/api/admin/users/${userId}`, { method: "DELETE" });
    setPending(false);
    router.refresh();
  }

  return (
    <div className="flex gap-2">
      <button
        type="button"
        onClick={handleToggleRole}
        disabled={pending || (isSelf && nextRole !== "ADMIN")}
        className="rounded-full border border-border px-2.5 py-1 text-xs text-ink transition hover:bg-surface-muted disabled:opacity-40"
      >
        {role === "ADMIN" ? "Rebaixar" : "Promover"}
      </button>
      <button
        type="button"
        onClick={handleDelete}
        disabled={pending || isSelf}
        className="rounded-full border border-red-600/30 px-2.5 py-1 text-xs text-red-600 transition hover:bg-red-600/10 disabled:opacity-40 dark:text-red-400"
      >
        Excluir
      </button>
    </div>
  );
}
