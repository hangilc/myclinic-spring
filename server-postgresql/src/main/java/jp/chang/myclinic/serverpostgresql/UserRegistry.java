package jp.chang.myclinic.serverpostgresql;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix="myclinic")
public class UserRegistry {

    private Map<String, UserInfo> users = new HashMap<>();

    public Map<String, UserInfo> getUsers() {
        return users;
    }

    public void setUsers(Map<String, UserInfo> users) {
        this.users = users;
    }

    public UserInfo getInfo(String user){
        return users.get(user);
    }

    public static class UserInfo {
        private String name;
        private String password;
        private List<String> roles;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
