package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoLote;

/**
 * Converter para mapear el enum EstadoLote de Java al tipo estado_lote de PostgreSQL.
 */
@Converter(autoApply = true)
public class EstadoLoteConverter implements AttributeConverter<EstadoLote, String> {

    @Override
    public String convertToDatabaseColumn(EstadoLote attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoLote convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoLote.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_lote en la base de datos: " + dbData, e
            );
        }
    }
}
