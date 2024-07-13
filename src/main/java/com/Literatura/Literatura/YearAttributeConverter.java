package com.Literatura.Literatura;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.Year;

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(Year attribute) {
        return attribute != null ? attribute.atDay(1) : null;
    }

    @Override
    public Year convertToEntityAttribute(LocalDate dbData) {
        return dbData != null ? Year.from(dbData) : null;
    }
}

