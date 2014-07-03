package co.lemonlabs.mortar.tests.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import static android.content.Context.MODE_WORLD_READABLE;
import static android.graphics.Bitmap.CompressFormat.PNG;
import static co.lemonlabs.mortar.tests.util.Chmod.chmodPlusR;
import static co.lemonlabs.mortar.tests.util.Chmod.chmodPlusRWX;

/**
 * Copied from https://github.com/square/spoon/issues/4#issuecomment-25717010
 * and edited for our needs
 */
public class ScreenshotTaker {

    private File   screenShotFolder;
    private String screenShotFileName;
    private String windowManagerString;

    public static File obtainScreenshotDirectory(Context context) {
        try {
            return Spoon.obtainScreenshotDirectory(context, Thread.currentThread());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static File obtainScreenshotDirectory(Context context, Thread currentThread) {
        try {
            return Spoon.obtainScreenshotDirectory(context, currentThread);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String obtainScreenshotName(String tag) {
        return System.currentTimeMillis() + Spoon.NAME_SEPARATOR + tag + Spoon.EXTENSION;
    }

    public ScreenshotTaker(File imgDir, String imgFile) {
        screenShotFolder = imgDir;
        screenShotFileName = imgFile;
        setWindowManagerString();
    }

    public void takeScreenShot() throws Exception {
        takeScreenShot(getRecentDecorView(getWindowDecorViews()));
    }

    public void takeScreenShot(View view) throws Exception {

        if (view == null) {
            throw new RuntimeException("takeScreenShot: view from getWindowDecorViews() is null.");
        }

        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();
        File screenShotFile = new File(screenShotFolder, screenShotFileName);

        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(screenShotFile));
            b.compress(PNG, 100 /* quality */, fos);

            chmodPlusR(screenShotFile);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }


    /**
     * Returns the WindorDecorViews shown on the screen.
     *
     * @return the WindorDecorViews shown on the screen
     */

    public View[] getWindowDecorViews()
    {

        Field viewsField;
        Field instanceField;
        try {
            viewsField = windowManager.getDeclaredField("mViews");
            instanceField = windowManager.getDeclaredField(windowManagerString);
            viewsField.setAccessible(true);
            instanceField.setAccessible(true);
            Object instance = instanceField.get(null);
            return (View[]) viewsField.get(instance);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the most recent DecorView
     *
     * @param views the views to check
     * @return the most recent DecorView
     */

    public final View getRecentDecorView(View[] views) {
        if (views == null) {
            throw new RuntimeException("Error in getRecentDecorView: 0 views passed in.");
        }

        final View[] decorViews = new View[views.length];
        int i = 0;
        View view;

        for (int j = 0; j < views.length; j++) {
            view = views[j];
            if (view != null && view.getClass().getName()
                .equals("com.android.internal.policy.impl.PhoneWindow$DecorView")) {
                decorViews[i] = view;
                i++;
            }
        }
        return getRecentContainer(decorViews);
    }

    /**
     * Returns the most recent view container
     *
     * @param views the views to check
     * @return the most recent view container
     */

    private final View getRecentContainer(View[] views) {
        View container = null;
        long drawingTime = 0;
        View view;

        for(int i = 0; i < views.length; i++){
            view = views[i];
            if (view != null && view.isShown() && view.hasWindowFocus() && view.getDrawingTime() > drawingTime) {
                container = view;
                drawingTime = view.getDrawingTime();
            }
        }
        return container;
    }


    private static Class<?> windowManager;
    static {
        try {
            String windowManagerClassName;
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                windowManagerClassName = "android.view.WindowManagerGlobal";
            } else {
                windowManagerClassName = "android.view.WindowManagerImpl";
            }
            windowManager = Class.forName(windowManagerClassName);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the window manager string.
     */
    private void setWindowManagerString() {

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            windowManagerString = "sDefaultWindowManager";

        } else if (android.os.Build.VERSION.SDK_INT >= 13) {
            windowManagerString = "sWindowManager";

        } else {
            windowManagerString = "mWindowManager";
        }
    }

    /** Copied from Spoon */
    /** Utility class for capturing screenshots for Spoon. */
    public static final class Spoon {
        static final         String  SPOON_SCREENSHOTS = "spoon-screenshots";
        static final         String  NAME_SEPARATOR    = "_";
        static final         String  TEST_CASE_CLASS   = "android.test.InstrumentationTestCase";
        static final         String  TEST_CASE_METHOD  = "runMethod";
        private static final String  EXTENSION         = ".png";
        private static final String  TAG               = "Spoon";
        private static final Object  LOCK              = new Object();
        private static final Pattern TAG_VALIDATION    = Pattern.compile("[a-zA-Z0-9_-]+");

        private static File obtainScreenshotDirectory(Context context, Thread currentThread) throws IllegalAccessException {
            File screenshotsDir = context.getDir(SPOON_SCREENSHOTS, MODE_WORLD_READABLE);

            StackTraceElement testClass = findTestClassTraceElement(currentThread.getStackTrace());
            String className = testClass.getClassName().replaceAll("[^A-Za-z0-9._-]", "_");
            File dirClass = new File(screenshotsDir, className);
            File dirMethod = new File(dirClass, testClass.getMethodName());
            createDir(dirMethod);
            return dirMethod;
        }

        /** Returns the test class element by looking at the method InstrumentationTestCase invokes. */
        static StackTraceElement findTestClassTraceElement(StackTraceElement[] trace) {
            for (int i = trace.length - 1; i >= 0; i--) {
                StackTraceElement element = trace[i];
                if (TEST_CASE_CLASS.equals(element.getClassName()) //
                    && TEST_CASE_METHOD.equals(element.getMethodName())) {
                    return trace[i - 3];
                }
            }

            throw new IllegalArgumentException("Could not find test class!");
        }

        private static void createDir(File dir) throws IllegalAccessException {
            File parent = dir.getParentFile();
            if (!parent.exists()) {
                createDir(parent);
            }
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IllegalAccessException("Unable to create output dir: " + dir.getAbsolutePath());
            }
            chmodPlusRWX(dir);
        }

        private static void deletePath(File path, boolean inclusive) {
            if (path.isDirectory()) {
                File[] children = path.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deletePath(child, true);
                    }
                }
            }
            if (inclusive) {
                path.delete();
            }
        }

        private Spoon() {
            // No instances.
        }
    }

}