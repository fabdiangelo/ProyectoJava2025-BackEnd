# Plan Básico de Endpoints 22/04/2025 solo pruebas con endpoints.
- Ejemplos:
  - `200 OK` La operación se realizó con éxito.
  - `201 CREATED` Se creó un nuevo elemento con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No se encontró el elemento correspondiente


***


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
  - `400 BAD REQUEST` No existe un usuario con ese email.
  - `403 FORBIDDEN` Fallo de autorización.


- `PUT /api/usuarios/{email}` *USER/ADMIN*  
  - `200 OK` Se actualizan los datos del usuario.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe el usuario / El body tiene un formato incorrecto.


- `DELETE /api/usuarios/{id}` *USER/ADMIN*
  - `200 OK` El estado del usuario pasa a ser inactivo (false).
  - `400 BAD REQUEST` No existe el usuario.
  - `403 FORBIDDEN` Fallo de autorización.

  
***


### Video-Controller `/api/videos`
- `POST /api/videos` *ADMIN*
  - `201 CREATED` Se creó un nuevo video con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/videos` *ADMIN*
  - `200 OK` Se listan todos los videos.
  - `400 BAD REQUEST` No existen videos que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/videos/{id}` *USER*
  - `200 OK` Se muestran los datos del video.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe un video con esa id.


- `PUT /api/videos/{id}` *ADMIN*
  - `200 OK` Se actualizan los datos del video.
  - `400 BAD REQUEST` No existe el video / El body tiene un formato incorrecto.
  - `403 FORBIDDEN` Fallo de autorización.


- `DELETE /api/videos/{id}` *ADMIN*
  - `200 OK` Se elimina el video.
  - `400 BAD REQUEST` No existe el video.
  - `403 FORBIDDEN` Fallo de autorización.


***


### Paquete-Controller `/api/paquetes`
- `POST /api/paquetes` *ADMIN*
  - `201 CREATED` Se creó un nuevo paquete con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/paquetes` *USER*
  - `200 OK` Se listan todos los paquetes.
  - `400 BAD REQUEST` No existen paquetes que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/paquetes/{id}` *USER*
  - `200 OK` Se muestran los datos del paquete.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe un paquete con esa id.


- `PUT /api/paquetes/{id}` *ADMIN*
  - Se modifica su contenido en `PUT /api/articulos/{id}`


- `DELETE /api/paquetes/{id}` *ADMIN*
  - `200 OK` Se elimina el paquete.
  - `400 BAD REQUEST` No existe el paquete.
  - `403 FORBIDDEN` Fallo de autorización.

//Necesita logica de negocio
- `POST /api/paquetes/{paqueteId}/cursos/{cursoId}` - Asociar un curso a un paquete.  
  Resultado: 403 response status is 403
- `DELETE /api/paquetes/{paqueteId}/cursos/{cursoId}` - Desasociar un curso de un paquete.  
  Resultado: 204 No Content response status is 204
- `GET /api/paquetes/{paqueteId}/cursos` - Listar cursos de un paquete
***


### Curso-Controller `/api/cursos`
- `POST /api/cursos` *ADMIN*
  - `201 CREATED` Se creó un nuevo curso con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/cursos` *USER*
  - `200 OK` Se listan todos los cursos.
  - `400 BAD REQUEST` No existen cursos que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/cursos/{id}` *USER*
  - `200 OK` Se muestran los datos del curso.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe un curso con esa id.


- `PUT /api/cursos/{id}` *ADMIN*
  - Se modifica su contenido en `PUT /api/articulos/{id}`


- `DELETE /api/cursos/{id}` *ADMIN*
  - `200 OK` Se elimina el curso.
  - `400 BAD REQUEST` No existe el curso.
  - `403 FORBIDDEN` Fallo de autorización.


//Necesario implementar los siguientes endpoints:


- `GET /api/curso/{cursoId}/videos` - Obtener los videos asociados a un curso.  
  Resultado:
- `POST /api/curso/{cursoId}/videos/{videoId}` - Asociar un video a un curso.  
  Resultado:


***


