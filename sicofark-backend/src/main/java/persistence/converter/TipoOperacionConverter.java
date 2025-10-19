package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoOperacion;

/**
 * Converter para mapear el enum TipoOperacion de Java al tipo tipo_operacion de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoOperacionConverter implements AttributeConverter<TipoOperacion, String> {

    @Override
    public String convertToDatabaseColumn(TipoOperacion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoOperacion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoOperacion.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_operacion en la base de datos: " + dbData, e
            );
        }
    }
}
