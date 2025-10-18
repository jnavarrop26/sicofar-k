package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.CategoriaMaterial;

/**
 * Converter para mapear el enum CategoriaMaterial de Java al tipo categoria_material de PostgreSQL.
 */
@Converter(autoApply = true)
public class CategoriaMaterialConverter implements AttributeConverter<CategoriaMaterial, String> {

    @Override
    public String convertToDatabaseColumn(CategoriaMaterial attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public CategoriaMaterial convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return CategoriaMaterial.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de categoria_material en la base de datos: " + dbData, e
            );
        }
    }
}
