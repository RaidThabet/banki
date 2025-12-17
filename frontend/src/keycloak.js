import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "https://keycloak.local",
  realm: "banki-app",
  clientId: "react-app",
});

export default keycloak;
