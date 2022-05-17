package it.uniroma3.siw.pietropaolo.model.pojo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Ingrediente {
	
	private Ingrediente() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String origine;
	
	@SuppressWarnings("unused")
	private String descr;

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
