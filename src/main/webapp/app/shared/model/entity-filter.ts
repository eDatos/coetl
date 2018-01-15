export interface EntityFilter {
    fromQueryParams(params: any): void;
    reset(): void;
    toQuery(): string;
}
