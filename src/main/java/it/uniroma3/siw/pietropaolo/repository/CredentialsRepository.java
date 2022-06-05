package it.uniroma3.siw.pietropaolo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long>{
    
    public Optional<Credentials> findByUsername(String username);
}
