# Guía de Instalación - CrudPark Java

## 📋 Requisitos del Sistema

### Software Requerido

1. **Java Development Kit (JDK) 17 o superior**
   - Descargar desde: https://adoptium.net/
   - Verificar instalación:
     ```bash
     java -version
     javac -version
     ```

2. **Apache Maven 3.6 o superior**
   - Descargar desde: https://maven.apache.org/download.cgi
   - Verificar instalación:
     ```bash
     mvn -version
     ```

3. **PostgreSQL 12 o superior**
   - Descargar desde: https://www.postgresql.org/download/
   - Durante la instalación, recordar la contraseña del usuario `postgres`

4. **Git** (opcional, para clonar el repositorio)
   - Descargar desde: https://git-scm.com/downloads

### Herramientas Recomendadas

- **IDE**: IntelliJ IDEA, Eclipse, o VS Code con extensión Java
- **Gestor de BD**: DBeaver, TablePlus, pgAdmin, o HeidiSQL

## 🚀 Instalación Paso a Paso

### Paso 1: Obtener el Código

**Opción A: Clonar con Git**
```bash
git clone <url-del-repositorio>
cd crudpark-java
```

**Opción B: Descargar ZIP**
1. Descargar el archivo ZIP del repositorio
2. Extraer en la carpeta deseada
3. Abrir terminal en esa carpeta

### Paso 2: Configurar PostgreSQL

1. **Crear la base de datos**
   
   Abrir terminal de PostgreSQL (psql) o tu gestor de BD favorito:
   ```sql
   CREATE DATABASE crudpark;
   ```

2. **Ejecutar el esquema**
   
   Desde la terminal:
   ```bash
   psql -U postgres -d crudpark -f resources/schema.sql
   ```
   
   O desde tu gestor de BD:
   - Conectar a la base de datos `crudpark`
   - Abrir el archivo `resources/schema.sql`
   - Ejecutar todo el script

3. **Verificar las tablas**
   ```sql
   \dt
   -- Deberías ver: Operators, Vehicles, Rates, Subscriptions, Tickets, Payments, etc.
   ```

### Paso 3: Configurar la Aplicación

1. **Copiar el archivo de configuración**
   ```bash
   copy config.properties.example config.properties
   ```
   
   En Linux/Mac:
   ```bash
   cp config.properties.example config.properties
   ```

2. **Editar config.properties**
   
   Abrir el archivo con un editor de texto y configurar:
   ```properties
   # Configuración de Base de Datos PostgreSQL
   db.host=localhost
   db.port=5432
   db.name=crudpark
   db.user=postgres
   db.password=TU_CONTRASEÑA_AQUI
   
   # Configuración de la Aplicación
   app.name=CrudPark - Crudzaso
   app.grace_period_minutes=30
   ```
   
   **Importante:** Reemplazar `TU_CONTRASEÑA_AQUI` con la contraseña real de PostgreSQL.

### Paso 4: Compilar el Proyecto

1. **Descargar dependencias y compilar**
   ```bash
   mvn clean install
   ```
   
   Este comando:
   - Descarga todas las dependencias de Maven
   - Compila el código fuente
   - Ejecuta las validaciones
   - Genera el archivo JAR

2. **Verificar la compilación**
   
   Si todo está correcto, verás:
   ```
   [INFO] BUILD SUCCESS
   ```

### Paso 5: Ejecutar la Aplicación

**Opción A: Desde Maven**
```bash
mvn exec:java -Dexec.mainClass="com.crudzaso.crudpark.Main"
```

**Opción B: Desde el JAR**
```bash
java -jar target/crudpark-1.0.0.jar
```

**Opción C: Desde tu IDE**
- Abrir el proyecto en tu IDE
- Ejecutar la clase `Main.java`

### Paso 6: Primer Inicio de Sesión

1. La aplicación mostrará la ventana de login
2. Ingresar el documento de un operador existente
3. Si ejecutaste el esquema SQL, hay operadores de prueba

**Para ver los operadores disponibles:**
```sql
SELECT "Document", "FullName", "Email" 
FROM public."Operators" 
WHERE "IsActive" = true;
```

## 🔧 Configuración Avanzada

### Cambiar el Puerto de PostgreSQL

