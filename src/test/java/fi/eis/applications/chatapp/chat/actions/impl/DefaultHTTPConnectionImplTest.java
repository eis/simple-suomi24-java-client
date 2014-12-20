package fi.eis.applications.chatapp.chat.actions.impl;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Test;

import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Creation Date: 23.11.2014
 * Creation Time: 22:40
 *
 * @author eis
 */
public class DefaultHTTPConnectionImplTest {
    String cookieTestValue = "DRUPAL_UID=6563241; path=/; domain=.suomi24.fi\n" +
            "SOLauth=Decccccccccccccccccccccccccccccccccccccccccccccccccccc; path=/; domain=.suomi24.fi; secure\n" +
            "S24auth=S24_dacccccccccccccccccccccccccccccc; path=/; domain=.suomi24.fi\n" +
            "emeu=5%2C33%2C262144; expires=Tue, 15-Nov-2044 20:39:16 GMT; path=/; domain=.suomi24.fi\n" +
            "emediate_user=deleted; expires=Sat, 23-Nov-2013 20:39:15 GMT; path=/; domain=.suomi24.fi\n" +
            "cacheControlDisabled=0; path=/; domain=.suomi24.fi\n" +
            "cacheControlSessionHash=19cccccccccccccccccccccccccccccccccccccc; path=/; domain=.suomi24.fi\n" +
            "cacheControlAuthenticated=1; path=/; domain=.suomi24.fi\n" +
            "SESS33ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc; path=/; domain=.suomi24.fi; HttpOnly\n" +
            "cacheControlDisabled=0; path=/; domain=.suomi24.fi\n" +
            "cacheControlSessionHash=cccccccccccccccccccccccccccccccccccccccc; path=/; domain=.suomi24.fi\n" +
            "cacheControlAuthenticated=0; path=/; domain=.suomi24.fi";

    @Test
    public void testCookieHandling() throws Exception {
        String expected = ("DRUPAL_UID=6563241; " +
                "SOLauth=Decccccccccccccccccccccccccccccccccccccccccccccccccccc; " +
                "S24auth=S24_dacccccccccccccccccccccccccccccc; " +
                "emeu=5%2C33%2C262144; " +
                "emediate_user=deleted; " +
                "cacheControlDisabled=0; " +
                "cacheControlSessionHash=19cccccccccccccccccccccccccccccccccccccc; " +
                "cacheControlAuthenticated=1; " +
                "SESS33ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc; " +
                "cacheControlDisabled=0; " +
                "cacheControlSessionHash=cccccccccccccccccccccccccccccccccccccccc; " +
                "cacheControlAuthenticated=0");
        DefaultHTTPConnectionImpl impl = new DefaultHTTPConnectionImpl(LogLevel.NONE);
        String result = impl.formatAsJavaNetAcceptableCookieString(cookieTestValue);
        Assert.assertThat(result, is(expected));
    }
}
