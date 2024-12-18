Proyecto: Central de Autobuses
Este proyecto se ha desarrollado con el propósito de crear una aplicación integral para la gestión de una central de autobuses. La aplicación está diseñada con múltiples clases, cada una de ellas desempeñando roles específicos para garantizar un funcionamiento eficiente y organizado. A continuación, se presenta una descripción detallada de cada clase y su propósito:

Clase Menú:

Descripción: Esta clase es responsable de la gestión de la interfaz de inicio de sesión.

Función:

Autenticación: Permite la entrada de credenciales y verifica la autenticidad del usuario.

Roles de Usuario: Dependiendo de las credenciales ingresadas, determina si el usuario es un administrador o un vendedor.

![Imagen de WhatsApp 2024-12-16 a las 08 36 01_b543d0ce](https://github.com/user-attachments/assets/adb7c130-c442-4ae7-bf25-7dcfc1eae8c7)


Redirección: Dirige al usuario a la interfaz correspondiente, ya sea el menú principal del administrador o del vendedor.

Seguridad: Implementa medidas de seguridad como el cifrado de contraseñas para proteger los datos de los usuarios.

Clase Menú Principal:

Descripción: Esta clase maneja la interfaz principal a la que los usuarios tienen acceso después de iniciar sesión.

Función:

Interfaz de Administrador: Si el usuario es un administrador, tiene acceso completo a todas las funcionalidades del sistema, incluyendo la gestión de boletos, camiones, conductores, viajes y más.

![Imagen de WhatsApp 2024-12-16 a las 08 36 56_ed484834](https://github.com/user-attachments/assets/b96425d7-cbae-4532-9ff2-305c1b9a8326)


Interfaz de Vendedor: Si el usuario es un vendedor, su acceso se limita a la gestión de boletos, permitiéndole crear, modificar y emitir boletos para los pasajeros.

![Imagen de WhatsApp 2024-12-16 a las 08 37 48_be5b7837](https://github.com/user-attachments/assets/0ac87959-5d35-42ad-8d99-75c9c6ff519e)


Personalización: Muestra diferentes opciones y menús dependiendo del rol del usuario, asegurando una experiencia de usuario adaptada.

Clase Boleto:

Descripción: Esta clase se encarga de la gestión y almacenamiento de la información de los boletos.

![Imagen de WhatsApp 2024-12-16 a las 08 38 51_2f2d3fa4](https://github.com/user-attachments/assets/c0e85687-449a-454f-8a1c-fcbdf0368502)

Descripción:Selecciona el asiento

![Imagen de WhatsApp 2024-12-16 a las 08 48 08_f4f14d24](https://github.com/user-attachments/assets/a0fe7e6a-e94f-46de-a486-c37defd61554)


Función:

Datos del Pasajero: Registra información detallada de los pasajeros, como nombre, dirección, número de identificación, y más.

Emisión de Boletos: Genera y asigna boletos a los pasajeros, incluyendo detalles como fecha de viaje, origen, destino y hora de salida.

Generación de PDF: Al momento de pagar, crea un documento PDF con toda la información del boleto.

Envío de Correos: Envía el PDF generado al correo electrónico del usuario, asegurando que el pasajero reciba una copia de su boleto.

Clase Camión:

Descripción: Gestiona la información de los diferentes tipos de camiones disponibles en la central de autobuses.

![Imagen de WhatsApp 2024-12-16 a las 08 39 44_761c81c6](https://github.com/user-attachments/assets/9a6bdbf3-9e0c-443e-ba55-39a1b33d2d9c)


Función:

Registro de Camiones: Permite el ingreso y actualización de datos de los camiones, tales como modelo, capacidad, estado de mantenimiento, y más.

Disponibilidad: Monitorea la disponibilidad de los camiones para asignarlos a viajes.

Mantenimiento: Registra y gestiona las fechas de mantenimiento para asegurar que los camiones estén en condiciones óptimas para operar.

Clase Conductor:

Descripción: Se encarga de la gestión de la información de los conductores de los camiones.

![Imagen de WhatsApp 2024-12-16 a las 08 40 28_512c9a3d](https://github.com/user-attachments/assets/ba1a6d09-d77c-454e-a141-ba12b85a3c4c)

Descripción: Al ingresar su fecha de nacimiento apareje un JCalender

![Imagen de WhatsApp 2024-12-16 a las 08 49 19_33f792bc](https://github.com/user-attachments/assets/cc10d62e-53dd-4b2a-8820-c6c4fe5f6e73)



Función:

Datos del Conductor: Almacena información personal y profesional de los conductores, como nombres, licencias, experiencia y disponibilidad.

Asignación de Conductores: Asigna conductores a los viajes programados, asegurando que cada viaje tenga un conductor adecuado.

Historial de Viajes: Mantiene un registro de los viajes realizados por cada conductor, lo cual es útil para evaluaciones de desempeño y planificación.

Clase Viajes:

Descripción: Planifica y gestiona los viajes que realiza la central de autobuses.

![Imagen de WhatsApp 2024-12-16 a las 08 41 22_8d9254b2](https://github.com/user-attachments/assets/d4f7fd1d-fc6e-41aa-8b80-8b9609f99205)


Función:

Creación de Viajes: Permite la creación y planificación de nuevos viajes, especificando detalles como destino, fecha, hora de salida y duración.

Asignación de Recursos: Asigna tanto el conductor como el camión para cada viaje, asegurando que los recursos estén bien distribuidos.

Gestión de Pasajeros: Administra la lista de pasajeros para cada viaje, incluyendo la verificación de boletos y asignación de asientos.

Clase Envío Correo:

Descripción: Maneja la funcionalidad de envío de correos electrónicos.

![Imagen de WhatsApp 2024-12-16 a las 08 43 41_7ac995a9](https://github.com/user-attachments/assets/401f8fcf-5511-4db1-a524-83aabdf2535c)


Función:

Configuración de Conexión: Configura y establece la conexión necesaria para el envío de correos electrónicos.

Envío de Documentos: Envía los PDF generados de los boletos a los correos electrónicos de los usuarios.

Notificaciones: Puede ser utilizada para enviar notificaciones importantes a los usuarios, tales como cambios en el itinerario de viaje o avisos de mantenimiento.

Clase Conectar:

Descripción: Gestiona la conexión con la base de datos.

Función:

Conexión: Establece y mantiene la conexión con la base de datos.


![Imagen de WhatsApp 2024-12-16 a las 08 46 16_b6ca6831](https://github.com/user-attachments/assets/f7050f47-7a37-459e-9a53-01292cb9556e)


Consulta de Datos: Permite realizar consultas a la base de datos para recuperar y manipular la información almacenada.

Actualización: Facilita la actualización de datos en la base de datos, asegurando que toda la información esté siempre actualizada.

Este proyecto de central de autobuses está cuidadosamente diseñado para proporcionar una gestión eficiente y eficaz de todas las operaciones relacionadas con la administración de una central de autobuses. Cada clase desempeña un papel crucial y su interacción asegura que tanto los administradores como los vendedores puedan realizar sus tareas de manera organizada y sin contratiempos. La implementación de características como la generación de boletos en PDF, el envío de correos electrónicos y la gestión de datos asegura una experiencia de usuario completa y satisfactoria.

Además, el sistema está diseñado para ser escalable y adaptable, lo que permite futuras expansiones y mejoras. Por ejemplo, se podrían añadir nuevas funcionalidades como la gestión de reservas en línea, seguimiento en tiempo real de los camiones, integración con sistemas de pago, y más. La arquitectura del software está pensada para facilitar estas ampliaciones sin comprometer el rendimiento o la estabilidad del sistema.

Este proyecto no solo aborda las necesidades actuales de una central de autobuses, sino que también se anticipa a futuras exigencias, proporcionando una solución robusta y flexible que puede evolucionar con el tiempo. Así, se garantiza que la central de autobuses pueda ofrecer un servicio de alta calidad a sus usuarios mientras se adapta a los cambios y demandas del mercado.


https://github.com/user-attachments/assets/1cfefeb9-6427-4a9e-a815-6a7ab5f1df6b

