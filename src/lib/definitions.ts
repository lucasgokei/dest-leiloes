export type FieldErrors = Record<string, string[]>;

/** Espelha o formato de erro devolvido pela API Java (ver GlobalExceptionHandler). */
export type FormState =
  | {
      errors?: FieldErrors;
      error?: string;
    }
  | undefined;
