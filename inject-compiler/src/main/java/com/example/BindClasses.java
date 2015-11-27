package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.reflect.TypeVariable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by nnero on 15/11/26.
 */
public class BindClasses {

  private static final ClassName FINDER = ClassName.get("percent.test.android.injector","Finder");
  private static final ClassName ACTIVITY = ClassName.get("android.app","Activity");
  private static final ClassName VIEW = ClassName.get("android.view","View");
  private static final ClassName VIEW_BIND = ClassName.get("percent.test.android.injector","ViewBind");

  private static       Map<Integer, FieldInfo> targetClassMap = new LinkedHashMap<>();

  private String className;
  private String packageName;
  private String targetType;

  public BindClasses(String className,String packageName,String targetType) {
    this.className = className;
    this.targetType = targetType;
    this.packageName = packageName;
  }

  public void addViewBind(int id,FieldInfo fieldInfo) {
    targetClassMap.put(id,fieldInfo);
  }

  public void brewJavaSourceFile(Filer filer) throws IOException, ClassNotFoundException {
    TypeSpec result = TypeSpec.classBuilder(className)
        .addTypeVariable(TypeVariableName.get("T",ClassName.bestGuess(targetType)))
        .addSuperinterface(ParameterizedTypeName.get(VIEW_BIND, TypeVariableName.get("T")))
        .addModifiers(Modifier.PUBLIC)
        .addMethod(createMethods())
        .build();
    JavaFile.builder(packageName,result).build().writeTo(filer);
  }

  private MethodSpec createMethods() throws ClassNotFoundException {
    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(void.class)
        .addParameter(TypeVariableName.get("T"), "source")
        .addParameter(FINDER, "finder");
    methodBuilder.addStatement("$T view",VIEW);
    for(int id : targetClassMap.keySet()) {
      methodBuilder.addStatement("view = finder.findView(source,$L)", id);
      methodBuilder.addStatement("source.$L = finder.castView(view)",targetClassMap.get(id).getName());
    }
    return methodBuilder.build();
  }
}
