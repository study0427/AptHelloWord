package com.youlingcao.annotation.compile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.youlingcao.annotation.BindView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {
    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.youlingcao.annotation.api", "ViewBinder");
        static final ClassName PROVIDER = ClassName.get("com.youlingcao.annotation.api", "ViewFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<BindViewField> mFields;
    private Elements mElements;

    public AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.mTypeElement = typeElement;
        this.mElements = elements;
        mFields = new ArrayList<>();
    }

    void addField(BindViewField field) {
        mFields.add(field);
    }

    JavaFile generateFile() {
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "finder");

        for (BindViewField field : mFields) {
            bindViewMethod.addStatement("host.$N = ($T)(finder.findView(source, $L))",
                    field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
        }
        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);

        for (BindViewField field : mFields) {
            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
        }
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                .addMethod(bindViewMethod.build())
                .addMethod(unBindViewMethod.build())
                .build();
        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, injectClass).build();
    }
}
