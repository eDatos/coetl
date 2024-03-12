package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.ComputationalThreads;

public interface ComputationalThreadsService {

    public ComputationalThreads create(ComputationalThreads computationalThreads);
    public ComputationalThreads findOne(Long id);

}
