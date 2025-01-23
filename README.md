# Microservicio Usuarios

Endpoints:
- POST /signup
  Create user with token
  Request:
  {
    "name": String,
    "email": String,
    "password": String,
    "phones": [
    {
      "number": long,
      "citycode": int,
      "contrycode": String
    }
    ]
  }
- GET /login/{id}
  HEADER(token)
  get user information updating token

Execute class com.globallogic.usuarios.UsuariosApplication to expose above endpoints
