package org.goodiemania.melinoe.samples.github;


import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.Flow;

public class GithubFlow extends Flow {
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    public GithubFlow(final Session session) {
        super(session);
    }

    public void navigateToPullRequestPage() {
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }
}
