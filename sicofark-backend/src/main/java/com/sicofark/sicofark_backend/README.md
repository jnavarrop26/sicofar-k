# SICOFAR-K Backend - Documentación Técnica

## Índice
1. [Descripción General](#descripción-general)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Arquitectura del Sistema](#arquitectura-del-sistema)
4. [Estructura de Paquetes](#estructura-de-paquetes)
5. [Tecnologías y Uso Detallado](#tecnologías-y-uso-detallado)
6. [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
7. [Configuración y Ejecución](#configuración-y-ejecución)

---

## Descripción General

SICOFAR-K Backend es una aplicación empresarial construida con **Spring Boot 3.5.4** y **Java 21** que proporciona una solución integral para la gestión de operaciones de reciclaje de residuos plásticos. El sistema maneja bodegas, inventarios, proveedores, transacciones y procesamiento de lotes con trazabilidad completa mediante auditorías.

**Punto de Entrada:** `SicofarkBackendApplication.java`

```java
@SpringBootApplication(scanBasePackages = {
    "com.sicofark.sicofark_backend",
    "persistence",
    "service",
    "presentation"
})
@EnableJpaRepositories(basePackages = "persistence.repository")
@EntityScan(basePackages = "persistence.entity")
public class SicofarkBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SicofarkBackendApplication.class, args);
    }
}
```

**Características Principales:**
- Gestión de usuarios con control de acceso basado en roles (RBAC)
- Administración de bodegas con seguimiento de capacidad
- Control de inventarios multi-bodega con umbrales mínimos/máximos
- Gestión de proveedores con sistema de calificación
- Procesamiento de transacciones con estados y pagos
- Trazabilidad de lotes con jerarquías padre-hijo
- Auditoría completa con verificación de integridad (hash)
- Sistema de alertas con gestión de ciclo de vida

---

## Stack Tecnológico

### Core Framework
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Boot** | 3.5.4 | Framework base de la aplicación |
| **Java** | 21 | Lenguaje de programación |
| **Maven** | 3.x | Gestión de dependencias y construcción |

### Persistencia de Datos
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Data JPA** | 3.5.4 | Capa de acceso a datos ORM |
| **Hibernate** | 6.x (incluido) | Implementación JPA |
| **PostgreSQL** | 42.7.3 (driver) | Base de datos relacional |

### Seguridad
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Security** | 3.5.4 | Framework de seguridad |
| **JJWT (Java JWT)** | 0.12.5 | Generación y validación de tokens JWT |

### Desarrollo
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Lombok** | Latest | Reducción de código boilerplate |
| **Spring Boot DevTools** | 3.5.4 | Recarga automática en desarrollo |

### Testing
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Boot Test** | 3.5.4 | Framework de testing |
| **Spring Security Test** | 3.5.4 | Utilidades de testing para seguridad |

---

## Arquitectura del Sistema

### Arquitectura en Capas (Layered Architecture)

El proyecto sigue estrictamente una arquitectura de 3 capas con separación de responsabilidades:

```
┌─────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN (Presentation Layer)  │
│  - Controllers REST (7 controladores)       │
│  - DTOs (10 objetos de transferencia)       │
└─────────────────┬───────────────────────────┘
                  │ Usa DTOs
                  ↓
┌─────────────────────────────────────────────┐
│  CAPA DE SERVICIO (Service Layer)           │
│  - Interfaces (6 contratos)                 │
│  - Implementaciones (6 servicios)           │
│  - Excepciones personalizadas (3)           │
└─────────────────┬───────────────────────────┘
                  │ Usa Entidades
                  ↓
┌─────────────────────────────────────────────┐
│  CAPA DE PERSISTENCIA (Persistence Layer)   │
│  - Entidades JPA (20 entidades)             │
│  - Repositorios (18 repositorios)           │
│  - Enums de dominio (18 enumeraciones)      │
│  - Convertidores JPA (18 convertidores)     │
└─────────────────┬───────────────────────────┘
                  │ JDBC/JPA
                  ↓
┌─────────────────────────────────────────────┐
│  BASE DE DATOS PostgreSQL                   │
│  - Schema: sicofar                          │
│  - Enums nativos de PostgreSQL              │
└─────────────────────────────────────────────┘
```

### Diagrama de Flujo de Datos

```
Cliente HTTP
    ↓
REST Controller (@RestController)
    ↓ DTO Request
Service Interface
    ↓ Implementación
Service Implementation (@Service)
    ↓ Conversión DTO → Entity
Repository (@Repository - Spring Data JPA)
    ↓ Query/Save
Base de Datos PostgreSQL
    ↓ Resultado
Service Implementation
    ↓ Conversión Entity → DTO
    ↓ DTO Response
REST Controller
    ↓ ResponseEntity<DTO>
Cliente HTTP
```

---

## Estructura de Paquetes

```
src/main/java/
│
├── com.sicofark.sicofark_backend/
│   └── SicofarkBackendApplication.java     # Clase principal Spring Boot
│
├── configuration/                           # Configuración de la aplicación
│   ├── app/                                # Configuraciones generales
│   └── security/                           # Configuración de seguridad
│       ├── SecurityConfig.java             # Config principal Spring Security
│       ├── JwtTokenProvider.java           # Generador/validador JWT
│       ├── JwtAuthenticationFilter.java    # Filtro de autenticación
│       ├── CustomUserDetailsServices.java  # Carga de usuarios desde DB
│       └── SecurityConstants.java          # Constantes de seguridad
│
├── persistence/                            # Capa de persistencia
│   ├── entity/                             # Entidades JPA (20 clases)
│   │   ├── Usuario.java                    # Gestión de usuarios
│   │   ├── Rol.java / Permiso.java        # RBAC
│   │   ├── UsuarioRol.java / RolPermiso.java  # Tablas de unión
│   │   ├── Bodega.java                     # Almacenes
│   │   ├── Inventario.java                 # Stock
│   │   ├── MovimientoInventario.java       # Trazabilidad
│   │   ├── TipoMaterial.java              # Tipos de plástico
│   │   ├── Proveedor.java                  # Proveedores
│   │   ├── Transaccion.java               # Transacciones
│   │   ├── DetalleTransaccion.java        # Líneas de transacción
│   │   ├── Lote.java                       # Lotes de procesamiento
│   │   ├── EtapaProcesamiento.java        # Etapas de procesamiento
│   │   ├── Auditoria.java                  # Auditoría completa
│   │   ├── Alerta.java                     # Sistema de alertas
│   │   ├── ConfiguracionSistema.java      # Config dinámica
│   │   └── MonitoreoTamaño.java           # Monitoreo de BD
│   │
│   ├── enums/                              # Enumeraciones de dominio (18)
│   │   ├── EstadoUsuario.java             # ACTIVO, INACTIVO, SUSPENDIDO
│   │   ├── EstadoBodega.java
│   │   ├── EstadoTransaccion.java         # PENDIENTE, COMPLETADA, CANCELADA
│   │   ├── EstadoProveedor.java
│   │   ├── EstadoLote.java
│   │   ├── EstadoAlerta.java
│   │   ├── CalidadMaterial.java
│   │   ├── CategoriaMaterial.java
│   │   ├── AccionAuditoria.java           # CREATE, UPDATE, DELETE, READ
│   │   ├── MetodoPago.java
│   │   ├── SeveridadAlerta.java
│   │   ├── TipoAlerta.java
│   │   ├── TipoDatoConfiguracion.java
│   │   ├── TipoDocumento.java
│   │   ├── TipoEtapa.java
│   │   ├── TipoOperacion.java
│   │   ├── TipoOrganizacion.java
│   │   └── UnidadMedida.java
│   │
│   ├── converter/                          # Convertidores JPA (18)
│   │   ├── EstadoUsuarioConverter.java
│   │   ├── EstadoBodegaConverter.java
│   │   ├── AccionAuditoriaConverter.java
│   │   └── ... (uno por cada enum)
│   │
│   ├── repository/                         # Repositorios Spring Data (18)
│   │   ├── UsuarioRepository.java
│   │   ├── BodegaRepository.java
│   │   ├── InventarioRepository.java
│   │   ├── TransaccionRepository.java
│   │   ├── ProveedorRepository.java
│   │   └── ... (extienden JpaRepository)
│   │
│   └── advice/                             # Manejo global de excepciones
│       └── GlobalExceptionHandler.java     # @ControllerAdvice
│
├── service/                                # Capa de lógica de negocio
│   ├── interfaces/                         # Contratos de servicio (6)
│   │   ├── UsuarioService.java
│   │   ├── AuthService.java
│   │   ├── BodegaService.java
│   │   ├── InventarioService.java
│   │   ├── ProveedorService.java
│   │   └── TransaccionService.java
│   │
│   ├── implementation/                     # Implementaciones (6)
│   │   ├── UsuarioServiceImpl.java        # @Service
│   │   ├── AuthServiceImpl.java           # Generación JWT
│   │   ├── BodegaServiceImpl.java         # Validación capacidad
│   │   ├── InventarioServiceImpl.java     # Actualización stock
│   │   ├── ProveedorServiceImpl.java      # Validación estado
│   │   └── TransaccionServiceImpl.java    # Máquina de estados
│   │
│   ├── exception/                          # Excepciones personalizadas (3)
│   │   ├── BodegaCapacityExceededException.java
│   │   ├── ProveedorInactivoException.java
│   │   └── TransaccionInvalidaException.java
│   │
│   └── http/                               # Servicios HTTP externos
│
├── presentation/                           # Capa de presentación
│   ├── controller/                         # Controladores REST (7)
│   │   ├── AuthController.java            # /api/auth/**
│   │   ├── UsuarioController.java         # /api/usuarios/**
│   │   ├── BodegaController.java          # /api/bodegas/**
│   │   ├── ProveedorController.java       # /api/proveedores/**
│   │   ├── InventarioController.java      # /api/inventarios/**
│   │   ├── TransaccionController.java     # /api/transacciones/**
│   │   └── ReportController.java          # /api/reportes/**
│   │
│   └── dto/                                # Data Transfer Objects (10)
│       ├── AuthRequestDTO.java            # Login credentials
│       ├── AuthResponseDTO.java           # JWT token response
│       ├── UsuarioDTO.java
│       ├── BodegaDTO.java
│       ├── InventarioDTO.java
│       ├── ProveedorDTO.java
│       ├── TransaccionDTO.java
│       ├── DetalleTransaccionDTO.java
│       ├── MovimientoInventarioDTO.java
│       └── ReporteDTO.java
│
├── api/                                    # Configuración de API
│   ├── constants/                          # Constantes de API
│   ├── documentation/                      # Documentación Swagger/OpenAPI
│   ├── request/                            # Objetos de request personalizados
│   └── response/                           # Wrappers de response
│
└── util/                                   # Utilidades
    └── mapper/                             # Mapeo Entity ↔ DTO
```

**Total:** 113 archivos Java organizados en 8 paquetes principales.

---

## Tecnologías y Uso Detallado

### 1. Spring Boot 3.5.4

**Uso Principal:** Framework base que proporciona:
- Auto-configuración de componentes
- Servidor embebido (Tomcat)
- Gestión de dependencias
- Perfiles de configuración

**Configuración:**
```properties
# src/main/resources/application.properties
spring.application.name=sicofark-backend
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```

**Anotaciones Clave:**
- `@SpringBootApplication` - Marca la clase principal
- `@RestController` - Controladores REST
- `@Service` - Servicios de negocio
- `@Repository` - Repositorios de datos
- `@Configuration` - Clases de configuración

---

### 2. Spring Data JPA + Hibernate

**Uso:** Capa de persistencia con mapeo objeto-relacional (ORM).

#### 2.1 Entidades JPA

**Ejemplo: Usuario.java**
```java
@Getter @Setter                              // Lombok: getters/setters
@Entity                                      // Marca como entidad JPA
@Table(name = "usuario", schema = "sicofar") // Mapeo a tabla PostgreSQL
public class Usuario {

    @Id                                      // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Convert(converter = EstadoUsuarioConverter.class)  // Enum → String
    private EstadoUsuario estado;

    @ColumnDefault("0")
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos;        // Seguridad: tracking de login

    @Column(name = "bloqueado_hasta")
    private Instant bloqueadoHasta;          // Bloqueo temporal de cuenta

    @ManyToOne(fetch = FetchType.LAZY)       // Carga perezosa
    @OnDelete(action = OnDeleteAction.SET_NULL)  // Cascade SET_NULL
    @JoinColumn(name = "bodega_id")
    private Bodega bodega;

    // Campos de auditoría
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Column(name = "usuario_creacion", length = 50)
    private String usuarioCreacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;
}
```

**Características:**
- Mapeo bidireccional con relaciones `@ManyToOne`, `@OneToMany`, `@ManyToMany`
- `FetchType.LAZY` por defecto para optimización
- Estrategias de cascada personalizadas (`CASCADE`, `SET_NULL`, `RESTRICT`)
- `BigDecimal` para valores monetarios/pesos (precisión financiera)
- Campos de auditoría en todas las entidades críticas

#### 2.2 Claves Primarias Compuestas

**Ejemplo: UsuarioRol.java (tabla de unión)**
```java
@Embeddable  // Clave compuesta embebible
@Getter @Setter
public class UsuarioRolId implements Serializable {
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    @Override
    public boolean equals(Object o) { /* ... */ }

    @Override
    public int hashCode() { /* ... */ }
}

@Entity
@Table(name = "usuario_rol", schema = "sicofar")
public class UsuarioRol {
    @EmbeddedId  // Usa la clave compuesta
    private UsuarioRolId id;

    @MapsId("usuarioId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @MapsId("rolId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}
```

**Ventaja:** Integridad referencial garantizada a nivel de base de datos.

#### 2.3 Repositorios Spring Data

**Ejemplo: UsuarioRepository.java**
```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data genera implementación automáticamente

    Optional<Usuario> findByUsername(String username);  // Query por nombre

    List<Usuario> findByEstado(EstadoUsuario estado);   // Query por enum

    @Query("SELECT u FROM Usuario u JOIN FETCH u.bodega WHERE u.id = :id")
    Optional<Usuario> findByIdWithBodega(@Param("id") Long id);  // JOIN FETCH
}
```

**Métodos Automáticos:**
- `save(T entity)` - Inserta o actualiza
- `findById(ID id)` - Busca por ID
- `findAll()` - Obtiene todos
- `delete(T entity)` - Elimina
- `count()` - Cuenta registros

**Naming Convention:** Spring Data genera queries automáticas basadas en el nombre del método.

#### 2.4 Convertidores de Enums PostgreSQL

**Problema:** PostgreSQL usa enums nativos (tipos personalizados), JPA necesita convertirlos.

**Solución:** Convertidores personalizados con `@Converter(autoApply = true)`.

**Ejemplo: EstadoUsuarioConverter.java**
```java
@Converter(autoApply = true)  // Se aplica automáticamente a EstadoUsuario
public class EstadoUsuarioConverter
    implements AttributeConverter<EstadoUsuario, String> {

    @Override
    public String convertToDatabaseColumn(EstadoUsuario attribute) {
        if (attribute == null) return null;
        return attribute.name();  // ACTIVO → "ACTIVO"
    }

    @Override
    public EstadoUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return EstadoUsuario.valueOf(dbData);  // "ACTIVO" → ACTIVO
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Unknown estado_usuario value: " + dbData, e
            );
        }
    }
}
```

**Uso en Entidad:**
```java
@Convert(converter = EstadoUsuarioConverter.class)
@Column(name = "estado")
private EstadoUsuario estado;
```

**Total:** 18 convertidores implementados (uno por cada enum de PostgreSQL).

#### 2.5 Configuración Hibernate

```properties
# Dialecto PostgreSQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Schema por defecto
spring.jpa.properties.hibernate.default_schema=sicofar

# Validación de esquema (no genera tablas)
spring.jpa.hibernate.ddl-auto=update

# Mostrar SQL formateado
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Deshabilitar Open-in-View (evita lazy loading problems)
spring.jpa.open-in-view=false
```

**Implicación de `open-in-view=false`:**
- Las relaciones `LAZY` NO se cargan automáticamente fuera de transacciones
- Requiere uso explícito de `JOIN FETCH` en queries
- Mejor rendimiento y control sobre consultas SQL

---

### 3. PostgreSQL + JDBC Driver 42.7.3

**Uso:** Base de datos relacional principal.

**Configuración:**
```properties
spring.datasource.url=${DATABASE_URL}           # jdbc:postgresql://localhost:5432/sicofar_k
spring.datasource.username=${DATABASE_USERNAME}  # Usuario PostgreSQL
spring.datasource.password=${DATABASE_PASSWORD}  # Contraseña
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Características Específicas de PostgreSQL:**

1. **Schema Separation:**
   ```sql
   CREATE SCHEMA sicofar;
   ```
   Todas las tablas están en el schema `sicofar`, no en `public`.

2. **Enums Nativos:**
   ```sql
   CREATE TYPE estado_usuario AS ENUM ('ACTIVO', 'INACTIVO', 'SUSPENDIDO');
   ```

3. **Precisión Decimal:**
   ```java
   @Column(name = "capacidad_maxima", precision = 12, scale = 2)
   private BigDecimal capacidadMaxima;  // NUMERIC(12,2) en PostgreSQL
   ```

4. **Tipos de Datos:**
   - `Instant` → `TIMESTAMP WITH TIME ZONE`
   - `LocalDate` → `DATE`
   - `BigDecimal` → `NUMERIC(p, s)`
   - `Boolean` → `BOOLEAN`
   - `String` → `VARCHAR(n)` o `TEXT`

---

### 4. Spring Security + JJWT

**Uso:** Autenticación basada en JWT (JSON Web Tokens) y autorización RBAC.

#### 4.1 Configuración de Seguridad

**SecurityConfig.java**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para API REST
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // Login público
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")  // Solo admin
                .anyRequest().authenticated()  // Resto requiere autenticación
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Sin sesiones
            )
            .addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);  // Filtro JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Hashing de contraseñas
    }
}
```

#### 4.2 Generación y Validación de JWT

**JwtTokenProvider.java**
```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

#### 4.3 Filtro de Autenticación

**JwtAuthenticationFilter.java**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            String username = tokenProvider.getUsernameFromJWT(jwt);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remover "Bearer "
        }
        return null;
    }
}
```

#### 4.4 Carga de Usuario desde Base de Datos

**CustomUserDetailsServices.java**
```java
@Service
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado: " + username)
            );

        // Cargar roles y permisos
        Set<GrantedAuthority> authorities = usuario.getUsuarioRoles().stream()
            .map(ur -> ur.getRol())
            .flatMap(rol -> rol.getRolPermisos().stream())
            .map(rp -> new SimpleGrantedAuthority(rp.getPermiso().getNombre()))
            .collect(Collectors.toSet());

        return new User(
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getEstado() == EstadoUsuario.ACTIVO,  // enabled
            true,  // accountNonExpired
            true,  // credentialsNonExpired
            usuario.getBloqueadoHasta() == null ||
                usuario.getBloqueadoHasta().isBefore(Instant.now()),  // accountNonLocked
            authorities
        );
    }
}
```

#### 4.5 Modelo RBAC (Role-Based Access Control)

```
Usuario (N) ←→ UsuarioRol (join) ←→ Rol (N)
                                       ↓
                                   RolPermiso (join)
                                       ↓
                                   Permiso (N)
```

**Ejemplo de Flujo:**
1. Usuario "admin" tiene rol "ADMINISTRADOR"
2. Rol "ADMINISTRADOR" tiene permisos: ["USUARIOS_READ", "USUARIOS_WRITE", "BODEGAS_WRITE"]
3. Spring Security usa estos permisos para autorización

**Uso en Controladores:**
```java
@PreAuthorize("hasAuthority('USUARIOS_WRITE')")
@PostMapping
public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO dto) {
    // Solo usuarios con permiso USUARIOS_WRITE pueden acceder
}
```

---

### 5. Lombok

**Uso:** Reducción de código boilerplate mediante anotaciones.

**Anotaciones Principales:**

```java
@Getter @Setter  // Genera getters y setters
public class Usuario {
    private Long id;
    private String username;
}

// Equivalente a:
public class Usuario {
    private Long id;
    private String username;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
```

**Otras Anotaciones:**
- `@NoArgsConstructor` - Constructor sin argumentos (requerido por JPA)
- `@AllArgsConstructor` - Constructor con todos los campos
- `@Data` - Combina @Getter, @Setter, @ToString, @EqualsAndHashCode (NO usar con entidades JPA)
- `@Builder` - Patrón Builder

**Configuración Maven:**
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

**Ventaja:** Reduce ~60% del código en entidades y DTOs.

---

### 6. Maven

**Uso:** Gestión de dependencias y construcción del proyecto.

**pom.xml** (configuración principal):
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.4</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.3</version>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.5</version>
    </dependency>

    <!-- Development Tools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

**Comandos Principales:**
```bash
# Compilar el proyecto
./mvnw clean compile

# Construir JAR ejecutable
./mvnw clean package

# Ejecutar la aplicación
./mvnw spring-boot:run

# Ejecutar tests
./mvnw test

# Instalar en repositorio local
./mvnw clean install

# Saltar tests
./mvnw clean install -DskipTests
```

---

## Patrones de Diseño Implementados

### 1. Layered Architecture (Arquitectura en Capas)

**Separación estricta de responsabilidades:**
- **Presentation Layer:** Maneja HTTP requests/responses
- **Service Layer:** Contiene lógica de negocio
- **Persistence Layer:** Gestiona acceso a datos

**Regla:** Cada capa solo puede comunicarse con la capa inmediatamente inferior.

---

### 2. Repository Pattern

**Spring Data JPA implementa el patrón Repository:**
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Abstracción completa del acceso a datos
}
```

**Ventajas:**
- Desacoplamiento de lógica de negocio y persistencia
- Facilita testing con mocks
- Queries generadas automáticamente

---

### 3. DTO Pattern (Data Transfer Object)

**Separación entre entidades de dominio y objetos de API:**

```java
// Entidad (NO exponer directamente al cliente)
@Entity
public class Usuario {
    private String password;  // Campo sensible
    @ManyToOne
    private Bodega bodega;    // Relación compleja
}

// DTO (exponer al cliente)
@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    // NO incluye password ni relaciones complejas
}
```

**Conversión en Service Layer:**
```java
@Service
public class UsuarioServiceImpl implements UsuarioService {

    public UsuarioDTO getUsuario(Long id) {
        Usuario entity = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException());

        return mapToDTO(entity);  // Entity → DTO
    }

    private UsuarioDTO mapToDTO(Usuario entity) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        // ... mapeo de campos
        return dto;
    }
}
```

---

### 4. Dependency Injection (Inyección de Dependencias)

**Constructor Injection (recomendado):**
```java
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;

    // Spring inyecta automáticamente las dependencias
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              BodegaRepository bodegaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bodegaRepository = bodegaRepository;
    }
}
```

**Ventajas:**
- Inmutabilidad (campos `final`)
- Facilita testing
- Dependencias explícitas

---

### 5. Strategy Pattern (Convertidores de Enums)

**Cada enum tiene su propia estrategia de conversión:**
```java
@Converter(autoApply = true)
public class EstadoUsuarioConverter implements AttributeConverter<EstadoUsuario, String> {
    // Estrategia específica para EstadoUsuario
}

