package itmo.is.lab1.helpers;

import itmo.is.lab1.data.Credentials;
import itmo.is.lab1.fragments.LoginFragment;
import org.apache.log4j.Logger;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import static itmo.is.lab1.helpers.ActionHelper.jsr223Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import static itmo.is.lab1.helpers.CommonHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class AuthorizationHelper {
    public static LoginFragment loginFragment;
    private static final Logger logger = Logger.getLogger(AuthorizationHelper.class);

    private static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = AuthorizationHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public static ArrayList<String[]> readFile(String filePath, String delimiter) {
        InputStream is = getFileFromResourceAsStream(filePath);

        ArrayList<String[]> strings = new ArrayList<>();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                strings.add(nextLine.split(delimiter));
            }
            return strings;
        } catch (IOException e) {
            logger.error("Error reading CSV file:" + e);
            throw new RuntimeException(e);
        }
    }


    private static ArrayList<Credentials> getData(String filePath) {
        ArrayList<String[]> strings = readFile(filePath, ";");
        ArrayList<Credentials> insuranceCompanyCredentials = new ArrayList<>();

        for (String[] s : strings) {
            if (s.length >= 3) {
                insuranceCompanyCredentials.add(new Credentials()
                        .setId(Integer.parseInt(s[0]))
                        .setLogin(s[1])
                        .setPassword(s[2]));
            }
        }

        return insuranceCompanyCredentials;
    }

    public static void auth(String credentialsPath, EmbeddedJmeterEngine embeddedJmeterEngine) throws IOException, InterruptedException, TimeoutException {
        ArrayList<Credentials> credentials = getData(credentialsPath);

        ConcurrentHashMap<Integer, Credentials> userMap = Credentials.createUserMap(credentials);
        loginFragment = new LoginFragment(
                "LOGIN",
                "http",
                "localhost",
                "auth/signin"
        );

        testPlan(
                getHttpDefaults(),
                getCacheDisable(),
                getHeadersDefaults(),
                setupThreadGroup("BEFORE_TEST",
                        forLoopController(credentials.size(),
                                counter("counter")
                                        .startingValue(1),
                                jsr223Action(s -> {
                                    int counter = Integer.parseInt(s.vars.get("counter")) - 1;
                                    s.vars.put("username", credentials.get(counter).getLogin());
                                    s.vars.put("password", credentials.get(counter).getPassword());
                                }),
                                loginFragment.get(),
                                ifController(s -> s.prev.isSuccessful(),
                                        jsr223Action(s -> {
                                            userMap.get(credentials.get(Integer.parseInt(s.vars.get("counter")) - 1).getId())
                                                    .setToken(String.format("Bearer %s", s.vars.get("token")));
                                        })
                                )
                        )
                ),
                resultTree(true)
        ).runIn(embeddedJmeterEngine);
        embeddedJmeterEngine.prop("CREDENTIALS", userMap);
    }
}
