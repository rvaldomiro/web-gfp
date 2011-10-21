import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.commons.AllTestsCommons;
import test.gfp.AllTestsGfp;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AllTestsCommons.class, AllTestsGfp.class })
public class AllTests {
	//
}
