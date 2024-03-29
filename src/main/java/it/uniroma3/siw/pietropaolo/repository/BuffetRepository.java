package it.uniroma3.siw.pietropaolo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;

public interface BuffetRepository extends CrudRepository<Buffet, Long>{
	
	public boolean existsByNome(String nome);

	public Buffet findByNome(String nome);

	public void deleteById(Long id);

	public List<Buffet> findByChefId(Long id);

}
