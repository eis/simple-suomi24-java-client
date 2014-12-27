package fi.eis.applications.chatapp.configuration.impl;

import java.util.prefs.Preferences;

import fi.eis.applications.chatapp.configuration.Configuration;

public abstract class AbstractConfiguration implements Configuration {
    protected Preferences prefs;

    // actual prefs beyond this point

    private static final String SHOW_USER_LIST_PREFERENCE = "userlist-visible";

    private static final boolean logAutomaticallyDefaultValue = false;

    private static final String LOG_AUTOMATICALLY = "log-automatically";

    @Override
    public boolean getShowUserList(boolean showUserListDefaultValue) {
        return prefs.getBoolean(SHOW_USER_LIST_PREFERENCE, showUserListDefaultValue);
    }
    @Override
    public void setShowUserList(boolean showUserList) {
        prefs.putBoolean(SHOW_USER_LIST_PREFERENCE, showUserList);
    }
    @Override
    public boolean getLogAutomatically() {
        return prefs.getBoolean(LOG_AUTOMATICALLY, logAutomaticallyDefaultValue);
    }
    @Override
    public void setLogAutomatically(boolean logAutomatically) {
        prefs.putBoolean(LOG_AUTOMATICALLY, logAutomatically);
    }

}
