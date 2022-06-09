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

	@ManyToMany(mappedBy = "ingredienti", cascade = CascadeType.ALL)
	private List<Piatto> piatti;

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
