select setval('threads_id_seq', (select NEXTVAL('computational_threads_id_seq')));

select setval('threads_etls_id_seq', (select NEXTVAL('computational_threads_etls_id_seq')));

select setval('threads_execution_id_seq', (select NEXTVAL('computational_threads_execution_id_seq')));

select setval('threads_execution_etls_id_seq', (select NEXTVAL('computational_threads_execution_etls_id_seq')));