### Pago-Controller `/api/pagos`
- `POST /api/pagos` *USER*
  - `201 CREATED` Se creó un nuevo pago con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/pagos` *ADMIN*
  - `200 OK` Se listan todos los pagos.
  - `400 BAD REQUEST` No existen pagos que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/pagos/{id}` *USER*
  - `200 OK` Se muestran los datos del pago.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe un pago con ese id.


//Logica De negocios necesaria
 

- `POST /api/pagos/paypal/webhook` - Endpoint para recibir notificaciones de estado de Paypal.  
  Resultado:
- `POST /api/pagos/mercadopago/webhook` - Endpoint para recibir notificaciones de estado de MercadoPago.  
  Resultado:
- `GET /api/usuarios/{userId}/pagos` - Obtener los pagos de un usuario específico.  


***


### Oferta-Controller `/api/ofertas`
- `POST /api/ofertas` *ADMIN*
  - `201 CREATED` Se creó una nueva oferta con éxito.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/ofertas` *USER*
  - `200 OK` Se listan todas las ofertas.
  - `400 BAD REQUEST` No existen ofertas que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/ofertas/{id}` *USER*
  - `200 OK` Se muestran los datos de la oferta.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe una oferta con ese id.


- `PUT /api/ofertas/{id}` *ADMIN*
  - `200 OK` Se actualizan los datos de la oferta.
  - `400 BAD REQUEST` No existe la oferta / El body tiene un formato incorrecto.
  - `403 FORBIDDEN` Fallo de autorización.


- `DELETE /api/ofertas/{id}` *ADMIN*
  - `200 OK` Se elimina la oferta.
  - `400 BAD REQUEST` No existe la oferta.
  - `403 FORBIDDEN` Fallo de autorización.


// Logica de negocio faltante
- `GET /api/ofertas/me` - Obtener los pagos de una oferta específica.  
  Resultado: 200 OK response status is 200


***


### Articulo-Controller `/api/articulos`
- `PUT /api/articulos/{id}` *ADMIN*
  - `200 OK` Se actualizan los datos del artículo (paquete/curso).
  - `400 BAD REQUEST` No existe el paquete / El body tiene un formato incorrecto.
  - `403 FORBIDDEN` Fallo de autorización.


***


### Articulo-Cliente-Controller `/api/curso-cliente/{email}/articulo/{id}`
- `POST /api/curso-cliente/{email}/articulo/{id}` *USER*
  - `201 CREATED` Se creó una nueva relación entre usuario y artículo.
  - `400 BAD REQUEST` Se envío un cuerpo de solicitud erróneo.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/curso-cliente/{email}/articulo/{id}` *USER*
  - `200 OK` Se muestran los datos de la relación entre usuario y artículo.
  - `403 FORBIDDEN` Fallo de autorización.
  - `404 NOT FOUND` No existe la relación.
  

- `GET /api/curso-cliente` *ADMIN*
  - `200 OK` Se listan todas las relaciónes.
  - `400 BAD REQUEST` No existen relaciones que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/curso-cliente/curso/{id}` *ADMIN*
  - `200 OK` Se listan todos las relaciones con un curso dado.
  - `400 BAD REQUEST` No existen relaciones que listar.
  - `403 FORBIDDEN` Fallo de autorización.


- `GET /api/curso-cliente/usuario/{email}` *ADMIN*
  - `200 OK` Se listan todos las relaciones de un usuario.
  - `400 BAD REQUEST` No existen relaciones que listar.
  - `403 FORBIDDEN` Fallo de autorización.

- `PUT /api/curso-cliente/{email}/articulo/{id}` *USER*
  - `200 OK` Se actualizan los datos de la relación.
  - `400 BAD REQUEST` No existe la relación / El body tiene un formato incorrecto.
  - `403 FORBIDDEN` Fallo de autorización.


- `DELETE /api/curso-cliente/{email}/articulo/{id}` *USER*
  - `200 OK` Se elimina la relación.
  - `400 BAD REQUEST` No existe la relación.
  - `403 FORBIDDEN` Fallo de autorización.


***


### Seguridad-Controller (`/api/auth`)**
- `POST /api/auth` *GUEST*
  - `200 OK` Se crea un token.
  - `400 BAD REQUEST` No existe un usuario con ese email.
  - `403 FORBIDDEN` Fallo de autorización.


// Falta desarrollar


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