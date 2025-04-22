# Plan Básico de Endpoints 22/04/2025 solo pruebas con endpoints.
- Ejemplos:
  - `200 OK` La operación se realizó con éxito.
  - `201 CREATED` Se creó un nuevo elemento con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 OK` No se encontró el elemento correspondiente


### Usuario-Controller `/api/usuarios`
- `POST /api/usuarios` *GUEST*
  - `201 CREATED` Se creó un nuevo usuario con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.


- `GET /api/usuarios` *ADMIN*
    - `200 OK` Se listan todos los usuario.
    - `400 BAD REQUEST` No existen usuarios que listar.
    - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/usuarios/{email}` *USER/ADMIN*
  - `200 OK` Se muestran los datos del usuario.
  - `400 BAD REQUEST` No existen datos que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `PUT /api/usuarios/{id}` *USER/ADMIN*  
  - `200 OK` Se actualizan los datos del usuario.
  - `400 BAD REQUEST` No existe el usuario / El body tiene un formato incorrecto.
  - `403 FORBIDDEN` Fallo de autorización.


- `DELETE /api/usuarios/{id}` *USER/ADMIN*
  - `200 OK` Se eliminan los datos del usuario.
  - `400 BAD REQUEST` No existe el usuario.
  - `403 FORBIDDEN` Fallo de autorización.


**Pruebas Video-Controller (Escenario: No existe ningún video previamente)**

Sin videos previamente creados
**Video-Controller (`/api/videos`)**
*(Resultados de pruebas pendientes)*
- `GET /api/videos` - Obtener una lista de todos los videos.
- Resultado: 200  OK response status is 200
- `GET /api/videos/{id}` - Obtener detalles de un video específico por ID.
- Resultado: 404 response status is 404
- `PUT /api/videos/{id}` - Actualizar la información de un video existente.
- Resultado: 404 response status is 404
- `DELETE /api/videos/{id}` - Eliminar un video.
- Resultado: 204 response status is 204 No Content
- `POST /api/videos` - Subir/registrar un nuevo video.
- Resultado: 201 response status is 201 created

//Los endpoint funcionan aun no esta vinculados a la logica de negocio asi que no crea el Video




**Paquete-Controller (`/api/paquetes`)**
- `POST /api/paquetes` - Crear un nuevo paquete (de cursos, artículos, etc.).  
  Resultado: 403 response status is 403 Forbidden
- `GET /api/paquetes` - Obtener una lista de todos los paquetes.  
  Resultado: 200 OK response status is 200
- `GET /api/paquetes/{id}` - Obtener detalles de un paquete específico por ID.  
  Resultado: 404 response status is 404
- `PUT /api/paquetes/{id}` - Actualizar la información de un paquete existente.  
  Resultado: 403 response status is 403 Forbidden
- `DELETE /api/paquetes/{id}` - Eliminar un paquete.  
  Resultado: 204 response status is 204 No Content



**Curso-Controller (`/api/cursos`)**
- `POST /api/cursos` - Crear un nuevo curso.
  Resultado: response status is 403

- `GET /api/cursos` - Obtener una lista de todos los cursos.  
  Resultado: 200 OK response status is 200
- `GET /api/cursos/{id}` - Obtener detalles de un curso específico por ID.  
  Resultado: 404 response status is 404
- `PUT /api/cursos/{id}` - Actualizar la información de un curso existente.  
  Resultado: 403 response status is 403

//Necesita logica de negocio
- `POST /api/paquetes/{paqueteId}/cursos/{cursoId}` - Asociar un curso a un paquete.  
  Resultado: 403 response status is 403
- `DELETE /api/paquetes/{paqueteId}/cursos/{cursoId}` - Desasociar un curso de un paquete.  
  Resultado: 204 No Content response status is 204


**Pago-Controller (`/api/pagos`)**
- `POST /api/pagos` - Iniciar un proceso de pago (puede devolver URL de redirección a Paypal/MercadoPago).  
  Resultado: 403 Forbidden response status is 403
- `GET /api/pagos` - Obtener historial de pagos (probablemente filtrado por usuario o admin).  
  Resultado: 200 OK response status is 200
- `GET /api/pagos/{id}` - Obtener detalles de un pago específico.  
  Resultado: Forbidden response status is 403

  //Logica De negocios necesaria
- `POST /api/pagos/paypal/webhook` - Endpoint para recibir notificaciones de estado de Paypal.  
  Resultado:
