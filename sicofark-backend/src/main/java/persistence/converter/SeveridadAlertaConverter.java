package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.SeveridadAlerta;

/**
 * Converter para mapear el enum SeveridadAlerta de Java al tipo severidad_alerta de PostgreSQL.
 * NOTA: El nombre del enum parece incorrecto (SeveridadAlerta vs SeveridadAlerta).
 * Considera renombrar el enum a SeveridadAlerta para consistencia.
 */
@Converter(autoApply = true)
public class SeveridadAlertaConverter implements AttributeConverter<SeveridadAlerta, String> {

    @Override
    public String convertToDatabaseColumn(SeveridadAlerta attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public SeveridadAlerta convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return SeveridadAlerta.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de severidad_alerta en la base de datos: " + dbData, e
            );
        }
    }
}
