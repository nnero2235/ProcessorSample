package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import annotations.interal.Bind;

@AutoService(Processor.class)
public class InjectProcessor extends AbstractProcessor{

  private Elements elementUtils;
  private Types    typeUtils;
  private Filer    filer;
  private Messager messager;

  private static final String CLASS_NAME_STUFF = "$$Injector";

//  private static final  = new LinkedHashMap<>();

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    elementUtils = env.getElementUtils();
    typeUtils = env.getTypeUtils();
    messager = env.getMessager();
    filer = env.getFiler();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> types = new LinkedHashSet<>();
    types.add(Bind.class.getCanonicalName());
    return types;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Map<TypeElement, BindClasses> sourceFiles = parseAnnotations(roundEnv);
    for (Map.Entry<TypeElement,BindClasses> entry: sourceFiles.entrySet()) {
      BindClasses bindClasses = entry.getValue();

      try {
        bindClasses.brewJavaSourceFile(filer);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  private Map<TypeElement,BindClasses> parseAnnotations(RoundEnvironment env){
    Map<TypeElement,BindClasses> targetMap = new LinkedHashMap<>();
    for(Element element : env.getElementsAnnotatedWith(Bind.class)){
      TypeElement typeElement = (TypeElement) element.getEnclosingElement();
      BindClasses bindClasses = createBindClasses(targetMap,typeElement);
      int id = element.getAnnotation(Bind.class).value();
      String name = element.getSimpleName().toString();
      TypeName typeName = TypeName.get(element.asType());
      FieldInfo fieldInfo = new FieldInfo(name,typeName);
      bindClasses.addViewBind(id,fieldInfo);
    }
    return targetMap;
  }

  private BindClasses createBindClasses(Map<TypeElement,BindClasses> targetMap,TypeElement element){
    BindClasses bindClasses = targetMap.get(element);
    if(bindClasses == null){
      String targetType = element.getQualifiedName().toString();
      String packageName = getPackageName(element);
      String className = getClassName(element,packageName) + CLASS_NAME_STUFF;
      bindClasses = new BindClasses(className,packageName,targetType);
      targetMap.put(element,bindClasses);
    }
    return bindClasses;
  }

  private String getClassName(TypeElement type,String packageName){
    int len = packageName.length() + 1;
    return type.getQualifiedName().toString().substring(len);
  }

  private String getPackageName(TypeElement type){
    return elementUtils.getPackageOf(type).getQualifiedName().toString();
  }


}