@Converter(autoApply = true)
public class EstadoBodegaConverter implements AttributeConverter<EstadoBodega, String> {
    // Estrategia específica para EstadoBodega
}
```

---

### 6. Composite Key Pattern

**Claves primarias compuestas para tablas de unión:**
```java
@Embeddable
public class UsuarioRolId implements Serializable {
    private Long usuarioId;
    private Long rolId;
}

@Entity
public class UsuarioRol {
    @EmbeddedId
    private UsuarioRolId id;
}
```

**Garantiza:** Unicidad de la combinación (usuarioId, rolId).

---

### 7. Lazy Loading Pattern

**Carga diferida de relaciones:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "bodega_id")
private Bodega bodega;  // No se carga hasta acceder a .getBodega()
```

**Requiere:** `open-in-view=false` para evitar N+1 queries.

**Solución:** JOIN FETCH explícito
```java
@Query("SELECT u FROM Usuario u JOIN FETCH u.bodega WHERE u.id = :id")
Optional<Usuario> findByIdWithBodega(@Param("id") Long id);
```

---

### 8. Cascade Delete Strategies

**Tres estrategias según el caso de uso:**

1. **CASCADE:** Padre posee hijos (eliminar en cascada)
   ```java
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<DetalleTransaccion> detalles;
   ```

