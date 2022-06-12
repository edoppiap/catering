package it.uniroma3.siw.pietropaolo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;
import it.uniroma3.siw.pietropaolo.model.pojo.User;

public interface CredentialsRepository extends CrudRepository<Credentials, Long>{
    
    public Optional<Credentials> findByUsername(String username);

    public boolean existsByUsername(String username);

    public Optional<Credentials> findByUser(User user);
}
