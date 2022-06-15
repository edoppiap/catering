## catering

Progetto d'esame Sistemi Informativi su Web.

### Caratteristiche

- Implementa il sistema informativo di una società di catering
- Permette ad un admin di caricare su un db relazionale tutte le entity utili e ad un admin e un user di vederle.  
- Si interfaccia con un database [PostgreSQL] su [Amazon RDS]. In locale a volte la connessione salta. (https://www.postgresql.org, https://aws.amazon.com/it/rds/)
- Implementa un sistema di login con [Spring Security] e [OAuth2] con l'accesso tramite GitHub e Google.(https://docs.spring.io/spring-security/reference/getting-spring-security.html#getting-maven-boot, https://www.baeldung.com/spring-security-5-oauth2-login)
- L'interfaccia grafica è realizzata con [Thymeleaf] e [Thymeleaf extra] e il css usa [Bootstrap]. (https://www.thymeleaf.org/, https://www.baeldung.com/spring-security-thymeleaf, https://getbootstrap.com/)
- Usa [Apache Commons Lang 3] per generare una stringa casuale da usare come password per gli utenti loggati con google o github. Non è una buona pratica permettere il salvataggio di user senza password perché potrebbero venir usati per accedere al sistema in maniera illecita. (https://www.baeldung.com/java-commons-lang-3)
- Sfrutta [Lombok] per la scrittura dei metodi getter e setter. (https://projectlombok.org/)
- Permette il caricamento di immagini da parte dell'admin durante la aggiunta/modifica di una entity. L'user può caricare una sua immagine profilo.
- Il sistema è consultabile al sito: [catering]. Purtroppo in questa versione le funzionalità per il caricamento delle immagini delle entity non funziona (http://siwcatering-env.eba-rap4upxh.us-east-1.elasticbeanstalk.com/)
