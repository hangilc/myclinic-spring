package jp.chang.myclinic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

    public static class UserInfo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
