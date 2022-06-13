package it.uniroma3.siw.pietropaolo.model.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    public User() {}

    public User(String nome, String cognome){
        setNome(nome);
        setCognome(cognome);
    }

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    
    @NotBlank
    String nome;

    @NotBlank
    String cognome;

    @NotBlank
    String email;

    String nomeFoto;

    public String getImmaginePath(){
		if(getNomeFoto() == null || getId() == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/fotoUtente/").append(getId()).append("/").append(getNomeFoto());
		return builder.toString();
	}

    @Override
    public boolean equals(Object other){
        return this.getId().equals(((User)other).getId());
    }

    @Override
    public int hashCode(){
        return this.getNome().hashCode() + this.getCognome().hashCode();
    }
}
