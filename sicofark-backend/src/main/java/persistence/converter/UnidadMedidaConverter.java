package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.UnidadMedida;

/**
 * Converter para mapear el enum UnidadMedida de Java al tipo unidad_medida de PostgreSQL.
 */
@Converter(autoApply = true)
public class UnidadMedidaConverter implements AttributeConverter<UnidadMedida, String> {

    @Override
    public String convertToDatabaseColumn(UnidadMedida attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public UnidadMedida convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return UnidadMedida.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de unidad_medida en la base de datos: " + dbData, e
            );
        }
    }
}
