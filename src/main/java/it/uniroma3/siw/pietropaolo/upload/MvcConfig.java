package it.uniroma3.siw.pietropaolo.upload;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        getCartella("fotoBuffet", registry);
        getCartella("fotoChef", registry);
        getCartella("fotoPiatto", registry);
        getCartella("fotoIngrediente", registry);
        getCartella("fotoUtente", registry);
    }

    private void getCartella(String nomeCartella, ResourceHandlerRegistry registry){
        Path caricaCartella = Paths.get(nomeCartella);
        String caricaPath = caricaCartella.toFile().getAbsolutePath();

        if(nomeCartella.startsWith("../")){
            nomeCartella = nomeCartella.replace("../", "");
        }

        registry.addResourceHandler("/"+nomeCartella+"/**").addResourceLocations("file:/"+caricaPath+"/");
    }*/
    
}
