package com.inaka.lewis;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.inaka.lewis.issues.RootPackageDetector;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RootPathTest extends LintDetectorTest {


    private Set<Issue> mEnabled = new HashSet<Issue>();

    @Override
    protected Detector getDetector() {
        return new RootPackageDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(RootPackageDetector.ISSUE_CLASS_IN_ROOT_PACKAGE);
    }

    @Override
    protected TestConfiguration getConfiguration(LintClient client, Project project) {
        return new TestConfiguration(client, project, null) {
            @Override
            public boolean isEnabled(@NonNull Issue issue) {
                return super.isEnabled(issue) && mEnabled.contains(issue);
            }
        };
    }

    /**
     * Test
     *
     * @throws Exception
     */
    public void testNotInRoot() throws Exception {
        mEnabled = Collections.singleton(RootPackageDetector.ISSUE_CLASS_IN_ROOT_PACKAGE);

        String expected = "No warnings.";

        String result = lintProject(java("Example.class", "" + "class Example {}"));

        assertEquals(expected, result);
    }
}