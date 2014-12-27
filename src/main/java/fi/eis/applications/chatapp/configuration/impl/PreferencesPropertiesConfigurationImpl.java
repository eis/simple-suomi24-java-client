package fi.eis.applications.chatapp.configuration.impl;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.infotrek.util.prefs.FilePreferencesFactory;
import fi.eis.applications.chatapp.App;
import fi.eis.applications.chatapp.configuration.Configuration;

/**
 * Alternative configuration abstraction class.
 * 
 * Uses Java Preferences API and our custom file Preferences API
 * implementation by default. This is to avoid default behavior of using
 * Windows Registry for this.
 * 
 * Implementation class is actually configured in
 * META-INF/services/java.util.prefs.PreferencesFactory
 * 
 * Note that this implementation DOES NOT support subkeys (the ones with '.'
 * in them). Tweak FilePreferences syncSpi to get subkeys as well.
 * 
 * @author eis
 *
 */
public class PreferencesPropertiesConfigurationImpl extends AbstractConfiguration
    implements Configuration {

    private static final String DEFAULT_CONFIGURATION_FILE = "chatapp-prefs.properties";

    public PreferencesPropertiesConfigurationImpl() {

        /**
         * Tells to use our file
         */
        System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE,
                DEFAULT_CONFIGURATION_FILE);
        /**
         * We use our App.class to define the namespace for our application.
         */
        this.prefs = Preferences.userNodeForPackage(App.class);
    }

    // actual prefs beyond this point

    @Override
    public void save() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new IllegalStateException("was unable to save settings to " + prefs.absolutePath(), e);
        }
    }
}
