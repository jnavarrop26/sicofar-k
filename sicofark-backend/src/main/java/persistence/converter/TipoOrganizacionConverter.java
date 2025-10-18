package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoOrganizacion;

/**
 * Converter para mapear el enum TipoOrganizacion de Java al tipo tipo_organizacion de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoOrganizacionConverter implements AttributeConverter<TipoOrganizacion, String> {

    @Override
    public String convertToDatabaseColumn(TipoOrganizacion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoOrganizacion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoOrganizacion.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_organizacion en la base de datos: " + dbData, e
            );
        }
    }
}
