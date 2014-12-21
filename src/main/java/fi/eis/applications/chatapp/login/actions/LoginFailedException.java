package fi.eis.applications.chatapp.login.actions;

/**
 * Creation Date: 5.11.2014
 * Creation Time: 22:47
 * 
 * @author eis
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
