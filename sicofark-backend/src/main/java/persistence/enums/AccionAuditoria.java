package persistence.enums;

public enum AccionAuditoria {

        CREAR("Creación de registro"),
        ACTUALIZAR("Actualización de registro"),
        ELIMINAR("Eliminación de registro"),
        CONSULTAR("Consulta de información"),
        ANULAR("Anulación de registro"),
        LOGIN("Inicio de sesión en el sistema"),
        LOGOUT("Cierre de sesión del sistema");

        private final String descripcion;

        AccionAuditoria(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
}
