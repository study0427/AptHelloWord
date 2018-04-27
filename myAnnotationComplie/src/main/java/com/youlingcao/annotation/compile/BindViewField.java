package com.youlingcao.annotation.compile;

import com.youlingcao.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class BindViewField {
    private VariableElement mVariableElement;
    private int mResId;

    public BindViewField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    BindView.class.getSimpleName()));
        }
        this.mVariableElement = (VariableElement) element;
        BindView bindView = mVariableElement.getAnnotation(BindView.class);
        mResId = bindView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(String.format("value() in %s for field %s is not valid !",
                    BindView.class.getSimpleName(), mVariableElement.getSimpleName()));
        }
    }

    Name getFieldName() {
        return mVariableElement.getSimpleName();
    }

    int getResId() {
        return mResId;
    }

    TypeMirror getFieldType() {
        return mVariableElement.asType();
    }
}
