export type ApiResult<T = unknown> = {
  ok: boolean;
  status: number;
  data: T;
};

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
const BACKEND_INTERNAL_URL = process.env.BACKEND_INTERNAL_URL ?? API_URL;

async function parseResponse<T>(response: Response): Promise<ApiResult<T>> {
  const text = await response.text();
  const data = text ? JSON.parse(text) : ({} as T);
  return { ok: response.ok, status: response.status, data };
}

/** Chamadas feitas do navegador (client components). Repassa o cookie de sessão automaticamente. */
export async function apiFetch<T = unknown>(
  path: string,
  init: RequestInit = {}
): Promise<ApiResult<T>> {
  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...init.headers,
    },
  });
  return parseResponse<T>(response);
}

/** Chamadas feitas em Server Components, repassando manualmente o cookie da request atual. */
export async function apiFetchServer<T = unknown>(
  path: string,
  init: RequestInit = {}
): Promise<ApiResult<T>> {
  const { cookies } = await import("next/headers");
  const cookieStore = await cookies();
  const cookieHeader = cookieStore
    .getAll()
    .map((c) => `${c.name}=${c.value}`)
    .join("; ");

  const response = await fetch(`${BACKEND_INTERNAL_URL}${path}`, {
    ...init,
    cache: "no-store",
    headers: {
      "Content-Type": "application/json",
      cookie: cookieHeader,
      ...init.headers,
    },
  });
  return parseResponse<T>(response);
}
