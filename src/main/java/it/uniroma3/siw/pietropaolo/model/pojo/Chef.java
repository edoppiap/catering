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
public class Chef {
	
	private Chef() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	@SuppressWarnings("unused")
	private String nazionalita;

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
}
