package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Chef implements Comparable<Chef>{
	
	public Chef() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	private String nazionalita;

	private String nomeFoto;
	
	/**
	 * La Fetch la lascio di default (Lazy) perché quando chiamo la pagina per la lista chef
	 * non devo leggere anche i dati dei buffet
	 * 
	 * Aggiungo una politica cascade per eliminare anche i buffet che venivano presentati
	 * dallo chef eliminato
	 */
	@OneToMany(mappedBy="chef", cascade = CascadeType.REMOVE)
	private List<Buffet> listaBuffet;

	public String getImmaginePath(){
		if(getNomeFoto() == null || getId() == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/fotoChef/").append(getId()).append("/").append(getNomeFoto());
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, nazionalita, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Chef other = (Chef) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(nazionalita, other.nazionalita)
				&& Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chef [id=");
		builder.append(id);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", cognome=");
		builder.append(cognome);
		builder.append(", nazionalita=");
		builder.append(nazionalita);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Chef that) {
		return this.getCognome().compareTo(that.getCognome());
	}
}