Si PostgreSQL usa un puerto diferente al 5432:
```properties
db.port=5433
```

### Usar una Base de Datos Remota

```properties
db.host=192.168.1.100
db.port=5432
db.name=crudpark
db.user=usuario_remoto
db.password=contraseña_remota
```

### Configurar Tiempo de Gracia

Para cambiar el tiempo de gracia por defecto:
```properties
app.grace_period_minutes=45
```

**Nota:** Esto es solo para referencia. El tiempo de gracia real se toma de la tabla `Rates`.

## 📦 Generar Ejecutable para Distribución

### Crear JAR con Dependencias

```bash
mvn clean package
```

El archivo se generará en: `target/crudpark-1.0.0.jar`

### Distribuir la Aplicación

Para distribuir a otros usuarios:

1. **Copiar estos archivos:**
   - `crudpark-1.0.0.jar`
   - `config.properties.example` (renombrar a `config.properties`)

2. **Crear carpeta de distribución:**
   ```
   crudpark-java-dist/
   ├── crudpark-1.0.0.jar
   ├── config.properties
   └── README.txt
   ```

3. **Instrucciones para el usuario final:**
   - Instalar Java 17 o superior
   - Configurar `config.properties` con sus credenciales
   - Ejecutar: `java -jar crudpark-1.0.0.jar`

## 🐛 Solución de Problemas Comunes

### Error: "JAVA_HOME not set"

**Windows:**
```bash
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
```

**Linux/Mac:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Error: "mvn: command not found"

Maven no está en el PATH. Agregar Maven al PATH del sistema.

**Windows:**
1. Panel de Control → Sistema → Configuración Avanzada
2. Variables de Entorno
3. Agregar `C:\apache-maven-3.x.x\bin` al PATH

### Error: "Connection refused"

PostgreSQL no está ejecutándose.

**Windows:**
```bash
# Verificar servicio
services.msc
# Buscar "postgresql" e iniciarlo
```

**Linux:**
```bash
sudo systemctl start postgresql
sudo systemctl status postgresql
```

**Mac:**
```bash
brew services start postgresql
```

### Error: "password authentication failed"

Credenciales incorrectas en `config.properties`.

**Solución:**
1. Verificar usuario y contraseña de PostgreSQL
2. Actualizar `config.properties`
3. Reiniciar la aplicación

### Error: "database crudpark does not exist"

La base de datos no fue creada.

**Solución:**
```bash
psql -U postgres
CREATE DATABASE crudpark;
\q
```

### Error al compilar: "package does not exist"

Dependencias no descargadas.

**Solución:**
```bash
mvn clean install -U
```

El flag `-U` fuerza la actualización de dependencias.

## ✅ Verificación de la Instalación

### Checklist de Instalación

- [ ] Java 17+ instalado y en PATH
- [ ] Maven 3.6+ instalado y en PATH
- [ ] PostgreSQL ejecutándose
- [ ] Base de datos `crudpark` creada
- [ ] Esquema SQL ejecutado correctamente
- [ ] Archivo `config.properties` configurado
- [ ] Proyecto compilado sin errores
- [ ] Aplicación inicia correctamente
- [ ] Login funciona con operador de prueba

### Comandos de Verificación

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar PostgreSQL
psql -U postgres -c "SELECT version();"

# Verificar base de datos
psql -U postgres -d crudpark -c "\dt"

# Compilar proyecto
mvn clean compile

# Ejecutar aplicación
mvn exec:java -Dexec.mainClass="com.crudzaso.crudpark.Main"
```

## 📚 Recursos Adicionales

- [Documentación de Java](https://docs.oracle.com/en/java/)
- [Documentación de Maven](https://maven.apache.org/guides/)
- [Documentación de PostgreSQL](https://www.postgresql.org/docs/)
- [Tutorial de JDBC](https://docs.oracle.com/javase/tutorial/jdbc/)

## 🆘 Obtener Ayuda

Si encuentras problemas durante la instalación:

1. Revisar esta guía completa
2. Verificar los logs de error
3. Consultar la sección de Solución de Problemas en README.md
4. Contactar al equipo de desarrollo

---

**¡Instalación completada! Ahora puedes usar CrudPark Java.**
