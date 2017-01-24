/*
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.zulip.android.customlintrules.detectors;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Custom lint rule to check e.printStackTrace() is used or not
 */
public class PrintStackTraceDetector extends Detector
        implements Detector.ClassScanner {

    private static final Class<? extends Detector> DETECTOR_CLASS = PrintStackTraceDetector.class;
    private static final EnumSet<Scope> DETECTOR_SCOPE = Scope.CLASS_FILE_SCOPE;

    private static final Implementation IMPLEMENTATION = new Implementation(
            DETECTOR_CLASS,
            DETECTOR_SCOPE
    );

    private static final String ISSUE_ID = "ZLog";
    private static final String ISSUE_DESCRIPTION = "Use `ZLog.logException(e);` instead of `e.printStackTrace();` ";
    private static final String ISSUE_EXPLANATION = "`ZLog.logException(e);` print stacktrace as well as report that to the Crashlytics";
    private static final Category ISSUE_CATEGORY = Category.USABILITY;
    private static final int ISSUE_PRIORITY = 9;
    private static final Severity ISSUE_SEVERITY = Severity.ERROR;

    public static final Issue ISSUE = Issue.create(
            ISSUE_ID,
            ISSUE_DESCRIPTION,
            ISSUE_EXPLANATION,
            ISSUE_CATEGORY,
            ISSUE_PRIORITY,
            ISSUE_SEVERITY,
            IMPLEMENTATION  // This was defined in the "Implementations" section
    );
    private static final String FUNCTION_NAME = "printStackTrace";

    @Override
    public List<String> getApplicableCallNames() {
        return Collections.singletonList(FUNCTION_NAME);
    }

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList(FUNCTION_NAME);
    }

    @Override
    public void checkCall(@NonNull ClassContext context,
                          @NonNull ClassNode classNode,
                          @NonNull MethodNode method,
                          @NonNull MethodInsnNode call) {
        context.report(ISSUE,
                method,
                call,
                context.getLocation(call),
                "You must use our `ZLog.logException(e);` ");

    }
}