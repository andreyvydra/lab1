package itmo.is.lab1.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Credentials {
    private int id;
    private String login;
    private String password;
    private String token;

    public static ConcurrentHashMap<Integer, Credentials> createUserMap(List<Credentials> credentialsList) {
        ConcurrentHashMap<Integer, Credentials> userMap = new ConcurrentHashMap<>();

        for (Credentials credentials : credentialsList) {
            Credentials user = new Credentials()
                    .setLogin(credentials.getLogin())
                    .setPassword(credentials.getPassword());
            userMap.put(credentials.getId(), user);
        }

        return userMap;
    }
}
