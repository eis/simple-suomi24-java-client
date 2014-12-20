package fi.eis.applications.chatapp.login.ui;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.awt.GraphicsEnvironment;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.controller.ChatEnterHandler;
import fi.eis.applications.chatapp.login.actions.LoginFailedException;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

public class LoginUITest {
    @Mock
    private LoginHandler loginHandler;
    @Mock
    private ChatEnterHandler chatEnterHandler;
    @Mock
    private RoomsProvider roomFetchHandler;
    @Mock
    private ChattingConnectionFactory chatConnectionFactory;

    final SimpleLogger logger = new SimpleLogger(this.getClass());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GraphicsEnvironment.getLocalGraphicsEnvironment();
        logger.setLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void testCannotConnect() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            logger.error("can't perform UI tests due to headless environment, so skipping them");
            return;
        }
        LoginUI loginUI = LoginUI.createGUI(loginHandler, chatEnterHandler,
                roomFetchHandler, chatConnectionFactory,
                LogLevel.DEBUG);
        when(loginHandler.tryLogin(anyString(), any(char[].class)))
            .thenThrow(new LoginFailedException("some issue"));
        loginUI.loginButtonPressed();
        waitForEDTActions();
        updateUI(loginUI);
        assertThat(loginUI.getTitle(), containsString("some issue"));
    }
    
    static class TestableLoginUI extends LoginUI {

        // the method we need for our tests
        public boolean areListsEnabled() {
            return this.chatRoomList.isEnabled() &&
                    this.userList.isEnabled();
        }
        // helpers to get class constructed
        public static TestableLoginUI createGUI(LoginHandler loginHandler,
                ChatEnterHandler chatEnterHandler, RoomsProvider roomFetchHandler,
                ChattingConnectionFactory chatConnectionFactory, LogLevel logLevel) {
            TestableLoginUI frame = new TestableLoginUI(loginHandler, chatEnterHandler,
                    roomFetchHandler, chatConnectionFactory);

            frame.add(frame.createLoginPanel());
            frame.logger.setLogLevel(logLevel);
            return frame;

        }
        public TestableLoginUI(LoginHandler loginHandler,
                ChatEnterHandler chatEnterHandler,
                RoomsProvider roomFetchHandler,
                ChattingConnectionFactory chatConnectionFactory) {
            super(loginHandler, chatEnterHandler, roomFetchHandler, chatConnectionFactory);
        }
    }
    @Test
    public void testLoadingState() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            logger.error("can't perform UI tests due to headless environment, so skipping them");
            return;
        }
        TestableLoginUI loginUI = 
                TestableLoginUI.createGUI(loginHandler, chatEnterHandler,
                roomFetchHandler, chatConnectionFactory,
                LogLevel.DEBUG);
        when(loginHandler.tryLogin(anyString(), any(char[].class)))
            .then(createDelayAnswer());
        loginUI.loginButtonPressed();
        loginUI.pack();
        loginUI.setVisible(true);
        assertEquals(Boolean.TRUE, Boolean.valueOf(loginUI.areListsEnabled()));
    }
        
    private Answer<?> createDelayAnswer() {
        return
            new Answer<String>() {
                @Override
                public String answer(InvocationOnMock invocation){
                  try {
                      Thread.sleep(5000);
                  } catch (InterruptedException e) {
                      
                  }
                  return "ABCD1234";
                }
             };
    }

    private void updateUI(JFrame loginUI) {
        loginUI.setVisible(true);
        loginUI.setVisible(false);
    }

    /**
     * hack to wait for EDT (event dispatcher thread) to finish its actions
     * @see http://ordify.blogspot.fi/2011/08/recently-i-tried-to-write-some.html
     */
    private static void waitForEDTActions() {
        final Semaphore s = new Semaphore(1,true);
        s.acquireUninterruptibly();
        new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() {
                return null;
            }
            @Override
            protected void done() {
                s.release();
            }
        }.run();
        s.acquireUninterruptibly();
    }
    
}
