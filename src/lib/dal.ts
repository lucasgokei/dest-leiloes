import "server-only";
import { cache } from "react";
import { redirect } from "next/navigation";
import { decrypt, getSessionCookie } from "@/lib/session";
import { apiFetchServer } from "@/lib/api-client";

export const verifySession = cache(async () => {
  const cookie = await getSessionCookie();
  const session = await decrypt(cookie);

  if (!session?.userId) {
    return null;
  }

  return { isAuth: true, userId: session.userId, role: session.role };
});

export const requireSession = cache(async () => {
  const session = await verifySession();
  if (!session) {
    redirect("/login");
  }
  return session;
});

export const requireAdmin = cache(async () => {
  const session = await requireSession();
  if (session.role !== "ADMIN") {
    redirect("/");
  }
  return session;
});

type UserResponse = {
  id: string;
  name: string;
  email: string;
  role: "USER" | "ADMIN";
  createdAt: string;
};

export const getUser = cache(async () => {
  const session = await verifySession();
  if (!session) return null;

  const result = await apiFetchServer<UserResponse>("/api/users/me");
  if (!result.ok) return null;
  return result.data;
});
