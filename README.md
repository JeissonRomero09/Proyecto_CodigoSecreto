Proyecto Código Secreto (JavaFX)

Este es un proyecto desarrollado en Java con JavaFX, enfocado en la creación de una aplicación de escritorio con interfaz gráfica utilizando FXML.

Requisitos del sistema

Antes de ejecutar el proyecto, asegúrate de tener instalado:

JDK 17 o JDK 21 (recomendado)
JavaFX SDK compatible
Eclipse IDE o IntelliJ IDEA
Configuración correcta de JavaFX en el entorno
Instalación del proyecto
Clona el repositorio:
git clone https://github.com/JeissonRomero09/Proyecto_CodigoSecreto.git
Abre el proyecto en Eclipse:
File → Import → Existing Projects into Workspace
Selecciona la carpeta del proyecto.
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
