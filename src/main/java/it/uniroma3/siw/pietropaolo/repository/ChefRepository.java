package it.uniroma3.siw.pietropaolo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long>{
	
	public boolean existsByNomeAndCognome(String nome, String cognome);

}
