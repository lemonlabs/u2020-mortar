package co.lemonlabs.mortar.tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

import co.lemonlabs.mortar.example.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isRoot;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@SmallTest
public class FlowTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * General test pattern:
     *
     * 1. Perform action - navigation, button click etc
     * 2. Take screenshot of the view you've got to
     * 3. Verify view you've got is correct by matching views
     *    making checks on them
     *
     * This way when something goes wrong you can visually check
     * what went wrong with your assertions by looking at
     * screenshots in test results
     */
    public void testBasicNavigation() throws Throwable {
        String tag;

        tag = "1. Initial view - Label \"Much Dagger \" should be visible";
        // Any check - needed to trigger waiting on UI thread until data & photos loads
        onView(isRoot()).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        screenshot(tag);
        onView(withText("Much Dagger")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        tag = "2. Click Action Bar home should open side menu";
        onView(withId(android.R.id.home)).perform(click());
        screenshot(tag);
        onView(withContentDescription("drawerList")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        tag = "4. Child on second element in Drawer should go to Nested Presenter Example Screen";
        onData(anything()).inAdapterView(withContentDescription("drawerList")).atPosition(1).perform(click());
        screenshot(tag);
        onView(withId(R.id.nested_child)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }


}
