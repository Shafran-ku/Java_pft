package ru.stqa.pft.rest;

import org.testng.annotations.Test;

public class CheckIssueTests extends TestBase{
    @Test
    public void testSkippedIssue() {
        int issueId = 125;       //закрытый Id = 125, открытый Id = 127
        skipIfNotFixed(issueId);
    }
}
