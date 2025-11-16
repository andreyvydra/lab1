package itmo.is.lab1.person;

import itmo.is.lab1.fragments.SelectCredentials;
import itmo.is.lab1.helpers.AuthorizationHelper;
import org.apache.http.entity.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static itmo.is.lab1.helpers.ActionHelper.jsr223Action;
import static itmo.is.lab1.helpers.CommonHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class ImportPersonTest {

    private final EmbeddedJmeterEngine embeddedJmeterEngine = new EmbeddedJmeterEngine();

    @BeforeTest
    public void init() throws IOException, InterruptedException, TimeoutException {
        AuthorizationHelper.auth("credentials.csv", embeddedJmeterEngine);
    }

    @Test(testName = "ImportPersonTest")
    public void test() throws IOException, InterruptedException, TimeoutException {
        String filePath = ImportPersonTest.class
                .getClassLoader()
                .getResource("person.csv")
                .getFile();

        testPlan(
                getHttpDefaults(),
                getCacheDisable(),
                getHeadersDefaults(),

                threadGroup("POST_IMPORT_PERSON_THREAD_GROUP", 4, 2)
                        .children(
                                jsr223Action(SelectCredentials.class),
                                httpSampler("IMPORT_PERSON", "api/person/import")
                                        .method("POST")
                                        .encoding(StandardCharsets.UTF_8)
                                        .header("Authorization", "${access_token}")
                                        .bodyFilePart("file", filePath, ContentType.MULTIPART_FORM_DATA)
                                        .children(
                                                jsr223PostProcessor(s ->
                                                        s.prev.setDataEncoding(StandardCharsets.UTF_8.name()))
                                        ),
                                synchronizingTimer(),
                                throughputTimer(120)
                        ),

                resultTree(true),
                resultDashboard(true)
        ).runIn(embeddedJmeterEngine);
    }
}

