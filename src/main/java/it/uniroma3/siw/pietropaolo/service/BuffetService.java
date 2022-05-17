package it.uniroma3.siw.pietropaolo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.repository.BuffetRepository;

@Service
public class BuffetService {
	
	Logger logger = LoggerFactory.getLogger(BuffetService.class);
	
	@Autowired BuffetRepository buffetRepository;
	
	public Buffet findById(Long id) {
		return buffetRepository.findById(id).orElse(null);
	}
	
	public void save(Buffet buffet) {
		this.buffetRepository.save(buffet);
	}
	
	public boolean alreadyExists(Buffet buffet) {
		if(buffet.getId() != null) {
			return this.buffetRepository.existsById(buffet.getId());
		}else {
			return this.buffetRepository.existsByNome(buffet.getNome());
		}
	}

	public List<Buffet> findAll() {
		List<Buffet> listaBuffet = new ArrayList<>();
		for(Buffet buffet : this.buffetRepository.findAll()) {
			listaBuffet.add(buffet);
		}
		logger.info(listaBuffet.toString());
		return listaBuffet;
	}

}
