package it.uniroma3.siw.pietropaolo.model.pojo;

public enum Classificazione {

    ANTIPASTO("Antipasto"), 
    PRIMO("Primo"), 
    SECONDO("Secondo"), 
    CONTORNO("Contorno"), 
    DOLCE("Dolce"),
    FRUTTA("Frutta");

    private String value;

    Classificazione(String value){
        this.value=value;
    }

    public String getValue(){ return this.value; }

    public Classificazione getClassificazione(String value){
        for(Classificazione classificazione : Classificazione.values()){
            if(value.equals(classificazione.value)){
                return classificazione;
            }
        }
        return null;
    }
    
}
