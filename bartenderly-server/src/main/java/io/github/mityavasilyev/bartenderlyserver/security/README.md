## Logic
1. Basic authentication via login/password is posted to a `/login`
2. All further authentication is based on JWTs


`POST` Request to `/login` with the following body serves as an end point to 
authenticate into an app and receive JSON Web Token

```
{
    "username": "bob",
    "password": "superPassword"
}
```

After successful authentication response's header will contain
JWT Authorization token (key `Authorization`) and a CSRF cookie

To comply with CSRF policy it is required to provide assigned `X-XSRF-TOKEN` 
with each `POST`, `PUT` or `DELETE` request as a header

Sample request headers should at least have these:

```
Authorization : Bearer eyJhbGciOiJIUzI1NiJ9...
X-XSRF-TOKEN  : 8676c6ea-1e65-42c4-a413-d3eca2db9
```

## Configuration

`application.properties` → `application.jwt` provides variables for AuthConfiguration

Options:
- `SecretKey` - Secret on which tokens will be verified and generated
- `tokenPrefix` - Authorization header value prefix. By default, is "Bearer "
- `tokenExpirationAfterDays` - Lifespan of a token
