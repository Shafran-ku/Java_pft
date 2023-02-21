package ru.stqa.pft.guthub;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

    @Test
    public void testCommits() throws IOException {
        Github github = new RtGithub("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");
        RepoCommits commits = github.repos().get(new Coordinates.Simple("Shafran-ku", "Java_pft")).commits();
        //итерация по комитам
        //Map - наборы пар
        for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            //вывести коммиты c комментариями
            System.out.println(new RepoCommit.Smart(commit).message());
        }

    }
}
