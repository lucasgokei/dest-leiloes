"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { apiFetch } from "@/lib/api-client";
import type { FormState } from "@/lib/definitions";

const inputClass =
  "mt-1 w-full rounded-lg border border-border bg-surface px-3 py-2 text-sm text-ink outline-none focus:border-brand-500";

export default function LoginPage() {
  const router = useRouter();
  const [state, setState] = useState<FormState>(undefined);
  const [pending, setPending] = useState(false);

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setPending(true);
    setState(undefined);

    const formData = new FormData(e.currentTarget);
    const result = await apiFetch<NonNullable<FormState>>("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({
        email: formData.get("email"),
        password: formData.get("password"),
      }),
    });

    setPending(false);

    if (!result.ok) {
      setState(result.data);
      return;
    }

    router.push("/dashboard");
    router.refresh();
  }

  return (
    <div className="mx-auto max-w-sm">
      <div className="rounded-card border border-border bg-surface p-8 shadow-sm">
        <h1 className="text-2xl font-bold tracking-tight text-ink">Entrar</h1>
        <p className="mt-1 text-sm text-ink-muted">
          Acesse sua conta para dar lances e criar leilões.
        </p>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-ink">
              E-mail
            </label>
            <input id="email" name="email" type="email" required className={inputClass} />
            {state?.errors?.email && (
              <p className="mt-1 text-xs text-red-600 dark:text-red-400">
                {state.errors.email[0]}
              </p>
            )}
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-ink">
              Senha
            </label>
            <input
              id="password"
              name="password"
              type="password"
              required
              className={inputClass}
            />
            {state?.errors?.password && (
              <p className="mt-1 text-xs text-red-600 dark:text-red-400">
                {state.errors.password[0]}
              </p>
            )}
          </div>

          {state?.error && (
            <p className="text-sm text-red-600 dark:text-red-400">{state.error}</p>
          )}

          <button
            type="submit"
            disabled={pending}
            className="w-full rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-brand-600 disabled:opacity-60"
          >
            {pending ? "Entrando..." : "Entrar"}
          </button>
        </form>

        <p className="mt-4 text-sm text-ink-muted">
          Não tem conta?{" "}
          <Link href="/register" className="text-brand-600 hover:underline">
            Criar conta
          </Link>
        </p>
      </div>
    </div>
  );
}