2. **SET_NULL:** Referencia opcional (preservar datos)
   ```java
   @ManyToOne
   @OnDelete(action = OnDeleteAction.SET_NULL)
   private Bodega bodega;
   ```

3. **RESTRICT:** Por defecto (evitar eliminaciones accidentales)
   ```java
   @ManyToOne  // No cascade
   private Proveedor proveedor;
   ```

---

### 9. Audit Trail Pattern

**Tracking automático de cambios:**
```java
@Entity
public class Usuario {
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant fechaCreacion;

    private String usuarioCreacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant fechaActualizacion;

    private String usuarioActualizacion;
}
```

**Auditoría avanzada:**
```java
@Entity
public class Auditoria {
    private AccionAuditoria accion;         // CREATE, UPDATE, DELETE
    private String tablaAfectada;
    private String valoresAnteriores;       // JSON before
    private String valoresNuevos;           // JSON after
    private String hashIntegridad;          // SHA-256 para detección de tampering
    private String ipAddress;
    private String userAgent;
}
```

---

### 10. Self-Referential Hierarchy

**Jerarquías padre-hijo en misma tabla:**
```java
@Entity
public class Lote {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_padre_id")
    private Lote lotePadre;  // Auto-referencia

    @OneToMany(mappedBy = "lotePadre")
    private Set<Lote> lotesHijos;
}
```

