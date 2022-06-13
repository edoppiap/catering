package it.uniroma3.siw.pietropaolo.model.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Credentials {

    public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";

    public Credentials() {}

    public Credentials(String username, String password, String role){
        setUsername(username);
        setPassword(password);
        setRole(role);
    }

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @NotBlank
    @Size(min=4, max=40)
    String username;

    @NotBlank
    @Size(min=6)
    String password;

    @Transient
    String confirmPassword;

    String role;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Override
    public int hashCode(){
        return this.getUsername().hashCode();
    }

    @Override
    public boolean equals(Object obj){
        Credentials that = (Credentials) obj;
        return this.getUsername().equals(that.getUsername());
    }
    
}
