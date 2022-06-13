package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Piatto {
	
	public Piatto() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String descrizione;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Classificazione classificazione;

	private String nomeFoto;
	
	/**
	 * La Fetch la lascio di default (Lazy) perché quando chiamo la pagina per la lista piatti
	 * non devo leggere anche i dati dei buffet
	 */
	@ManyToMany(mappedBy = "piatti")
	private List<Buffet> buffets;
	
	/**
	 * La Fetch la cambio dal default (Lazy) e la metto Eager perché quando chiamo la pagina per la lista piatti
	 * leggo anche la lista di ingredienti
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Ingrediente> ingredienti;

	public String getImmaginePath(){
		if(getNomeFoto() == null || getId() == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/fotoPiatto/").append(getId()).append("/").append(getNomeFoto());
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
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
		Piatto other = (Piatto) obj;
		return Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Piatto [id=");
		builder.append(id);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", descrizione=");
		builder.append(descrizione);
		builder.append("]");
		return builder.toString();
	}

}
