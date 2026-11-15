pokemon
=============

Proyecto JavaFX modular (Java 21) con estructura Modelo-Vista-Controlador.
Paquete base: `com.uabc.poo2`

Requisitos
----------

- JDK 25 o superior, con `JAVA_HOME` configurado

Verifica con:

```
java -version
```

Comandos basicos
----------------


| Accion | Linux / macOS | Windows |
|---|---|---|
| Ejecutar la app | `./mvnw javafx:run` | `mvnw.cmd javafx:run` |
| Compilar | `./mvnw compile` | `mvnw.cmd compile` |
| Limpiar y compilar | `./mvnw clean compile` | `mvnw.cmd clean compile` |
| Limpiar `target/` | `./mvnw clean` | `mvnw.cmd clean` |
| Generar el paquete nativo | `./mvnw clean package -Ppaquete` | `mvnw.cmd clean package -Ppaquete` |


Empaquetar
----------

`./mvnw clean package -Ppaquete` genera la aplicacion en `target/dist/`.

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

Antes de empezar a trabajar haz `git pull`.
