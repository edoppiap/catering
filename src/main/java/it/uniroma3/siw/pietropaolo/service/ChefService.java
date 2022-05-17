package it.uniroma3.siw.pietropaolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.repository.ChefRepository;

@Service
public class ChefService {
	
	@Autowired ChefRepository chefRepository;

}
