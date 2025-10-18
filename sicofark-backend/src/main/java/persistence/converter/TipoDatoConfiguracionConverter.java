package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoDatoConfiguracion;

/**
 * Converter para mapear el enum TipoDatoConfiguracion de Java al tipo tipo_dato_config de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoDatoConfiguracionConverter implements AttributeConverter<TipoDatoConfiguracion, String> {

    @Override
    public String convertToDatabaseColumn(TipoDatoConfiguracion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoDatoConfiguracion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoDatoConfiguracion.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_dato_config en la base de datos: " + dbData, e
            );
        }
    }
}
