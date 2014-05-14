package co.lemonlabs.mortar.example.core.anim;

import android.content.Context;
import android.view.animation.AnimationUtils;

/**
 * Responsible for loading transition animations between screens.
 * Transitions are specified in {@link Transition} annotation.
 * When pushing a new screen Forward, the annotation specifies what animations
 * to run when removing old views and adding new views. Use {@link #pushIn} and
 * {@link #pushOut} for these transactions.
 *
 * For moving Backwards transitions must be stored into the screen object and
 * persisted in the backstack because the annotated Screen is no longer accessible
 * due to the way Flow works. Use {@link #popIn} and {@link #popOut} for these
 * transactions using stored transition resources.
 */
public final class Transitions {

    public static android.view.animation.Animation pushIn(Context context, Object screen) {
        return loadTransition(context, screen.getClass(), true);
    }

    public static android.view.animation.Animation pushOut(Context context, Object screen) {
        return loadTransition(context, screen.getClass(), false);
    }

    public static android.view.animation.Animation popIn(Context context, int[] transitions) {
        return loadAnimation(context, transitions[2]);
    }

    public static android.view.animation.Animation popOut(Context context, int[] transitions) {
        return loadAnimation(context, transitions[3]);
    }

    /**
     * Create an instance of the animations specified in a {@link Transition} annotation.
     */
    private static android.view.animation.Animation loadTransition(Context context, Class<?> screenType, boolean in) {
        int[] transitions = getTransitionResources(screenType);
        return loadAnimation(context, in ? transitions[0] : transitions[1]);
    }

    /**
     * Retrieve resources of the animations specified in a {@link Transition} annotation.
     */
    public static int[] getTransitionResources(Class<?> screenType) {
        Transition transitionScreen = screenType.getAnnotation(Transition.class);
        if (transitionScreen == null) {
            throw new IllegalArgumentException(
                String.format("Class %s does not have %s annotation", screenType.getClass().getSimpleName(), Transition.class.getSimpleName())
            );
        }

        if (transitionScreen.value().length != 4) {
            throw new IllegalArgumentException(
                String.format("Must provide 4 animation resource values for %s", Transition.class.getSimpleName())
            );
        }

        return transitionScreen.value();
    }

    /**
     * Inflate an animation resource
     */
    private static android.view.animation.Animation loadAnimation(Context context, int animationId) {
        return AnimationUtils.loadAnimation(context, animationId);
    }


    private Transitions() {
    }
}
