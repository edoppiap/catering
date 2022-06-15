package it.uniroma3.siw.pietropaolo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.User;

public interface UserRepository extends CrudRepository<User, Long>{

    public boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);
    
}