**Caso de uso:** Descomposición de lotes (1 lote grande → N lotes pequeños).

---

### 11. Service Interface Pattern

**Contratos de servicio separados de implementaciones:**
```java
// Interfaz (contrato)
public interface UsuarioService {
    UsuarioDTO createUsuario(UsuarioDTO dto);
    UsuarioDTO getUsuario(Long id);
}

// Implementación
@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Override
    public UsuarioDTO createUsuario(UsuarioDTO dto) {
        // Lógica de negocio
    }
}
```

**Ventaja:** Facilita cambio de implementación sin afectar clientes.

---

### 12. Exception Handling Pattern

**Excepciones personalizadas + manejo global:**

```java
// Excepción personalizada
public class BodegaCapacityExceededException extends RuntimeException {
    public BodegaCapacityExceededException(String message) {
        super(message);
    }
}

// Manejo global
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BodegaCapacityExceededException.class)
    public ResponseEntity<ErrorResponse> handleCapacityExceeded(
        BodegaCapacityExceededException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

---

## Configuración y Ejecución

### Requisitos Previos
- Java 21
- PostgreSQL 15+
- Maven 3.8+

### Variables de Entorno

Configurar antes de ejecutar:
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/sicofar_k
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=tu_password
```

### Ejecutar la Aplicación

