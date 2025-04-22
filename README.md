# Taller Java 2025

## Grupo 3 - UTEC: Universidad Tegnologica
- Sede: San Jose
- Estudiantes:
    - Fabricio Fuentes
    - Alex Zimmer
    - Pablo De Leon
***  
## Epata 1:
#### Diagramas y diseño
> $\color{green}{\text{Completo}}$
***
## Etapa 2:
#### Implementacion de proyecto
> Tareas:
>> **Fabricio:** Usuarios, Pagos y apis.  
>> **Alex:** Systema, Interfaces, comunicacion de clases.  
>> **Pablo:** Content, articulos, Testing.

*Favor de crear sus respectivas clases en*  
**src/main/java/com/Tisj/bussines/entities**

Apis usadas
MaiApi creacion propia de Fabricio
Api Paypal
Api MercadoPago
Api Youtube (Para los videos y su duracion)
Api  Supabase.com (para almacenamiento en la nube de base de datos)

Base de datos
Postgres
Nombre
	Solariana

Frontend
	React.js
	PrimeReact
	
Backend
	Java
	Springboot

Testing
	Swagger

### Plan Básico de Endpoints

**Usuario-Controller (`/api/usuarios`)**
- `POST /api/usuarios` - Crear un nuevo usuario.  
  Resultado:
- `GET /api/usuarios` - Obtener una lista de todos los usuarios (puede requerir permisos de admin).  
  Resultado:
- `GET /api/usuarios/{id}` - Obtener detalles de un usuario específico por ID.  
  Resultado:
- `PUT /api/usuarios/{id}` - Actualizar la información de un usuario existente.  
  Resultado:
- `DELETE /api/usuarios/{id}` - Eliminar un usuario (puede ser borrado lógico).  
  Resultado:

**Video-Controller (`/api/videos`)**
- `POST /api/videos` - Subir/registrar un nuevo video.  
  Resultado:
- `GET /api/videos` - Obtener una lista de todos los videos.  
  Resultado:
- `GET /api/videos/{id}` - Obtener detalles de un video específico por ID.  
  Resultado:
- `PUT /api/videos/{id}` - Actualizar la información de un video existente.  
  Resultado:
- `DELETE /api/videos/{id}` - Eliminar un video.  
  Resultado:

**Paquete-Controller (`/api/paquetes`)**
- `POST /api/paquetes` - Crear un nuevo paquete (de cursos, artículos, etc.).  
  Resultado:
- `GET /api/paquetes` - Obtener una lista de todos los paquetes.  
  Resultado:
- `GET /api/paquetes/{id}` - Obtener detalles de un paquete específico por ID.  
  Resultado:
- `PUT /api/paquetes/{id}` - Actualizar la información de un paquete existente.  
  Resultado:
- `DELETE /api/paquetes/{id}` - Eliminar un paquete.  
  Resultado:
- `POST /api/paquetes/{paqueteId}/cursos/{cursoId}` - Asociar un curso a un paquete.  
  Resultado:
- `DELETE /api/paquetes/{paqueteId}/cursos/{cursoId}` - Desasociar un curso de un paquete.  
  Resultado:

**Pago-Controller (`/api/pagos`)**
- `POST /api/pagos` - Iniciar un proceso de pago (puede devolver URL de redirección a Paypal/MercadoPago).  
  Resultado:
- `GET /api/pagos` - Obtener historial de pagos (probablemente filtrado por usuario o admin).  
  Resultado:
- `GET /api/pagos/{id}` - Obtener detalles de un pago específico.  
  Resultado:
- `POST /api/pagos/paypal/webhook` - Endpoint para recibir notificaciones de estado de Paypal.  
  Resultado:
- `POST /api/pagos/mercadopago/webhook` - Endpoint para recibir notificaciones de estado de MercadoPago.  
  Resultado:
- `GET /api/usuarios/{userId}/pagos` - Obtener los pagos de un usuario específico.  
  Resultado:

**Oferta-Controller (`/api/ofertas`)**
- `POST /api/ofertas` - Crear una nueva oferta.  
  Resultado:
- `GET /api/ofertas` - Obtener una lista de todas las ofertas activas.  
  Resultado:
- `GET /api/ofertas/{id}` - Obtener detalles de una oferta específica por ID.  
  Resultado:
- `PUT /api/ofertas/{id}` - Actualizar una oferta existente.  
  Resultado:
- `DELETE /api/ofertas/{id}` - Eliminar/desactivar una oferta.  
  Resultado:

**Curso-Controller (`/api/cursos`)**
- `POST /api/cursos` - Crear un nuevo curso.  
  Resultado:
- `GET /api/cursos` - Obtener una lista de todos los cursos.  
  Resultado:
- `GET /api/cursos/{id}` - Obtener detalles de un curso específico por ID.  
  Resultado:
- `PUT /api/cursos/{id}` - Actualizar la información de un curso existente.  
  Resultado:
- `DELETE /api/cursos/{id}` - Eliminar un curso.  
  Resultado:
- `GET /api/cursos/{cursoId}/videos` - Obtener los videos asociados a un curso.  
  Resultado:
- `POST /api/cursos/{cursoId}/videos/{videoId}` - Asociar un video a un curso.  
  Resultado:

**Articulo-Controller (`/api/articulos`)**
- `POST /api/articulos` - Crear un nuevo artículo.  
  Resultado:
- `GET /api/articulos` - Obtener una lista de todos los artículos.  
  Resultado:
- `GET /api/articulos/{id}` - Obtener detalles de un artículo específico por ID.  
  Resultado:
- `PUT /api/articulos/{id}` - Actualizar un artículo existente.  
  Resultado:
- `DELETE /api/articulos/{id}` - Eliminar un artículo.  
  Resultado:

**Articulo-Cliente-Controller (`/api/usuarios/{userId}/articulos`)**
- `GET /api/usuarios/{userId}/articulos` - Obtener los artículos asociados/comprados por un cliente/usuario.  
  Resultado:
- `POST /api/usuarios/{userId}/articulos/{articuloId}` - Asociar un artículo a un cliente (ej. después de una compra o suscripción).  
  Resultado:
- `DELETE /api/usuarios/{userId}/articulos/{articuloId}` - Desasociar un artículo de un cliente.  
  Resultado:

**Seguridad-Controller (`/api/auth`)**
- `POST /api/auth/login` - Autenticar un usuario y devolver un token (JWT).  
  Resultado:
- `POST /api/auth/register` - Registrar un nuevo usuario (puede ser redundante con `POST /api/usuarios`).  
  Resultado:
- `POST /api/auth/refresh-token` - Refrescar un token de acceso expirado usando un token de refresco.  
  Resultado:
- `GET /api/auth/me` - Obtener información del usuario autenticado actualmente.  
  Resultado:

**Youtube-Controller (`/api/youtube`)**
- `GET /api/youtube/videos/{youtubeVideoId}/details` - Obtener detalles (como duración) de un video de YouTube usando su ID.  
  Resultado:
- `GET /api/youtube/search` - Buscar videos en YouTube (proxy a la API de YouTube).  
  Resultado:

**Email-Controller (`/api/email`)**
- `POST /api/email/send` - Enviar un correo electrónico (ej. bienvenida, notificación, etc.). El cuerpo del request contendría destinatario, asunto, contenido.  
  Resultado:
