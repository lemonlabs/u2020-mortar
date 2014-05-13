package co.lemonlabs.mortar.tests;

import android.test.suitebuilder.annotation.SmallTest;

import co.lemonlabs.mortar.example.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;

@SmallTest
public class FlowTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * No useful things are actually tested. This is just for checking if configuration works
     *
     * @throws Throwable
     */
    public void testBasicNavigation() throws Throwable {
        String tag;

        tag = "1. Click on Action Bar should flow to new screen";
        onView(withText("Blaze it")).perform(click());
        screenshot(tag);

        onView(withId(R.id.stub_text)).check(matches(withText("STUB SCREEN 420")));

        tag = "2. Click Action Bar back should return to Gallery";
        onView(withId(android.R.id.home)).perform(click());
        screenshot(tag);

        tag = "3. Click on Action Bar Toggle should open Navigation Drawer";
        onView(withId(android.R.id.home)).perform(click());
        screenshot(tag);

        tag = "4. Child on second element in Drawer should go to Child Presenter Example Screen";
        onData(anything()).inAdapterView(withContentDescription("drawerList")).atPosition(1).perform(click());
        screenshot(tag);
    }


}
