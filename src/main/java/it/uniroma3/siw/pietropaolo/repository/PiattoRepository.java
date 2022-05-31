package it.uniroma3.siw.pietropaolo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;

public interface PiattoRepository extends CrudRepository<Piatto, Long>{

    public boolean existsByNome(String nome);

    public Piatto findByNome(String nome);

}
