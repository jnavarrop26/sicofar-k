package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoAlerta;

/**
 * Converter para mapear el enum TipoAlerta de Java al tipo tipo_alerta de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoAlertaConverter implements AttributeConverter<TipoAlerta, String> {

    @Override
    public String convertToDatabaseColumn(TipoAlerta attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoAlerta convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoAlerta.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_alerta en la base de datos: " + dbData, e
            );
        }
    }
}
