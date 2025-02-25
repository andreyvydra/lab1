package itmo.is.lab1.fragments;

import itmo.is.lab1.data.Credentials;
import us.abstracta.jmeter.javadsl.core.preprocessors.DslJsr223PreProcessor.PreProcessorScript;
import us.abstracta.jmeter.javadsl.core.preprocessors.DslJsr223PreProcessor.PreProcessorVars;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SelectCredentials implements PreProcessorScript {
    @Override
    @SuppressWarnings("unchecked")
    public void runScript(PreProcessorVars s) {
        ConcurrentHashMap<Integer, Credentials> userMap = (ConcurrentHashMap<Integer, Credentials>) s.props.get("CREDENTIALS");

        int randomIndex = new Random().nextInt(userMap.size());
        Integer randomKey = (Integer) userMap.keySet().toArray()[randomIndex];
        Credentials user = userMap.get(randomKey);

        s.vars.put("insured_id", randomKey.toString());
        s.vars.put("username", user.getLogin());
        s.vars.put("password", user.getPassword());
        s.vars.put("access_token", user.getToken());
    }
}