# feud-admin

How to compile:

1. Download and install Apache Maven from https://maven.apache.org/
2. Run command `mvn package -DskipTests` inside root
3. You can detect jar inside target folder. This jar can be used for developing and explotation in production environment
4. Application has prod and default profiles. Default profile uses h2 for local explotation. Prod profile uses settings for PostgreSQL.
See here https://www.baeldung.com/spring-profiles for more information.
5. Application can use your own application.yml. See https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html for more information.
