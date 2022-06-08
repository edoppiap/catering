package it.uniroma3.siw.pietropaolo.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.Ingrediente;
import it.uniroma3.siw.pietropaolo.repository.IngredienteRepository;

@Service
public class IngredienteService {

	@Autowired 
	private IngredienteRepository ingredienteRepository;

	public Ingrediente findById(Long id){
		return ingredienteRepository.findById(id).orElse(null);
	}

	public boolean alreadyExists(Ingrediente ingrediente){
		if(ingrediente.getId() == null){
			return ingredienteRepository.existsByNome(ingrediente.getNome());
		}else if(ingredienteRepository.existsByNome(ingrediente.getNome())){
			Ingrediente ingredienteEsistente = ingredienteRepository.findByNome(ingrediente.getNome());
			return !(ingrediente.getId().equals(ingredienteEsistente.getId()));
		}else{
			return false;
		}
	}

	public void save(Ingrediente ingrediente){
		ingredienteRepository.save(ingrediente);
	}

	public void deleteIngredienteById(Long id){
		ingredienteRepository.deleteById(id);
	}

	public List<Ingrediente> findAll(){
		return StreamSupport.stream(ingredienteRepository.findAll().spliterator(), true)
			.toList();
	}
}
