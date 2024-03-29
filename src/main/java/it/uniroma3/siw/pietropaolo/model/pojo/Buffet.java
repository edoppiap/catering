package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Buffet implements Comparable<Buffet>{
	
	public Buffet() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String descr;

	private String nomeFoto;
	
	/**
	 * La Fetch la lascio di default (Eager) perché quando chiamo la pagina per la lista buffet
	 * leggo anche i dati degli chef che presentano ogni buffet
	 */
	@NotNull
	@ManyToOne
	private Chef chef;
	
	/**
	 * La Fetch la lascio di default (Lazy) perché quando chiamo la pagina per la lista buffet
	 * non devo leggere anche i dati dei piatti
	 */
	@ManyToMany
	private List<Piatto> piatti;

	@Override
	public int hashCode() {
		return Objects.hash(descr, nome);
	}

	public String getImmaginePath(){
		if(getNomeFoto() == null || getId() == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/fotoBuffet/").append(getId()).append("/").append(getNomeFoto());
		return builder.toString();
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
		Buffet other = (Buffet) obj;
		return Objects.equals(descr, other.descr) && Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Buffet [id=");
		builder.append(id);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", descr=");
		builder.append(descr);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Buffet that) {
		return this.getNome().compareTo(that.getNome());
	}

	
}
