package com.everett.apis;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT", realmName = "nlcntt")
@DeclareRoles({ "admin", "visitor" })
public class App extends Application {

}
