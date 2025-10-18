package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoTransaccion;

/**
 * Converter para mapear el enum EstadoTransaccion de Java al tipo estado_transaccion de PostgreSQL.
 */
@Converter(autoApply = true)
public class EstadoTransaccionConverter implements AttributeConverter<EstadoTransaccion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoTransaccion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoTransaccion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoTransaccion.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_transaccion en la base de datos: " + dbData, e
            );
        }
    }
}
