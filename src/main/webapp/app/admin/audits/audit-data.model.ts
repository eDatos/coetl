export class AuditData {
    constructor(
        public remoteAddress: string,
        public sessionId: string,
        public code: string,
    ) { }
}
