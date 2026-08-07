import "server-only";
import { jwtVerify } from "jose";
import { cookies } from "next/headers";

export type Role = "USER" | "ADMIN";

export type SessionPayload = {
  userId: string;
  role: Role;
  expiresAt: number;
};

const secretKey = process.env.SESSION_SECRET;
if (!secretKey) {
  throw new Error("SESSION_SECRET não está definida no .env");
}
const encodedKey = new TextEncoder().encode(secretKey);

const SESSION_COOKIE_NAME = "session";

/**
 * Verifica o JWT emitido pelo backend Java (mesma chave HS256, ver
 * backend/src/main/java/com/destleiloes/security/JwtService.java). O Next não
 * emite mais sessões — só valida o cookie para proteger rotas no middleware.
 */
export async function decrypt(
  session: string | undefined = ""
): Promise<SessionPayload | null> {
  if (!session) return null;
  try {
    const { payload } = await jwtVerify(session, encodedKey, {
      algorithms: ["HS256"],
    });
    return payload as unknown as SessionPayload;
  } catch {
    return null;
  }
}

export async function getSessionCookie() {
  const cookieStore = await cookies();
  return cookieStore.get(SESSION_COOKIE_NAME)?.value;
}

export { SESSION_COOKIE_NAME };
