package com.Literatura.Literatura.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Idioma lenguaje;
    private Long numero_descargas;

    @ManyToOne
    private Autor autor;

    public Libro(DatosLibros datos) {
        this.titulo = datos.titulo();
        if (!datos.lenguaje().isEmpty()) {
            this.lenguaje = Idioma.fromString(datos.lenguaje().getFirst());
        } else {
            throw new IllegalArgumentException("Lista de idiomas vacía.");
        }
         this.numero_descargas = datos.numero_descargas();
    }
}