```bash
# Clonar repositorio
git clone <repository-url>
cd sicofark-backend

# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run
```

**Servidor:** http://localhost:8080

### Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Test específico
./mvnw test -Dtest=UsuarioServiceImplTest

# Con coverage
./mvnw clean test jacoco:report
```

### Empaquetar JAR

```bash
# Generar JAR ejecutable
./mvnw clean package

# Ejecutar JAR
java -jar target/sicofark-backend-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
# Construir imagen
docker build -t sicofark-backend:latest .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/sicofar_k \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=password \
  sicofark-backend:latest
```

---

## Resumen de Tecnologías

| Capa | Tecnología | Propósito |
|------|------------|-----------|
| **Framework** | Spring Boot 3.5.4 | Base de la aplicación |
| **Lenguaje** | Java 21 | Programación |
| **Persistencia** | Spring Data JPA + Hibernate 6 | ORM y acceso a datos |
| **Base de Datos** | PostgreSQL 15 | Almacenamiento relacional |
| **Seguridad** | Spring Security + JJWT 0.12.5 | Autenticación JWT + RBAC |
| **Productividad** | Lombok | Reducción de boilerplate |
| **Build** | Maven 3.8+ | Gestión de dependencias |
| **Servidor** | Tomcat embebido | Servidor de aplicaciones |
| **Testing** | Spring Boot Test + JUnit 5 | Pruebas unitarias/integración |

---

## Métricas del Proyecto

- **Total de archivos Java:** 113
- **Entidades JPA:** 20
- **Enumeraciones:** 18
- **Convertidores JPA:** 18
- **Repositorios:** 18
- **Servicios:** 6 (interfaces + implementaciones)
- **Controladores REST:** 7
- **DTOs:** 10
- **Excepciones personalizadas:** 3
- **Líneas de código (estimado):** ~8,000 LOC

---

## Licencia

MIT License - Ver archivo LICENSE para más detalles.

---

## Contacto

**Desarrollador:** Jose Navarro
**Email:** jnavarrop26@ucentral.edu.co
**Repositorio:** [GitHub URL]
