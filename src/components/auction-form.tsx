"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api-client";
import type { FormState } from "@/lib/definitions";
import { AUCTION_CATEGORIES, CATEGORY_LABELS } from "@/lib/category";

const inputClass =
  "mt-1 w-full rounded-lg border border-border bg-surface px-3 py-2 text-sm text-ink outline-none focus:border-brand-500";

export default function AuctionForm() {
  const router = useRouter();
  const [state, setState] = useState<FormState>(undefined);
  const [pending, setPending] = useState(false);

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setPending(true);
    setState(undefined);

    const formData = new FormData(e.currentTarget);
    const result = await apiFetch<NonNullable<FormState> & { id?: string }>("/api/auctions", {
      method: "POST",
      body: JSON.stringify({
        title: formData.get("title"),
        description: formData.get("description"),
        imageUrl: formData.get("imageUrl") || "",
        startingPrice: Number(formData.get("startingPrice")),
        durationMinutes: Number(formData.get("durationMinutes")),
        category: formData.get("category"),
      }),
    });

    setPending(false);

    if (!result.ok) {
      setState(result.data);
      return;
    }

    router.push(`/auctions/${result.data.id}`);
    router.refresh();
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="title" className="block text-sm font-medium text-ink">
          Título
        </label>
        <input id="title" name="title" type="text" required className={inputClass} />
        {state?.errors?.title && (
          <p className="mt-1 text-xs text-red-600 dark:text-red-400">{state.errors.title[0]}</p>
        )}
      </div>

      <div>
        <label htmlFor="description" className="block text-sm font-medium text-ink">
          Descrição
        </label>
        <textarea
          id="description"
          name="description"
          rows={4}
          required
          className={inputClass}
        />
        {state?.errors?.description && (
          <p className="mt-1 text-xs text-red-600 dark:text-red-400">
            {state.errors.description[0]}
          </p>
        )}
      </div>

      <div>
        <label htmlFor="category" className="block text-sm font-medium text-ink">
          Categoria
        </label>
        <select id="category" name="category" required defaultValue="" className={inputClass}>
          <option value="" disabled>
            Selecione uma categoria
          </option>
          {AUCTION_CATEGORIES.map((category) => (
            <option key={category} value={category}>
              {CATEGORY_LABELS[category]}
            </option>
          ))}
        </select>
        {state?.errors?.category && (
          <p className="mt-1 text-xs text-red-600 dark:text-red-400">
            {state.errors.category[0]}
          </p>
        )}
      </div>

      <div>
        <label htmlFor="imageUrl" className="block text-sm font-medium text-ink">
          URL da imagem (opcional)
        </label>
        <input
          id="imageUrl"
          name="imageUrl"
          type="url"
          placeholder="https://..."
          className={inputClass}
        />
        {state?.errors?.imageUrl && (
          <p className="mt-1 text-xs text-red-600 dark:text-red-400">
            {state.errors.imageUrl[0]}
          </p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label htmlFor="startingPrice" className="block text-sm font-medium text-ink">
            Lance inicial (R$)
          </label>
          <input
            id="startingPrice"
            name="startingPrice"
            type="number"
            step="0.01"
            min="0.01"
            required
            className={inputClass}
          />
          {state?.errors?.startingPrice && (
            <p className="mt-1 text-xs text-red-600 dark:text-red-400">
              {state.errors.startingPrice[0]}
            </p>
          )}
        </div>

        <div>
          <label htmlFor="durationMinutes" className="block text-sm font-medium text-ink">
            Duração (minutos)
          </label>
          <input
            id="durationMinutes"
            name="durationMinutes"
            type="number"
            min="1"
            defaultValue={60}
            required
            className={inputClass}
          />
          {state?.errors?.durationMinutes && (
            <p className="mt-1 text-xs text-red-600 dark:text-red-400">
              {state.errors.durationMinutes[0]}
            </p>
          )}
        </div>
      </div>

      {state?.error && (
        <p className="text-sm text-red-600 dark:text-red-400">{state.error}</p>
      )}

      <button
        type="submit"
        disabled={pending}
        className="w-full rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-brand-600 disabled:opacity-60"
      >
        {pending ? "Criando..." : "Publicar leilão"}
      </button>
    </form>
  );
}
