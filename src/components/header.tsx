import Link from "next/link";
import { getUser } from "@/lib/dal";
import LogoutButton from "@/components/logout-button";
import Logo from "@/components/logo";

export default async function Header() {
  const user = await getUser();

  return (
    <header className="sticky top-0 z-20 border-b border-border bg-surface/90 backdrop-blur">
      <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-4 px-4 py-3">
        <Link href="/" className="shrink-0">
          <Logo />
        </Link>

        <nav className="flex flex-wrap items-center gap-x-5 gap-y-2 text-sm font-medium text-ink-muted">
          <Link href="/" className="transition hover:text-brand-600">
            Leilões
          </Link>

          {user ? (
            <>
              <Link href="/auctions/novo" className="transition hover:text-brand-600">
                Criar leilão
              </Link>
              <Link href="/dashboard" className="transition hover:text-brand-600">
                Minha área
              </Link>
              {user.role === "ADMIN" && (
                <Link href="/admin" className="transition hover:text-brand-600">
                  Admin
                </Link>
              )}
              <span className="hidden text-ink-muted sm:inline">{user.name}</span>
              <LogoutButton />
            </>
          ) : (
            <>
              <Link href="/login" className="transition hover:text-brand-600">
                Entrar
              </Link>
              <Link
                href="/register"
                className="rounded-full bg-brand-500 px-4 py-2 font-semibold text-white shadow-sm transition hover:bg-brand-600"
              >
                Criar conta
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
