package fi.eis.applications.chatapp.login.actions;


/**
 * Creation Date: 5.11.2014
 * Creation Time: 22:41
 * @author eis
 */
public interface LoginHandler {

    void setDebug(boolean value);
    String tryLogin(String userName, char[] password) throws LoginFailedException;
}
