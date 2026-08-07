import Link from "next/link";
import { requireAdmin } from "@/lib/dal";
import { apiFetchServer } from "@/lib/api-client";
import { formatDateTime } from "@/lib/format";
import AdminUserActions from "@/components/admin-user-actions";

export const dynamic = "force-dynamic";

type AdminUser = {
  id: string;
  name: string;
  email: string;
  role: "USER" | "ADMIN";
  createdAt: string;
  auctionCount: number;
  bidCount: number;
};

export default async function AdminUsersPage() {
  const session = await requireAdmin();

  const result = await apiFetchServer<AdminUser[]>("/api/admin/users");
  const users = result.ok ? result.data : [];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold tracking-tight text-ink">Usuários</h1>
        <Link href="/admin" className="text-sm text-brand-600 hover:underline">
          ← Leilões
        </Link>
      </div>

      <div className="overflow-x-auto rounded-card border border-border bg-surface">
        <table className="w-full text-left text-sm">
          <thead className="bg-surface-muted text-ink-muted">
            <tr>
              <th className="px-4 py-2 font-medium">Nome</th>
              <th className="px-4 py-2 font-medium">E-mail</th>
              <th className="px-4 py-2 font-medium">Papel</th>
              <th className="px-4 py-2 font-medium">Leilões</th>
              <th className="px-4 py-2 font-medium">Lances</th>
              <th className="px-4 py-2 font-medium">Desde</th>
              <th className="px-4 py-2 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="border-t border-border">
                <td className="px-4 py-2 text-ink">{user.name}</td>
                <td className="px-4 py-2 text-ink">{user.email}</td>
                <td className="px-4 py-2">
                  <span
                    className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
                      user.role === "ADMIN"
                        ? "bg-brand-500/10 text-brand-600 dark:text-brand-400"
                        : "bg-ink/8 text-ink-muted"
                    }`}
                  >
                    {user.role}
                  </span>
                </td>
                <td className="px-4 py-2 text-ink">{user.auctionCount}</td>
                <td className="px-4 py-2 text-ink">{user.bidCount}</td>
                <td className="px-4 py-2 text-ink">{formatDateTime(user.createdAt)}</td>
                <td className="px-4 py-2">
                  <AdminUserActions
                    userId={user.id}
                    role={user.role}
                    isSelf={user.id === session.userId}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
