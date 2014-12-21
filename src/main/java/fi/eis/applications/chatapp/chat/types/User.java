package fi.eis.applications.chatapp.chat.types;

public class User {

    private final String nick;
    private final String type;
    private final String profileUrl;
    public User(String nick, String type, String profileUrl) {
        this.nick = nick;
        this.type = type;
        this.profileUrl = profileUrl;
    }
    @Override
    public String toString() {
        return "User [nick=" + nick + ", type=" + type + ", profileUrl="
                + profileUrl + "]";
    }
    public String getNick() {
        return nick;
    }
    public String getType() {
        return type;
    }
    public String getProfileUrl() {
        return profileUrl;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nick == null) ? 0 : nick.hashCode());
        result = prime * result
                + ((profileUrl == null) ? 0 : profileUrl.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (nick == null) {
            if (other.nick != null)
                return false;
        } else if (!nick.equals(other.nick))
            return false;
        if (profileUrl == null) {
            if (other.profileUrl != null)
                return false;
        } else if (!profileUrl.equals(other.profileUrl))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
