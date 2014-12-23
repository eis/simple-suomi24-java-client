package fi.eis.applications.chatapp.configuration;


public interface Configuration {

    boolean getShowUserList(boolean showUserListDefaultValue);

    void setShowUserList(boolean showUserList);

    void save();

}
