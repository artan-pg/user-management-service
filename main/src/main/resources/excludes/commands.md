Create Self-Signed Certificate for undertow server using `keytool` tool in `dev` environment:

```shell
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server.jks -validity 3650
```
