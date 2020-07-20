package org.goodiemania.melinoe.samples;

import org.goodiemania.melinoe.framework.api.Flow;
import org.goodiemania.melinoe.framework.api.IgnoreFlowDecoration;
import org.goodiemania.melinoe.framework.api.Session;

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
