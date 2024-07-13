package com.Literatura.Literatura.Model;

import com.Literatura.Literatura.YearAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Convert(converter = YearAttributeConverter.class)
    private Year fechaNacimiento;

    @Convert(converter = YearAttributeConverter.class)
    private Year fechaFallecimiento;

    public Autor(DatosAutor datos) {
        this.nombre = datos.nombre();
        this.fechaNacimiento = datos.fechaNacimiento();
        this.fechaFallecimiento = datos.fechaFallecimiento();
    }
}
