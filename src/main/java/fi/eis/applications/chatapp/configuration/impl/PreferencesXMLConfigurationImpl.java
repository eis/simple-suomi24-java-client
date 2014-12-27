package fi.eis.applications.chatapp.configuration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import net.infotrek.util.prefs.FilePreferencesFactory;
import fi.eis.applications.chatapp.App;
import fi.eis.applications.chatapp.configuration.Configuration;

/**
 * Alternative configuration abstraction class.
 * 
 * Uses Java Preferences API and XML export.
 * This is to avoid default behavior of using Windows Registry for this.
 * 
 * @author eis
 *
 */
public class PreferencesXMLConfigurationImpl extends AbstractConfiguration implements Configuration {


    private static final String DEFAULT_CONFIGURATION_FILE = "chatapp-prefs.xml";

    public PreferencesXMLConfigurationImpl() {

        /**
         * Tells to use our file
         */
        System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE,
                DEFAULT_CONFIGURATION_FILE);
        /**
         * We use our App.class to define the namespace for our application.
         */
        this.prefs = Preferences.userNodeForPackage(App.class);
        try {
            File config = new File(DEFAULT_CONFIGURATION_FILE);
            if (config.exists()) {
                InputStream is = new FileInputStream(config);
                Preferences.importPreferences(is);
                is.close();
            }
        } catch (IOException | InvalidPreferencesFormatException e) {
            throw new IllegalArgumentException(e);
        }
        
    }

    @Override
    public void save() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(DEFAULT_CONFIGURATION_FILE));
            prefs.exportSubtree(fos);
            fos.close();
        } catch (IOException | BackingStoreException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
