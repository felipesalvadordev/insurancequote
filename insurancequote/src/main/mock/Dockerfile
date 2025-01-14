FROM wiremock/wiremock:2.35.0-alpine

COPY mappings ./mappings
COPY files ./files

EXPOSE 8088

CMD ["java", "-cp", "/var/wiremock/lib/*:/var/wiremock/extensions/*", "com.github.tomakehurst.wiremock.standalone.WireMockServerRunner", "--global-response-templating", "--container-threads=20", "--async-response-enabled=true", "--async-response-threads=20", "--no-request-journal=true", "--disable-gzip=true"]