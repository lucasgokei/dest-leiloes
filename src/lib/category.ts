export type AuctionCategory = "VEHICLES" | "PROPERTIES" | "ELECTRONICS" | "OTHERS";

export const AUCTION_CATEGORIES: AuctionCategory[] = [
  "VEHICLES",
  "PROPERTIES",
  "ELECTRONICS",
  "OTHERS",
];

export const CATEGORY_LABELS: Record<AuctionCategory, string> = {
  VEHICLES: "Veículos",
  PROPERTIES: "Imóveis",
  ELECTRONICS: "Eletrônicos",
  OTHERS: "Outros",
};

export const CATEGORY_ICONS: Record<AuctionCategory, string> = {
  VEHICLES: "🚗",
  PROPERTIES: "🏠",
  ELECTRONICS: "💻",
  OTHERS: "📦",
};

export function isAuctionCategory(value: string): value is AuctionCategory {
  return (AUCTION_CATEGORIES as string[]).includes(value);
}
