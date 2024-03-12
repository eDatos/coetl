package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreads;

@Repository
public interface ComputationalThreadsRepository extends JpaRepository<ComputationalThreads, Long> {

    ComputationalThreads findOneByCode(String code);

}
