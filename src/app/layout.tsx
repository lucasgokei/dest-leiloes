import type { Metadata } from "next";
import localFont from "next/font/local";
import Link from "next/link";
import Header from "@/components/header";
import Logo from "@/components/logo";
import { AUCTION_CATEGORIES, CATEGORY_LABELS } from "@/lib/category";
import "./globals.css";

const helveticaRounded = localFont({
  src: "../../public/fonts/helvetica-rounded-bold.otf",
  variable: "--font-helvetica-rounded",
  weight: "700",
  display: "swap",
});

const sfPro = localFont({
  src: "../../public/fonts/SF-Pro.ttf",
  variable: "--font-sf-pro",
  display: "swap",
});

export const metadata: Metadata = {
  title: "Dest Leilões",
  description: "Leilões online em tempo real, com lances seguros e encerramento automático.",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html
      lang="pt-BR"
      className={`${helveticaRounded.variable} ${sfPro.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col bg-background text-ink">
        <Header />
        <main className="mx-auto w-full max-w-6xl flex-1 px-4 py-8">
          {children}
        </main>
        <footer className="border-t border-border bg-surface-muted">
          <div className="mx-auto grid max-w-6xl gap-8 px-4 py-10 sm:grid-cols-2 lg:grid-cols-4">
            <div>
              <Logo />
              <p className="mt-3 max-w-xs text-sm text-ink-muted">
                Leilões online em tempo real, com lances seguros e encerramento
                automático.
              </p>
            </div>

            <div>
              <h3 className="text-sm font-semibold text-ink">Navegação</h3>
              <ul className="mt-3 space-y-2 text-sm text-ink-muted">
                <li>
                  <Link href="/" className="transition hover:text-brand-600">
                    Leilões ativos
                  </Link>
                </li>
                <li>
                  <Link href="/auctions/novo" className="transition hover:text-brand-600">
                    Criar leilão
                  </Link>
                </li>
                <li>
                  <Link href="/dashboard" className="transition hover:text-brand-600">
                    Minha área
                  </Link>
                </li>
              </ul>
            </div>

            <div>
              <h3 className="text-sm font-semibold text-ink">Categorias</h3>
              <ul className="mt-3 space-y-2 text-sm text-ink-muted">
                {AUCTION_CATEGORIES.map((category) => (
                  <li key={category}>
                    <Link
                      href={`/?category=${category}`}
                      className="transition hover:text-brand-600"
                    >
                      {CATEGORY_LABELS[category]}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>

            <div>
              <h3 className="text-sm font-semibold text-ink">Conta</h3>
              <ul className="mt-3 space-y-2 text-sm text-ink-muted">
                <li>
                  <Link href="/login" className="transition hover:text-brand-600">
                    Entrar
                  </Link>
                </li>
                <li>
                  <Link href="/register" className="transition hover:text-brand-600">
                    Criar conta
                  </Link>
                </li>
              </ul>
            </div>
          </div>

          <div className="border-t border-border px-4 py-4 text-center text-xs text-ink-muted">
            Copyright © {new Date().getFullYear()} Dest Leilões — Todos os direitos reservados.
          </div>
        </footer>
      </body>
    </html>
  );
}
