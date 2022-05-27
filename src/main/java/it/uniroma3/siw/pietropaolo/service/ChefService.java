package it.uniroma3.siw.pietropaolo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.repository.ChefRepository;

@Service
public class ChefService {
	
	@Autowired 
	private ChefRepository chefRepository;
	
	public void save(Chef chef) {
		this.chefRepository.save(chef);
	}
	
	public boolean alreadyExists(Chef chef) {
		return this.chefRepository.existsByNomeAndCognome(chef.getNome(), chef.getCognome());
	}
	
	public Chef findById(Long id) {
		return this.chefRepository.findById(id).orElse(null);
	}
	
	public List<Chef> findAll(){
		return StreamSupport.stream(chefRepository.findAll().spliterator(), true)
			.sorted().toList();
	}

    public void deleteBuffetById(Long id) {
		this.chefRepository.deleteById(id);
    }

}
