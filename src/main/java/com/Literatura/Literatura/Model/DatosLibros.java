package com.Literatura.Literatura.Model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("languages")List<String> lenguaje,
        @JsonAlias("download_count") Long numero_descargas,
        @JsonAlias("authors")List<DatosAutor> autor) {
}