package ru.stqa.pft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//помощник не требует доступа к браузеру
public class HttpSession {
    private CloseableHttpClient httpclient;
    private ApplicationManager app;

    //конструктор принимает на вход объект типа ApplicationManager
    public HttpSession(ApplicationManager app) {
        this.app = app;

        //создается новый клиент - объект который будет отправлять запросы на сервер
        //этот объкт помещен в поле httpclient
        //setRedirectStrategy - стратегия перенаправления на другие страницы
        httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
    }

    //метод позволяет делать login
    public boolean login(String username, String password) throws IOException {
        //для того чтобы выполнить логин отправляем запрос по адресу ("web.baseUrl") + "/login.php")
        //создаем запрос типа Post
        HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php");

        //формируем набор параметров
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("secure_session", "on"));
        params.add(new BasicNameValuePair("return", "index.php"));

        //далее параметры упаковываем (UrlEncodedFormEntity)
        post.setEntity(new UrlEncodedFormEntity(params));

        //происходит отправка запроса (httpclient.execute(post))
        //response - полученный ответ от сервера
        CloseableHttpResponse response = httpclient.execute(post);

        //получение исходного кода страницы (html)
        String body = geTextFrom(response);

        //проверяется действительно ли пользователь залогинен (код страницы должен содержать нужную строчку)
        return body.contains(String.format("<span class=\"user-info\">%s</span", username));
    }

    private String geTextFrom(CloseableHttpResponse response) throws IOException {
        try {
            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }

    //метод определяет какой пользователь залогинен
    public boolean isLoggedInAs(String username) throws IOException {
        //выполнить запрос типа Get - зайти на главную страницу index
        HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");

        //выполнение запроса, получение ответа
        CloseableHttpResponse response = httpclient.execute(get);
        //получаем текст с помощью функции
        String body = geTextFrom(response);
        //проверяем что в тексте страницы содержится нужный фрагмент (залогинн тот юзер который интересует)
        return body.contains(String.format("<span class=\"user-info\">%s</span", username));
    }
}