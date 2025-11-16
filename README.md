📌 Banco de Agricultura - Sistema de Gestión Bancaria

📖 Descripción

Este proyecto es un sistema web de gestión bancaria desarrollado para el Banco de Agricultura Salvadoreño S.A. de C.V.. Su objetivo es modernizar y optimizar la administración interna del banco, permitiendo a distintos roles (clientes, empleados, cajeros y gerentes) realizar operaciones de manera eficiente y segura.

🚀 Tecnologías Utilizadas

🟢 Java con Spring MVC para la gestión de la lógica de negocio y controladores.

🎨 JSF (JavaServer Faces) para el desarrollo de interfaces de usuario dinámicas.

💾 Hibernate para la gestión de la persistencia de datos mediante ORM.

🛢 MySQL como base de datos para el almacenamiento de la información.

🖥 Tomcat Server 8.5.96 para la ejecución del sistema.

🔗 JDBC con consultas parametrizadas para interacción segura con la base de datos.

🏆 Funcionalidades Principales

✔ Gestión de Cuentas Bancarias: Creación, consulta, depósitos, retiros y transferencias.

✔ Gestión de Usuarios: Administración de clientes, cajeros y gerentes.

✔ Administración de Créditos: Solicitud y aprobación de préstamos.

✔ Seguridad y Roles de Usuario: Acceso basado en permisos según el rol asignado.

✔ Interfaz Web Amigable: Uso de JSF para formularios y validaciones.

⚙️ Instalación

git clone https://github.com/tu-usuario/DWF2.git
# Edita src/main/resources/application.properties con tu DB
mvn clean install
mvn spring-boot:run

