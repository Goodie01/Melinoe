package org.goodiemania.melinoe.samples;

import org.goodiemania.melinoe.framework.api.Flow;

public class GithubFlow implements Flow {
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    public void navigateToPullRequestPage() {
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }
}
