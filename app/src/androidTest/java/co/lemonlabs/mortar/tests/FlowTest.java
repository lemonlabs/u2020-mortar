package co.lemonlabs.mortar.tests;

import android.test.suitebuilder.annotation.SmallTest;

import co.lemonlabs.mortar.example.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

@SmallTest
public class FlowTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testBasicNavigation() throws Throwable {
        String tag;

        tag = "1. Click on Action Bar should flow to new screen";
        onView(withText("Blaze it")).perform(click());
        screenshot(tag);

        onView(withId(R.id.stub_text)).check(matches(withText("STUB SCREEN 420")));

    }

}
