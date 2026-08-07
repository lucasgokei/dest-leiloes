export default function Logo({ className = "" }: { className?: string }) {
  return (
    <span className={`inline-flex items-center gap-2 ${className}`}>
      <span className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-brand-500 text-white">
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          aria-hidden="true"
        >
          <rect
            x="1.5"
            y="12.8"
            width="10"
            height="3"
            rx="1"
            transform="rotate(-45 1.5 12.8)"
            fill="currentColor"
          />
          <rect
            x="9"
            y="5.3"
            width="10.5"
            height="4.2"
            rx="1"
            transform="rotate(45 9 5.3)"
            fill="currentColor"
          />
          <rect x="2.5" y="18.5" width="9" height="2.4" rx="1.2" fill="currentColor" />
        </svg>
      </span>
      <span className="text-lg font-bold tracking-tight text-ink">
        Dest <span className="text-brand-600">Leilões</span>
      </span>
    </span>
  );
}
