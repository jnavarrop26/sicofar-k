package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoAlerta;

/**
 * Converter para mapear el enum EstadoAlerta de Java al tipo estado_alerta de PostgreSQL.
 */
@Converter(autoApply = true)
public class EstadoAlertaConverter implements AttributeConverter<EstadoAlerta, String> {

    @Override
    public String convertToDatabaseColumn(EstadoAlerta attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoAlerta convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoAlerta.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_alerta en la base de datos: " + dbData, e
            );
        }
    }
}
