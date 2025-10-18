package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoBodega;

/**
 * Converter para mapear el enum EstadoBodega de Java al tipo estado_bodega de PostgreSQL.
 */
@Converter(autoApply = true)
public class EstadoBodegaConverter implements AttributeConverter<EstadoBodega, String> {

    @Override
    public String convertToDatabaseColumn(EstadoBodega attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoBodega convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoBodega.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_bodega en la base de datos: " + dbData, e
            );
        }
    }
}
