import { NextRequest, NextResponse } from "next/server";
import { decrypt, SESSION_COOKIE_NAME } from "@/lib/session";

const protectedRoutes = ["/dashboard", "/auctions/novo", "/admin"];
const adminRoutes = ["/admin"];
const authRoutes = ["/login", "/register"];

export default async function proxy(req: NextRequest) {
  const path = req.nextUrl.pathname;

  const isProtectedRoute = protectedRoutes.some(
    (route) => path === route || path.startsWith(`${route}/`)
  );
  const isAdminRoute = adminRoutes.some(
    (route) => path === route || path.startsWith(`${route}/`)
  );
  const isAuthRoute = authRoutes.includes(path);

  const cookie = req.cookies.get(SESSION_COOKIE_NAME)?.value;
  const session = await decrypt(cookie);

  if (isProtectedRoute && !session?.userId) {
    const url = new URL("/login", req.nextUrl);
    url.searchParams.set("next", path);
    return NextResponse.redirect(url);
  }

  if (isAdminRoute && session?.role !== "ADMIN") {
    return NextResponse.redirect(new URL("/", req.nextUrl));
  }

  if (isAuthRoute && session?.userId) {
    return NextResponse.redirect(new URL("/dashboard", req.nextUrl));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico|.*\\.(?:svg|png|jpg|jpeg|gif|webp)$).*)"],
};
