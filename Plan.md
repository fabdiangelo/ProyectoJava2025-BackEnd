Claro, aquí tienes una guía clara y concisa para integrar la API de YouTube en un proyecto Java con Spring Boot, incluyendo el uso de Postman, dependencias necesarias y configuración de OAuth 2.0.

---

## 🔧 1. Configuración Inicial

### a. Crear Proyecto en Google Cloud

1. Accede a [Google Cloud Console](https://console.cloud.google.com/).
2. Crea un nuevo proyecto.
3. Habilita la **YouTube Data API v3**.
4. En "Credenciales", crea:
    - Una **clave de API** para acceso público.
    - Un **ID de cliente OAuth 2.0** para acceso autenticado. ([YouTube Data API: redirect URL not working witn Spring Boot and ...](https://stackoverflow.com/questions/66686883/youtube-data-api-redirect-url-not-working-witn-spring-boot-and-react-js?utm_source=chatgpt.com), [Java Quickstart | YouTube Data API - Google for Developers](https://developers.google.com/youtube/v3/quickstart/java?utm_source=chatgpt.com))

### b. Dependencias Maven

Agrega al `pom.xml`:

## 🔐 2. Configuración de OAuth 2.0 en Spring Boot

1. Coloca el archivo `client_secret.json` descargado en `src/main/resources`.
2. Configura la clase `Oauth2Config` para manejar la autenticación: ([Configuring OAuth 2.0 for YouTube Data API V3 in Spring Boot](https://medium.com/%40shreeramchaulagain/configuring-youtube-data-api-v3-in-a-spring-boot-application-847acf54e6a1?utm_source=chatgpt.com))

   ```java
   GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
       httpTransport, jsonFactory, clientSecrets, scopes)
       .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
       .setAccessType("offline")
       .build();
   ```


3. Define el URI de redirección, por ejemplo: `http://localhost:8080/oauth2callback`.

Este proceso permite a los usuarios autorizar el acceso a sus datos de YouTube de forma segura.  ([Configuring OAuth 2.0 for YouTube Data API V3 in Spring Boot](https://medium.com/%40shreeramchaulagain/configuring-youtube-data-api-v3-in-a-spring-boot-application-847acf54e6a1?utm_source=chatgpt.com))

---

## 📦 3. Uso de la API de YouTube

### a. Ejemplo: Obtener Detalles de un Video


```java
YouTube youtubeService = new YouTube.Builder(httpTransport, jsonFactory, credential)
    .setApplicationName("your-app-name")
    .build();

YouTube.Videos.List request = youtubeService.videos()
    .list("snippet,contentDetails,statistics")
    .setId("VIDEO_ID");

VideoListResponse response = request.execute();
```


Este código permite obtener información detallada de un video específico.

---

## 🧪 4. Pruebas con Postman

1. Importa la colección de YouTube desde [Postman API Network](https://www.postman.com/api-evangelist/youtube/overview).
2. Configura las variables de entorno: `API_KEY`, `CLIENT_ID`, `CLIENT_SECRET`.
3. Para endpoints que requieren OAuth 2.0, realiza la autenticación en la pestaña "Authorization" de Postman. ([YouTube | Postman API Network](https://www.postman.com/api-evangelist/youtube/overview?utm_source=chatgpt.com))

Esto te permite probar las solicitudes a la API de YouTube de manera interactiva.

---

## 📚 Recursos Adicionales

- Guía oficial de inicio rápido en Java: [Quickstart Java](https://developers.google.com/youtube/v3/quickstart/java)
- Documentación completa de la API: [YouTube Data API Docs](https://developers.google.com/youtube/v3/docs)
- Repositorio del cliente Java: [GitHub - YouTube Data API v3 Client Library for Java](https://github.com/googleapis/google-api-java-client-services/blob/main/clients/google-api-services-youtube/v3/README.md) ([Java Quickstart | YouTube Data API - Google for Developers](https://developers.google.com/youtube/v3/quickstart/java?utm_source=chatgpt.com), [API Reference | YouTube Data API - Google for Developers](https://developers.google.com/youtube/v3/docs?utm_source=chatgpt.com), [YouTube Data API v3 Client Library for Java - GitHub](https://github.com/googleapis/google-api-java-client-services/blob/main/clients/google-api-services-youtube/v3/README.md?utm_source=chatgpt.com))

---

## 🎥 Tutoriales en Video

- Integración de YouTube con Spring Boot:

  ([YouTube Data API - Spring Boot App integrated with YouTube search](https://www.youtube.com/watch?v=qIDW6VZHJY4&utm_source=chatgpt.com))

- Uso de Postman para pruebas de API: ([Postman Beginner's Course - API Testing - YouTube](https://www.youtube.com/watch?v=VywxIQ2ZXw4&utm_source=chatgpt.com))

  ([Learn Postman for API Testing (Step-by-Step Tutorial)](https://www.youtube.com/watch?v=wEOLZq-7DYs&utm_source=chatgpt.com))

---

¿Necesitas ejemplos específicos como subir videos, buscar por palabra clave o listar playlists? Estoy aquí para ayudarte. 