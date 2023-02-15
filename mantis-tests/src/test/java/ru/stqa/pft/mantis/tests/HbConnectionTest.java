package ru.stqa.pft.mantis.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.model.UserData;

import java.util.List;

public class HbConnectionTest {
    //sessionFactory - процедура инициализации(чтение конф-го файла,проверка доступа к БД)
    private SessionFactory sessionFactory;


    @BeforeClass
    protected void setUp() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace(); //вывести сообщение об ошибке на консоль
            // The registry would be destroyed by the SessionFactory, but we had troubled building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void testHbConnection() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<UserData> result = session.createQuery("from UserData").list();
        session.getTransaction().commit();
        session.close();

        for (UserData user : result) {
            System.out.println(user);

        }
    }
}
