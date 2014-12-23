package fi.eis.applications.chatapp.login.actions;

import fi.eis.libraries.di.SimpleLogger.LogLevel;


/**
 * Creation Date: 5.11.2014
 * Creation Time: 22:41
 * @author eis
 */
public interface LoginHandler {

    void setLogLevel(LogLevel logLevel);
    String tryLogin(String userName, char[] password) throws LoginFailedException;
}
