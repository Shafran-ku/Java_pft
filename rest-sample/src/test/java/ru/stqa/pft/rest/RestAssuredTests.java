package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestAssuredTests {
    //для того чтобы RestAssured выполнял вход с указанным логином и паролем при каждом обращении к RestAssured
    @BeforeClass
    public void init() {
        RestAssured.authentication = RestAssured.basic("7172fcb5f1888f5fac3dced24caeaa6a", "");
    }


    @Test
    public void testCreateIssue() throws IOException {
        //берем старый список
        Set<Issue> oldIssues = getIssue();

        //создаем новый issue
        Issue newIssue = new Issue().withSubject("my test issue").withDescription("My New test issue");
        int issueId = createIssue(newIssue);

        //прописать иденификатор в новый объект который мы создали
        Set<Issue> newIssues = getIssue();

        //добавить этот объект в старый набор
        oldIssues.add(newIssue.withId(issueId));

        //сравниваем старый набор баг-репортов с новым
        assertEquals(newIssues, oldIssues);

    }

    private Set<Issue> getIssue() throws IOException {

       //авторизация
        RestAssured.get("https://bugify.stqa.ru/api/issues.json").asString();

        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");

        //преобразуем в множество объектов модельных;
        // первый параметр - откуда должна извлекаться инф-ия,
        // 2ой параметр описывает тип данных который должен получиться в конце
        return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
        }.getType());
    }

    //создание баг-репорта
    private int createIssue(Issue newIssue) throws IOException {
        //выполняем запрос на указанный адрес и передаем параметры
        //ответ получаем в виде строки (текст в формате json)
        String json = RestAssured.given()
                .parameter("subject", newIssue.getSubject())
                .parameter("description", newIssue.getDescription())
                .post("https://bugify.stqa.ru/api/issues.json").asString();

        //анализируем полученную строчку
        JsonElement parsed = new JsonParser().parse(json);
        //и извлекаем идентификатор созданного баг-репорта
        return parsed.getAsJsonObject().get("issue_id").getAsInt();
    }
}
