package ru.stqa.pft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

    private ApplicationManager app;

    public SoapHelper(ApplicationManager app) {
        this.app = app;
    }

    public Set<Project> getProjects() throws RemoteException, MalformedURLException, ServiceException {
        //метод для извлечения проектов
        MantisConnectPortType mc = new MantisConnectLocator().
                getMantisConnectPort(new URL("http://localhost/mantisbt-2.25.4/api/soap/mantisconnect.php"));
        //массив проектов к которому administrator имеет доступ
        ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");

        //преобразуем данные в модельные объекты
        //превращаем в поток, ко всем элементам потока применяем ф-ию map, которая будует из объекта типа projectData
        //строить новый объект типа project c идентификаторм
        //intValue() чтобы преобразовать bigint в int
        return Arrays.asList(projects).stream().map((p) -> new Project().withId(p.getId().intValue()).
                //и именем; после чего все собираем
                        withName(p.getName())).collect(Collectors.toSet());
    }

    private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        return new MantisConnectLocator()
                .getMantisConnectPort(new URL("http://localhost/mantisbt-2.25.4/api/soap/mantisconnect.php"));
    }


    public Issue addIssue(Issue issue) throws RemoteException, MalformedURLException, ServiceException {
        MantisConnectPortType mc = getMantisConnect();

        //запрашиваем возможные категории
        String[] categories = mc.mc_project_get_categories("administrator", "root", BigInteger.valueOf(issue.getProject().getId()));

        IssueData issueData = new IssueData();
        issueData.setSummary(issue.getSummary());
        issueData.setDescription(issue.getDescription());
        //ObjectRef - ссылка на проект
        issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));

        //указание первой категории баг-репорта
        issueData.setCategory(categories[0]);

        //обратное преобразование
        BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
        IssueData createdIssueData = mc.mc_issue_get("administrator", "root", issueId);

        //преобразуем в модельный объект
        return new Issue().withId(createdIssueData.getId().intValue()).
                withSummary(createdIssueData.getSummary()).withDescription(createdIssueData.getDescription())
                .withProject(new Project().withId(createdIssueData.getProject().getId().intValue())
                        .withName(createdIssueData.getProject().getName()));
    }
}
