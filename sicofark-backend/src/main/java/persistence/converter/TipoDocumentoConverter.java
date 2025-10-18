package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.TipoDocumento;

/**
 * Converter para mapear el enum TipoDocumento de Java al tipo tipo_documento de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoDocumentoConverter implements AttributeConverter<TipoDocumento, String> {

    @Override
    public String convertToDatabaseColumn(TipoDocumento attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoDocumento convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return TipoDocumento.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de tipo_documento en la base de datos: " + dbData, e
            );
        }
    }
}
