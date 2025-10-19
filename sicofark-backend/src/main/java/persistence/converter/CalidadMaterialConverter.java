package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.CalidadMaterial;

/**
 * Converter para mapear el enum CalidadMaterial de Java al tipo calidad_material de PostgreSQL.
 */
@Converter(autoApply = true)
public class CalidadMaterialConverter implements AttributeConverter<CalidadMaterial, String> {

    @Override
    public String convertToDatabaseColumn(CalidadMaterial attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public CalidadMaterial convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return CalidadMaterial.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de calidad_material en la base de datos: " + dbData, e
            );
        }
    }
}
