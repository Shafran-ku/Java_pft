package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestTests {

    @Test
    public void testCreateIssue() throws IOException {
        //берем старый список
        Set<Issue> oldIssues = getIssues();

        //создаем новый issue
        Issue newIssue = new Issue().withSubject("my test issue").withDescription("My New test issue");
        int issueId = createIssue(newIssue);

        //прописать иденификатор в новый объект который мы создали
        Set<Issue> newIssues = getIssues();

        //добавить этот объект в старый набор
        oldIssues.add(newIssue.withId(issueId));

        //сравниваем старый набор баг-репортов с новым
        assertEquals(newIssues, oldIssues);

    }

    private Set<Issue> getIssues() throws IOException {
        //авторизация
        String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues.json"))
                .returnContent().asString();

        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");

        //первый параметр - откуда должна извлекаться инф-ия,
        // 2ой параметр описывает тип данных который должен получиться в конце
        return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
        }.getType());
    }

    private Executor getExecutor() {
        //указываем Ключ для доступа, пароль пустой
        return Executor.newInstance().auth("7172fcb5f1888f5fac3dced24caeaa6a", "");
    }

    //создание баг-репорта
    private int createIssue(Issue newIssue) throws IOException {
        //bodyForm - для передачи параметров
        String json = getExecutor().execute(
                Request.Post("https://bugify.stqa.ru/api/issues.json").
                        bodyForm(
                                //имя параметра, значение параметра
                                new BasicNameValuePair("subject", newIssue.getSubject()),
                                new BasicNameValuePair("description", newIssue.getDescription())
                        )).returnContent().asString();

        //сначала анализируем строчку
        JsonElement parsed = new JsonParser().parse(json);
        //возвращаем идентификатор созданного баг-репорта
        return parsed.getAsJsonObject().get("issue_id").getAsInt();
    }
}
