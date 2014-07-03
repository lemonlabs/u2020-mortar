package co.lemonlabs.mortar.tests.util;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ViewMatchers {

    /**
     * Matches views that have specified child count
     *
     * @param count
     * @return
     */
    public static Matcher<View> withChildrenCount(final int count) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely(View view) {
                return view instanceof ViewGroup && ((ViewGroup) view).getChildCount() == count;
            }

            @Override public void describeTo(Description description) {
                description.appendText("Matches ViewGroups with childrenCount == " + count);
            }
        };
    }

    /**
     * Matches Views that are selected
     */
    public static Matcher<View> isSelected() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely(View view) {
                return view.isSelected();
            }

            @Override public void describeTo(Description description) {
                description.appendText("Matches Views where isSelected() == true");
            }
        };
    }

    /**
     * Matches views that have the drawable state provided
     * @param drawableState e.g. R.attr.state_pressed
     * @return
     */
    public static Matcher<View> withDrawableState(final int drawableState) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely(View view) {
                int[] states = view.getDrawableState();
                for (int i = 0; i < states.length; i++) {
                    if (states[i] == drawableState) {
                        return true;
                    }
                }

                return false;
            }

            @Override public void describeTo(Description description) {
                description.appendText("Matches views that have the drawable state provided");
            }
        };
    }

    public static Matcher<View> withAlpha(final float alpha) {
        return new TypeSafeMatcher<View>() {
            private float matchedAlpha;
            @Override public boolean matchesSafely(View view) {
                matchedAlpha = view.getAlpha();
                return view.getAlpha() == alpha;
            }

            @Override public void describeTo(Description description) {
                description.appendText(String.format("alpha should be %.2f, was %.2f", alpha, matchedAlpha));
            }
        };
    }

    public static Matcher<View> anyView() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely(View view) {
                return true;
            }
            @Override public void describeTo(Description description) {

            }
        };
    }
}
