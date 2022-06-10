package it.uniroma3.siw.pietropaolo.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.repository.PiattoRepository;

@Service
public class PiattoService {
	
	@Autowired 
	private PiattoRepository piattoRepository;

	public void updatePiatto(Piatto piattoAggiornato){
		save(piattoAggiornato);
	}

	public Piatto save(Piatto piatto){
		return piattoRepository.save(piatto);
	}

	public boolean alreadyExists(Piatto piatto){
		if(piatto.getId() == null){
			return piattoRepository.existsByNome(piatto.getNome());
		}else if(piattoRepository.existsByNome(piatto.getNome())){
			Piatto piattoEsistente = piattoRepository.findByNome(piatto.getNome());
			return !(piatto.getId().equals(piattoEsistente.getId()));
		}else{
			return false;
		}
	}

	public Piatto findById(Long id){
		return piattoRepository.findById(id).orElse(null);
	}

	public Piatto findByNome(String nome){
		return piattoRepository.findByNome(nome);
	}

	public List<Piatto> findAll(){
		return StreamSupport.stream(piattoRepository.findAll().spliterator(), true)
			.toList();
	}

	public void deletePiattoById(Long id){
		piattoRepository.deleteById(id);
	}

	public void deleteAll(){
		piattoRepository.deleteAll();
	}

}
