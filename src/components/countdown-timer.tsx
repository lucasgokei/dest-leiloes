"use client";

import { useEffect, useState } from "react";

function formatRemaining(ms: number) {
  if (ms <= 0) return "Encerrado";

  const totalSeconds = Math.floor(ms / 1000);
  const days = Math.floor(totalSeconds / 86400);
  const hours = Math.floor((totalSeconds % 86400) / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const parts = [];
  if (days > 0) parts.push(`${days}d`);
  if (days > 0 || hours > 0) parts.push(`${hours}h`);
  parts.push(`${minutes}m`);
  parts.push(`${seconds}s`);

  return parts.join(" ");
}

export default function CountdownTimer({
  endsAt,
  onExpire,
  className,
}: {
  endsAt: string;
  onExpire?: () => void;
  className?: string;
}) {
  const target = new Date(endsAt).getTime();
  const [remaining, setRemaining] = useState<number | null>(null);

  useEffect(() => {
    function tick() {
      setRemaining((prev) => {
        const next = target - Date.now();
        if (prev !== null && prev > 0 && next <= 0) {
          onExpire?.();
        }
        return next;
      });
    }

    const immediate = setTimeout(tick, 0);
    const interval = setInterval(tick, 1000);

    return () => {
      clearTimeout(immediate);
      clearInterval(interval);
    };
  }, [target, onExpire]);

  if (remaining === null) {
    return <span className={`${className ?? ""} text-ink-muted font-mono`}>—</span>;
  }

  const expired = remaining <= 0;

  return (
    <span
      className={`${className ?? ""} ${expired ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400"} font-mono`}
    >
      {formatRemaining(remaining)}
    </span>
  );
}
