"use client";

import { useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api-client";

export default function LogoutButton() {
  const router = useRouter();

  async function handleLogout() {
    await apiFetch("/api/auth/logout", { method: "POST" });
    router.push("/login");
    router.refresh();
  }

  return (
    <button
      type="button"
      onClick={handleLogout}
      className="rounded-full border border-border px-3 py-1.5 transition hover:bg-surface-muted"
    >
      Sair
    </button>
  );
}
