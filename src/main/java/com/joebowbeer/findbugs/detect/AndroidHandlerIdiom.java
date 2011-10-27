package com.joebowbeer.findbugs.detect;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ch.Subtypes2;
import edu.umd.cs.findbugs.bcel.PreorderDetector;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;

public class AndroidHandlerIdiom extends PreorderDetector {

    private final BugReporter bugReporter;

    public AndroidHandlerIdiom(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        classContext.getJavaClass().accept(this);
    }

    @Override
    public void visit(JavaClass jclass) {
        if (Subtypes2.instanceOf(jclass, "android.os.Handler") && hasOuterInstance(jclass)) {
            bugReporter.reportBug(
                    new BugInstance(this, "ANDROID_HANDLER_IDIOM", NORMAL_PRIORITY).addClass(this));
        }
    }

    private boolean hasOuterInstance(JavaClass jclass) {
        for (Field f : jclass.getFields()) {
            if (f.getName().startsWith("this$")) {
                return true;
            }
        }
        return false;
    }
}
