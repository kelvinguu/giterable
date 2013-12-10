package com.github.giterable;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * User: Kelvin
 * Date: 12/9/13
 * Time: 5:58 PM
 */

@RunWith(Suite.class)
@SuiteClasses({ GiterableTests.class, GitLoaderTests.class })
public class AllTests {

}
