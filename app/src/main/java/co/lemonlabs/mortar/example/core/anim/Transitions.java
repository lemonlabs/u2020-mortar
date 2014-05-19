package co.lemonlabs.mortar.example.core.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;

import co.lemonlabs.mortar.example.R;
import flow.Flow;

/**
 * Responsible for loading transition animators between screens.
 * Transitions are specified in {@link Transition} annotation.
 * When pushing a new screen Forward, the annotation specifies what animator
 * to run when removing old views and adding a new view. Use {@link #forward}
 * for these transactions.
 * <p/>
 * For moving Backwards transitions must be stored into the screen object and
 * persisted in the backstack because the annotated Screen is no longer accessible
 * due to the way Flow works. Use {@link #backward} for these transactions
 * using stored transition ids.
 */

@TargetApi(11)
public final class Transitions {

    public static final int NONE = R.animator.empty;

    public static class Animators {
        public Animator in;
        public Animator out;
    }

    /**
     * Load Animators from {@link Transition} annotation
     */
    public static Animators forward(Context context, Object screen) {
        return loadTransition(context, screen.getClass(), Flow.Direction.FORWARD);
    }

    /**
     * Load Animators from stored animator ids in the backstack
     */
    public static Animators backward(Context context, int[] transitions) {
        return createLayoutTransition(context, transitions, Flow.Direction.BACKWARD);
    }

    /**
     * Create an instance of the layout transitions specified in a {@link Transition} annotation.
     */
    private static Animators loadTransition(Context context, Class<?> screenType, Flow.Direction direction) {
        int[] transitions = getTransitionResources(screenType);
        return createLayoutTransition(context, transitions, direction);
    }

    private static Animators createLayoutTransition(Context context, int[] transitionIds, Flow.Direction direction) {

        final boolean forward = direction == Flow.Direction.FORWARD;
        final int addAnimatorId = forward ? transitionIds[0] : transitionIds[2];
        final int removeAnimatorId = forward ? transitionIds[1] : transitionIds[3];

        Animators tuple = new Animators();
        tuple.in = loadAnimator(context, addAnimatorId);
        tuple.out = loadAnimator(context, removeAnimatorId);
        return tuple;
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
    private static Animator loadAnimator(Context context, int animationId) {
        return AnimatorInflater.loadAnimator(context, animationId);
    }


    private Transitions() {
    }

}

