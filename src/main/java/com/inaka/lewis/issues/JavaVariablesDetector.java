package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.inaka.lewis.utils.PackageManager;

import lombok.ast.AstVisitor;
import lombok.ast.Block;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.KeywordModifier;
import lombok.ast.MethodDeclaration;
import lombok.ast.Node;
import lombok.ast.VariableDeclaration;
import lombok.ast.VariableDefinitionEntry;

public class JavaVariablesDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE_INSTANCE_VARIABLE_NAME = Issue.create(
            "InstanceVariableName",
            "An instance variable should be named beginning with 'm' and with camelCase.",
            "Every instance variable should be named beginning with 'm' and be written in camelCase, for example: 'mCounter'." +
                    "Except if the class is a model (should be inside a package called 'models').",
            Category.TYPOGRAPHY,
            4,
            Severity.WARNING,
            new Implementation(JavaVariablesDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context) {
        return new ForwardingAstVisitor() {

            @Override
            public boolean visitVariableDeclaration(VariableDeclaration node) {

                if (hasClassParent(node)) {

                    Node classDeclaration = node.getParent();

                    VariableDefinitionEntry variableDefinition = node.astDefinition().astVariables().first();
                    String name = variableDefinition.astName().astValue();

                    if (!isStaticOrFinal(node) && !isModel(context, classDeclaration)) {
                        if (!instanceVariableCorrectFormat(name)) {
                            context.report(ISSUE_INSTANCE_VARIABLE_NAME, context.getLocation(node),
                                    "Expecting " + name + " to begin with 'm' and be written in camelCase.");
                        }
                    }
                }

                return super.visitVariableDeclaration(node);
            }

        };
    }

    private boolean isModel(JavaContext context, Node classDeclaration) {
        String classFilePackage = PackageManager.getPackage(context, classDeclaration);
        return classFilePackage.contains(".models.");
    }

    private boolean hasClassParent(VariableDeclaration variableDeclaration) {
        MethodDeclaration methodDeclaration = variableDeclaration.astDefinition().upIfParameterToMethodDeclaration();
        ConstructorDeclaration constructorDeclaration = variableDeclaration.astDefinition().upIfParameterToConstructorDeclaration();
        Block block = variableDeclaration.astDefinition().upUpIfLocalVariableToBlock();

        return methodDeclaration == null && constructorDeclaration == null && block == null;
    }

    private boolean isStaticOrFinal(VariableDeclaration variableDeclaration) {
        boolean isStaticOrFinal = false;
        for (KeywordModifier keywordModifier : variableDeclaration.astDefinition().astModifiers().astKeywords()) {
            if (keywordModifier.astName().equals("static") || keywordModifier.astName().equals("final")) {
                isStaticOrFinal = true;
            }
        }
        return isStaticOrFinal;
    }

    private boolean instanceVariableCorrectFormat(String name) {
        return name.startsWith("m") && Character.isUpperCase(name.charAt(1))
                && !name.contains("_") && !name.contains("-");
    }
}
