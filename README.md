## catering

Progetto d'esame Sistemi Informativi su Web.

### Caratteristiche

- Implementa il sistema informativo di una società di catering
- Permette ad un admin di caricare su un db relazionale tutte le entity utili e ad un admin e un user di vederle.  
- Si interfaccia con un database [PostgreSQL](https://www.postgresql.org) su [Amazon RDS](https://aws.amazon.com/it/rds/). In locale a volte la connessione salta.
- Implementa un sistema di login con [Spring Security](https://docs.spring.io/spring-security/reference/getting-spring-security.html#getting-maven-boot) e [OAuth2](https://www.baeldung.com/spring-security-5-oauth2-login) con l'accesso tramite GitHub e Google.
- L'interfaccia grafica è realizzata con [Thymeleaf](https://www.thymeleaf.org/) e [Thymeleaf extra](https://www.baeldung.com/spring-security-thymeleaf) e il css usa [Bootstrap](https://getbootstrap.com/).
- Usa [Apache Commons Lang 3](https://www.baeldung.com/java-commons-lang-3) per generare una stringa casuale da usare come password per gli utenti loggati con google o github. Non è una buona pratica permettere il salvataggio di user senza password perché potrebbero venir usati per accedere al sistema in maniera illecita.
- Sfrutta [Lombok](https://projectlombok.org/) per la scrittura dei metodi getter e setter. 
- Permette il caricamento di immagini da parte dell'admin durante la aggiunta/modifica di una entity. L'user può caricare una sua immagine profilo.
- Il sistema è consultabile al sito: [catering](http://siwcatering-env.eba-rap4upxh.us-east-1.elasticbeanstalk.com/). Purtroppo in questa versione la funzionalità per il caricamento delle immagini delle entity non funziona
