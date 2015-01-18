package co.songliao.guitvi.songtest;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * Created by Song on 1/12/15.
 */
public class TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(TestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public TestSuite() {
        super();
    }

}
