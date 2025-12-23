// KeycloakProvider.jsx
import React, {useState, useEffect} from "react";
import keycloak from "../keycloak";

export const KeycloakProvider = ({children}) => {
    const [initialized, setInitialized] = useState(false);

    useEffect(() => {
        keycloak
            .init({
                onLoad: "login-required",
                checkLoginIframe: false,
            })
            .then((authenticated) => {
                if (authenticated) {
                    // Store the token in localStorage
                    localStorage.setItem("jwt", keycloak.token);

                    // Set up token refresh
                    keycloak.onTokenExpired = () => {
                        keycloak.updateToken(30).then((refreshed) => {
                            if (refreshed) {
                                localStorage.setItem("jwt", keycloak.token);
                            }
                        }).catch(() => {
                            console.error("Failed to refresh token");
                            keycloak.logout();
                        });
                    };
                } else {
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
