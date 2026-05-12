Proyecto Código Secreto (JavaFX)

Este es un proyecto desarrollado en Java con JavaFX, enfocado en la creación de una aplicación de escritorio con interfaz gráfica utilizando FXML.

Requisitos del sistema

Antes de ejecutar el proyecto, asegúrate de tener instalado:

JDK 17 o JDK 21 (recomendado)
JavaFX SDK compatible
Eclipse IDE o IntelliJ IDEA
Configuración correcta de JavaFX en el entorno
Base de datos correspondiente al proyecto (ver sección de instalación)
Instalación del proyecto
Clona el repositorio:
git clone https://github.com/JeissonRomero09/Proyecto_CodigoSecreto.git
Cambia a la rama donde se encuentra la base de datos:
git checkout Info
Importa el proyecto en Eclipse:
File → Import → Existing Projects into Workspace
Selecciona la carpeta del proyecto.
Base de datos

Este proyecto requiere una base de datos para su correcto funcionamiento.

La base de datos se encuentra en la rama: Info
Debes descargar o revisar los archivos incluidos en dicha rama
Importa la base de datos en tu gestor (por ejemplo MySQL o el que use el proyecto)
Configura las credenciales y conexión en el archivo correspondiente del proyecto (clase de conexión)

Sin la base de datos, algunas funcionalidades del sistema no funcionarán correctamente.

Ejecución del proyecto
Opción 1: Desde Eclipse
Ubica la clase principal (por ejemplo: Main.java o App.java)
Haz clic derecho sobre la clase
Selecciona:
Run As → Java Application
Configuración importante (JavaFX)

Si el proyecto no ejecuta correctamente, agrega en:

Run Configurations → VM arguments
--module-path "C:\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml

Ajusta la ruta según donde tengas instalado JavaFX.

Estructura del proyecto
src/ → Código fuente Java
Vista/ → Archivos FXML (interfaces gráficas)
Controlador/ → Lógica de la aplicación
Modelo/ → Clases de datos
resources/ → Imágenes y estilos
database/ → Archivos de base de datos (rama Info)
Ejecución en celular

Este proyecto no se puede ejecutar directamente en dispositivos Android.

Motivos:

JavaFX es una tecnología de escritorio
Requiere JVM completa
No es compatible con Android de forma nativa
Recomendación

Para ejecutar el proyecto correctamente utiliza:

PC con Eclipse o IntelliJ IDEA
Máquina virtual si no tienes PC disponible
Migración futura a aplicación web o Android si se desea compatibilidad móvil
Autor

Jeisson Romero
Camilo Vera
