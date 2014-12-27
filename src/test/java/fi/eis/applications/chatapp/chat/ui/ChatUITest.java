package fi.eis.applications.chatapp.chat.ui;

import java.awt.GraphicsEnvironment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.configuration.Configuration;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

public class ChatUITest {
    final SimpleLogger logger = new SimpleLogger(this.getClass());
    
    @Mock
    private LoginHandler mockLoginHandler;
    @Mock
    private Configuration mockConfiguration;
    @Mock
    private RoomsProvider mockRoomsProvider;
    @Mock
    private ChattingConnectionFactory mockChatConnectionFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GraphicsEnvironment.getLocalGraphicsEnvironment();
        logger.setLogLevel(LogLevel.NONE);
    }

    @Test
    public void testSetup() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("can't perform UI tests due to headless environment, so skipping them");
            return;
        }
        ChatUI chatUI = new ChatUI(mockLoginHandler,
                mockRoomsProvider,
                mockChatConnectionFactory,
                mockConfiguration);
        chatUI.display();
        chatUI.dispose();
    }
}
