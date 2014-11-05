package fi.eis.applications.chatapp.actions;

/**
 * User: eis
 * Creation Date: 5.11.2014
 * Creatuin Time: 22:41
 */
public interface LoginHandler {

    String tryLogin(String userName, char[] password) throws LoginFailedException;
}
