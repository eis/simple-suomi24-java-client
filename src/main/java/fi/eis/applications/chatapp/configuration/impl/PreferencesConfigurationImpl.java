package fi.eis.applications.chatapp.configuration.impl;

import java.util.prefs.Preferences;

import net.infotrek.util.prefs.FilePreferencesFactory;
import fi.eis.applications.chatapp.App;
import fi.eis.applications.chatapp.configuration.Configuration;

/**
 * Our configuration implementation class.
 * 
 * Uses Java Preferences API and out custom .ini file Preferences API
 * implementation by default. This is to avoid default behaviour of using
 * Windows Registry for this.
 * 
 * @author eis
 *
 */
public class PreferencesConfigurationImpl implements Configuration {


    private static final String DEFAULT_CONFIGURATION_FILE = "chatapp-prefs.ini";

    /**
     * system property that defines our preferences factory class.
     * Basically we assume that this is unset, and set it ourselves to point to
     * out internal implementation using .ini files in the working directory.
     */
    private static final String PREF_FACTORY_CLASS_PROPERTY_NAME = "java.util.prefs.PreferencesFactory";
    
    private final Preferences prefs;
    public PreferencesConfigurationImpl() {
        /**
         * get the property value, giving our value as fallback, and then set that value.
         * This is done for both preferences factory class name as well as default file name
         * to be used. 
         */
        String prefFactoryClassName = System.getProperty(PREF_FACTORY_CLASS_PROPERTY_NAME,
                FilePreferencesFactory.class.getName());
        System.setProperty(PREF_FACTORY_CLASS_PROPERTY_NAME, prefFactoryClassName);
        String prefFileName = System.getProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE,
                DEFAULT_CONFIGURATION_FILE);
        System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, prefFileName);

        /**
         * We use our App.class to define the namespace for out application.
         */
        this.prefs = Preferences.userNodeForPackage(App.class);
    }
}
