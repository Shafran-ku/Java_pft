package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import java.util.Set;

public class TestBase {
    //для того чтобы RestAssured выполнял вход с указанным логином и паролем при каждом обращении к RestAssured
    @BeforeSuite
    public void init() {
        RestAssured.authentication = RestAssured.basic("7172fcb5f1888f5fac3dced24caeaa6a", "");
    }

    //через Remote API получать из баг-трекера информацию о баг-репорте с заданным идентификатором,
    // и возвращать значение false или true в зависимости от того, помечен он как исправленный или нет
    public boolean isIssueOpen(int issueId) {
        String json = RestAssured.get("https://bugify.stqa.ru/api/issues/"
                + issueId + ".json").asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issuesAsJson = parsed.getAsJsonObject()
                .get("issues");

        Set<Issue> issues = new Gson().fromJson(issuesAsJson,
                new TypeToken<Set<Issue>>() {
                }.getType());
        Issue issue = issues.iterator().next();

        if (issue.getState_name().equals("Resolved") || issue.getState_name().equals("Closed")) {
            return false;
        }
        return true;
    }

    //вызывать её в начале нужного теста, чтобы он пропускался, если баг ещё не исправлен
    public void skipIfNotFixed(int issueId) {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }
}
