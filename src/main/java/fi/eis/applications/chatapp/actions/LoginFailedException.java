package fi.eis.applications.chatapp.actions;

/**
 * User: eis
 * Creation Date: 5.11.2014
 * Creatuin Time: 22:47
 */
public class LoginFailedException extends Exception {
    public LoginFailedException(Throwable t) {
        super(t);
    }
    public LoginFailedException(String msg, Throwable t) {
        super(msg, t);
    }
    public LoginFailedException(String msg) {
        super(msg);
    }
}
