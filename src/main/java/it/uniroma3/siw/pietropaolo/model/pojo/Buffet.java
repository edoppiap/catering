package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Buffet {
	
	private Buffet() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@SuppressWarnings("unused")
	private String descr;
	
	@ManyToOne
	private Chef chef;
	
	@OneToMany(mappedBy="chef")
	private List<Piatto> piatti;

	@Override
	public int hashCode() {
		return Objects.hash(descr, nome);
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
}