- `POST /api/pagos/mercadopago/webhook` - Endpoint para recibir notificaciones de estado de MercadoPago.  
  Resultado:
- `GET /api/usuarios/{userId}/pagos` - Obtener los pagos de un usuario específico.  
  Resultado:

**Oferta-Controller (`/api/ofertas`)**
- `POST /api/ofertas` - Crear una nueva oferta.  
  Resultado: 403 Forbidden response status is 403
- `GET /api/ofertas` - Obtener una lista de todas las ofertas activas.  
  Resultado: 202 response status is 202
- `GET /api/ofertas/{id}` - Obtener detalles de una oferta específica por ID.  
  Resultado: 404 response status is 404
- `PUT /api/ofertas/{id}` - Actualizar una oferta existente.  
  Resultado: 403 response status is 403
- `DELETE /api/ofertas/{id}` - Eliminar/desactivar una oferta.  
  Resultado: 204  response status is 204
- `GET /api/ofertas/me` - Obtener los pagos de una oferta específica.  
  Resultado: 200 OK response status is 200


**Curso-Controller (`/api/curso`)**
- `POST /api/curso` - Crear un nuevo curso.  
  Resultado: 403 Forbidden response status is 403

- `GET /api/curso` - Obtener una lista de todos los cursos.  
  Resultado: 200 OK response status is 200

- `GET /api/curso/{id}` - Obtener detalles de un curso específico por ID.  
  Resultado: 404 response status is 404

- `PUT /api/curso/{id}` - Actualizar la información de un curso existente.  
  Resultado: 404 response status is 404

- `DELETE /api/curso/{id}` - Eliminar un curso.  
  Resultado: 204 response status is 204

//Necesario implementar los siguientes endpoints:
- `GET /api/curso/{cursoId}/videos` - Obtener los videos asociados a un curso.  
  Resultado:
- `POST /api/curso/{cursoId}/videos/{videoId}` - Asociar un video a un curso.  
  Resultado:


**Articulo-Controller (`/api/articulos`)**
- `POST /api/articulos` - Crear un nuevo artículo.  
  Resultado: response status is 403 Forbidden
- `GET /api/articulos` - Obtener una lista de todos los artículos.  
  Resultado:  200 OK response status is 200
- `GET /api/articulos/{id}` - Obtener detalles de un artículo específico por ID.  
  Resultado: 404 response status is 404
- `PUT /api/articulos/{id}` - Actualizar un artículo existente.  
  Resultado: Error: response status is 403
- `DELETE /api/articulos/{id}` - Eliminar un artículo.  
  Resultado: 204 response status is 204

**Articulo-Cliente-Controller (`/api/usuarios/{userId}/articulos`)**
//Hacer pruebas en usuario antes
- `GET /api/usuarios/{userId}/articulos` - Obtener los artículos asociados/comprados por un cliente/usuario.  
  Resultado:
- `POST /api/usuarios/{userId}/articulos/{articuloId}` - Asociar un artículo a un cliente (ej. después de una compra o suscripción).  
  Resultado:
- `DELETE /api/usuarios/{userId}/articulos/{articuloId}` - Desasociar un artículo de un cliente.  
  Resultado:

// Preguntar a Fabricio antes de realizar pruebas
**Seguridad-Controller (`/api/auth`)**
- `POST /api/auth/login` - Autenticar un usuario y devolver un token (JWT).  
  Resultado:
- `POST /api/auth/register` - Registrar un nuevo usuario (puede ser redundante con `POST /api/usuarios`).  
  Resultado:
- `POST /api/auth/refresh-token` - Refrescar un token de acceso expirado usando un token de refresco.  
  Resultado:
- `GET /api/auth/me` - Obtener información del usuario autenticado actualmente.  
  Resultado:

//Implementar logica de negocio
**Youtube-Controller (`/api/youtube`)**
- `GET /api/youtube/videos/{youtubeVideoId}/details` - Obtener detalles (como duración) de un video de YouTube usando su ID.  
  Resultado:
- `GET /api/youtube/search` - Buscar videos en YouTube (proxy a la API de YouTube).  
  Resultado:


**Email-Controller (`/api/email`)**
- `POST /api/email/send` - Enviar un correo electrónico (ej. bienvenida, notificación, etc.). El cuerpo del request contendría destinatario, asunto, contenido.  
  Resultado:

Lo que hay disponible son dos metodos Get, hay que revistar los controllers 