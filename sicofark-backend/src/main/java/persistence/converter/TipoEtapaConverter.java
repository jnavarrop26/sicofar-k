package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoEtapa;

/**
 * Converter para mapear el enum TipoEtapa de Java al tipo tipo_etapa de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoEtapaConverter implements AttributeConverter<TipoEtapa, String> {

    @Override
    public String convertToDatabaseColumn(TipoEtapa attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoEtapa convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoEtapa.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_etapa en la base de datos: " + dbData, e
            );
        }
    }
}
