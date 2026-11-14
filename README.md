pokemon
=============

Proyecto JavaFX modular (Java 21) con estructura Modelo-Vista-Controlador.
Paquete base: `com.uabc.poo2`

Requisitos
----------

- JDK 21 o superior, con `JAVA_HOME` configurado
- No hace falta instalar Maven: el proyecto incluye Maven Wrapper (`mvnw`)

Verifica con:

```
java -version
```

Comandos basicos
----------------

En Linux/macOS usa `./mvnw`; en Windows usa `mvnw.cmd`.

| Accion | Linux / macOS | Windows |
|---|---|---|
| Ejecutar la app | `./mvnw javafx:run` | `mvnw.cmd javafx:run` |
| Compilar | `./mvnw compile` | `mvnw.cmd compile` |
| Limpiar y compilar | `./mvnw clean compile` | `mvnw.cmd clean compile` |
| Limpiar `target/` | `./mvnw clean` | `mvnw.cmd clean` |
| Generar el paquete nativo | `./mvnw clean package -Ppaquete` | `mvnw.cmd clean package -Ppaquete` |

Maven descarga automaticamente la version de JavaFX que corresponde a tu sistema operativo.
La primera ejecucion necesita internet (descarga Maven y las dependencias).

Empaquetar
----------

`./mvnw clean package -Ppaquete` genera la aplicacion en `target/dist/`.
El resultado solo funciona en el sistema operativo donde se genera: para tener
la version de Windows hay que ejecutar el comando en Windows.

Estructura
----------

- `modelo/` - clases de datos y logica de negocio
- `controlador/` - controladores asociados a los FXML
- `src/main/resources/.../vista/` - archivos FXML
- `module-info.java` - descriptor del modulo

Git
------------------------

```
git clone <url-del-repositorio>
git pull
git add .
git commit -m "mensaje"
git push
```

Antes de empezar a trabajar haz `git pull`, y no subas la carpeta `target/`
(ya esta en `.gitignore`).
