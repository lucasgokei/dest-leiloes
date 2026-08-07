import type { ReactNode } from "react";
import {
  CATEGORY_ICONS,
  CATEGORY_LABELS,
  type AuctionCategory,
} from "@/lib/category";

type AuctionStatus = "PENDING" | "ACTIVE" | "CLOSED" | "CANCELLED";
type BadgeTone = "neutral" | "success" | "danger" | "brand";

const TONE_CLASSES: Record<BadgeTone, string> = {
  neutral: "bg-ink/8 text-ink-muted",
  success: "bg-emerald-600/10 text-emerald-700 dark:text-emerald-400",
  danger: "bg-red-600/10 text-red-700 dark:text-red-400",
  brand: "bg-brand-500/10 text-brand-600 dark:text-brand-400",
};

export function Badge({
  tone = "neutral",
  children,
  className = "",
}: {
  tone?: BadgeTone;
  children: ReactNode;
  className?: string;
}) {
  return (
    <span
      className={`inline-flex items-center whitespace-nowrap rounded-full px-2.5 py-0.5 text-xs font-medium ${TONE_CLASSES[tone]} ${className}`}
    >
      {children}
    </span>
  );
}

const STATUS_LABELS: Record<AuctionStatus, string> = {
  PENDING: "Pendente",
  ACTIVE: "Ativo",
  CLOSED: "Encerrado",
  CANCELLED: "Cancelado",
};

const STATUS_TONES: Record<AuctionStatus, BadgeTone> = {
  PENDING: "neutral",
  ACTIVE: "success",
  CLOSED: "neutral",
  CANCELLED: "danger",
};

export function StatusBadge({
  status,
  className,
}: {
  status: AuctionStatus;
  className?: string;
}) {
  return (
    <Badge tone={STATUS_TONES[status]} className={className}>
      {STATUS_LABELS[status]}
    </Badge>
  );
}

export function CategoryBadge({
  category,
  className,
}: {
  category: AuctionCategory;
  className?: string;
}) {
  return (
    <Badge tone="brand" className={className}>
      <span className="mr-1">{CATEGORY_ICONS[category]}</span>
      {CATEGORY_LABELS[category]}
    </Badge>
  );
}
