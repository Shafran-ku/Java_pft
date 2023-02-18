package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class SoapTests extends TestBase {

    @Test
    public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
        Set<Project> projects = app.soap().getProjects();

        //кол-во проектов
        System.out.println(projects.size());
        //название проектов
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }

    @Test
    public void testCreateIssue() throws MalformedURLException, ServiceException, RemoteException {
        //получаем множество проектов
        Set<Project> projects = app.soap().getProjects();
        Issue issue = new Issue().withSummary("Test issue").withDescription("Test issue description").
                withProject(projects.iterator().next());
        Issue created = app.soap().addIssue(issue);
        //сравниваем новый проект Issue с существующим
        assertEquals(issue.getSummary(), created.getSummary());
    }
}
