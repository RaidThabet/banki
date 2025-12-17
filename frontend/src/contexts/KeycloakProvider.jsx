// KeycloakProvider.jsx
import React, { useState, useEffect } from "react";
import keycloak from "../keycloak"; 

export const KeycloakProvider = ({ children }) => {
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "login-required",
        checkLoginIframe: false,
      })
      .then((authenticated) => {
        if (!authenticated) {
          console.warn("User not authenticated");
        }
        setInitialized(true);
      })
      .catch((err) => {
        console.error("Keycloak init failed", err);
      });
  }, []);

  return <>{children}</>;
};
