package br.gov.df.seagri;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SeagriApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeagriApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Força o Java a rodar em UTC, garantindo total alinhamento com o
        // Banco de Dados e evitando surpresas com fusos horários
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
