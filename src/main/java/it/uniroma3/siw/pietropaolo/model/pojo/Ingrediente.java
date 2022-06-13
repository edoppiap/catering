package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Ingrediente {
	
	public Ingrediente() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String origine;
	
	private String descr;

	private String nomeFoto;

	/**
	 * La Fetch la lascio di default (Lazy) perché quando chiamo la pagina per la lista ingredienti
	 * non devo leggere anche i dati dei piatti
	 * 
	 * Aggiungo una politica cascade per eliminare anche i piatti che utilizzavano l'ingrediente
	 * eliminato
	 */
	@ManyToMany(mappedBy = "ingredienti", cascade = CascadeType.REMOVE)
	private List<Piatto> piatti;

	public String getImmaginePath(){
		if(getNomeFoto() == null || getId() == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/fotoIngrediente/").append(getId()).append("/").append(getNomeFoto());
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, origine);
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
		Ingrediente other = (Ingrediente) obj;
		return Objects.equals(nome, other.nome) && Objects.equals(origine, other.origine);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ingrediente [id=");
		builder.append(id);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", origine=");
		builder.append(origine);
		builder.append(", descr=");
		builder.append(descr);
		builder.append("]");
		return builder.toString();
	}

}
