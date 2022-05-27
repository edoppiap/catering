package it.uniroma3.siw.pietropaolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.repository.BuffetRepository;

@Service
public class BuffetService {
	
	@Autowired 
	private BuffetRepository buffetRepository;
	
	public Buffet findById(Long id) {
		return buffetRepository.findById(id).orElse(null);
	}

	public Buffet findByNome(String nome){
		return buffetRepository.findByNome(nome);
	}

	public void updateBuffet(Buffet buffetAggiornato){
		this.save(buffetAggiornato);
	}
	
	public void save(Buffet buffet) {
		this.buffetRepository.save(buffet);
	}
	
	public boolean alreadyExists(Buffet buffet) {
		if(buffet.getId() == null){
			return this.buffetRepository.existsByNome(buffet.getNome());
		}else if(buffetRepository.existsByNome(buffet.getNome())){
			Buffet buffetEsistente = buffetRepository.findByNome(buffet.getNome());
			return !(buffet.getId() == buffetEsistente.getId());
		}else{
			return false;
		}
	}

	public List<Buffet> findAll() {
		return StreamSupport.stream(this.buffetRepository.findAll().spliterator(), true)
			.sorted()
			.toList();
	}

	public void deleteBuffetById(Long id){
		this.buffetRepository.deleteById(id);
	}

	public void deleteAll(){
		this.buffetRepository.deleteAll();
	}

}
