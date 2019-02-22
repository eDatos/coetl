export interface EntityFilter {
    fromQueryParams(params: any): any;
    reset(): void;
    toQuery(): string;
}
