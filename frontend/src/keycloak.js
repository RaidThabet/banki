import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://keycloak.local",
  realm: "banki-app",
  clientId: "spring-boot-app",
});

export default keycloak;
