export interface GenericConfig {
    cas: {
        login;
        logout;
        service;
    };
    installation: {
        type;
        instance;
    };
    metadata: {
        navbarScriptUrl: string;
    };
}
