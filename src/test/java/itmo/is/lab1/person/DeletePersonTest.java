package itmo.is.lab1.person;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import itmo.is.lab1.fragments.SelectCredentials;
import itmo.is.lab1.helpers.AuthorizationHelper;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import static itmo.is.lab1.helpers.ActionHelper.jsr223Action;
import static itmo.is.lab1.helpers.CommonHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class DeletePersonTest {

    private final EmbeddedJmeterEngine embeddedJmeterEngine = new EmbeddedJmeterEngine();
    private final BlockingDeque<String> entities = new LinkedBlockingDeque<>();

    @BeforeTest
    public void init() throws IOException, InterruptedException, TimeoutException {
        AuthorizationHelper.auth("credentials.csv", embeddedJmeterEngine);
    }


    @Test(testName = "DeletePersonTest")
    public void test() throws IOException, InterruptedException, TimeoutException {
        testPlan(
                getHttpDefaults(),
                getCacheDisable(),
                getHeadersDefaults(),

                threadGroup("POST_IMPORT_PERSON_THREAD_GROUP", 4, 10)
                        .children(
                                jsr223Action(SelectCredentials.class),

                                ifController(s -> !entities.isEmpty(),
                                        jsr223Action(s -> s.vars.put("id", new Gson().fromJson(entities.poll(), JsonObject.class).get("id").getAsString())),
                                        httpSampler("DELETE_PERSON", "api/person/${id}/delete")
                                                .method("DELETE")
                                                .encoding(StandardCharsets.UTF_8)
                                                .header("Authorization", "${access_token}")
                                                .children(
                                                        jsr223PostProcessor(s -> s.prev.setDataEncoding(StandardCharsets.UTF_8.name()))
                                                )
                                ),
                                synchronizingTimer(),
                                throughputTimer(120)
                        ),

                threadGroup("GET_PERSON_THREAD_GROUP", 1, 100)
                        .children(
                                jsr223Action(SelectCredentials.class),

                                ifController(s -> entities.isEmpty(),
                                        httpSampler("GET_PERSONS", "api/person")
                                                .method("GET")
                                                .param("size", "50")
                                                .param("ascending", "false")
                                                .encoding(StandardCharsets.UTF_8)
                                                .header("Authorization", "${access_token}")
                                                .children(
                                                        jsr223PostProcessor(s -> s.prev.setDataEncoding(StandardCharsets.UTF_8.name())),
                                                        jsr223PostProcessor(s -> {
                                                            JsonObject jsonObject = new Gson().fromJson(s.prev.getResponseDataAsString(), JsonObject.class);
                                                            JsonArray jsonArray = jsonObject.get("content").getAsJsonArray();
                                                            for (JsonElement jsonElement : jsonArray) {
                                                                entities.add(jsonElement.toString());
                                                            }
                                                        })
                                                )
                                )
                        ),


                resultTree(true),
                resultDashboard(true)
        ).runIn(embeddedJmeterEngine);
    }

}
