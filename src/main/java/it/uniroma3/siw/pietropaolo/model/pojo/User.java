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

    @Override
    public boolean equals(Object other){
        return this.getId().equals(((User)other).getId());
    }

    @Override
    public int hashCode(){
        return this.getNome().hashCode() + this.getCognome().hashCode();
    }
}
