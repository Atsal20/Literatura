package com.Literatura.Literatura;

import com.Literatura.Literatura.repository.AutorRepository;
import com.Literatura.Literatura.repository.LibroRepository;
import com.Literatura.Literatura.service.ConsumoAPI;
import com.Literatura.Literatura.service.ConvierteDatos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository repositoryLibro;

    @Autowired
    private AutorRepository repositoryAutor;

    @Autowired
    private ConsumoAPI consumoApi;

    @Autowired
    private ConvierteDatos conversor;



    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(consumoApi, conversor, repositoryLibro, repositoryAutor);
        principal.muestraElMenu();
    }
}
