package it.uniroma3.siw.pietropaolo.auth;

import static it.uniroma3.siw.pietropaolo.model.pojo.Credentials.ADMIN_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter{

    /**
     * datasource viene automaticamente iniettato e viene usato per accedere al db
     */
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                        //authoriztion paragraph: qui definiamo chi può accedere a cosa
                        .authorizeRequests()
                        //chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
                        .antMatchers(HttpMethod.GET, "/", "/index", "/login", "/register", "/users/**").permitAll()
                        //chiunque (autenticato o no) può accedere alle richieste POST al punto di accesso per login e register
                        .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                        //solo gli utenti autenticati con ruolo ADMIN possono accedere a risorse con path /admin/**
                        .antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
                        .antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
                        //
                        .anyRequest().authenticated()

                        //login paragraph: qui definiamo come è gestita l'autenticazione
                        //usiamo il protocollo formlogin
                        .and().formLogin()
                        //la pagina di login si trova a /login
                        // NOTA: Spring gestisce il post di login automaticamente
                        .loginPage("/login")
                        //se il login ha successo, si viene rediretti al path /default
                        .defaultSuccessUrl("/default")

                        //logou paragraph: qui definiamo il logout
                        .and().logout()
                        //il logout è attivato con una richiesta GET a "/logout"
                        .logoutUrl("/logout")
                        //in caso di successo, si viene reindirizzati alla /index page
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true).permitAll();
    }

    /**
     * This method provide the SQL queries to get username and password.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
            //use the autowired datasource to access the saved credentials
            .dataSource(this.dataSource)
            //retrieve username and role
            .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
            //retrieve username, password and a boolean flag specifying whether the user is enabled or not (always enabled in our case)
            .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    /**
     * this method defines a "passwordEncoder" Bean.
     * The passwordEncoder BEan is used to encrypt and decrypt the Credentials passwords.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
