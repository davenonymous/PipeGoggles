package com.davenonymous.pipegoggles.compat;

import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class AnnotationHelpers {
	public static class AnnotatedLoadException extends Exception {
		public AnnotatedLoadException(String message) {
			super(message);
		}

		public AnnotatedLoadException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static Field getSingularField(Class<?> clazz, Class<? extends Annotation> annotation, boolean mustBeStatic) throws AnnotatedLoadException {
		var fields = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(annotation)).toList();
		if(fields.isEmpty()) {
			throw new AnnotatedLoadException("No field annotated with " + annotation.getSimpleName() + " in class " + clazz.getName());
		}
		if(fields.size() > 1) {
			throw new AnnotatedLoadException("Multiple fields annotated with " + annotation.getSimpleName() + " in class " + clazz.getName());
		}

		var field = fields.getFirst();
		if(mustBeStatic && !Modifier.isStatic(field.getModifiers())) {
			throw new AnnotatedLoadException("Field annotated with " + annotation.getSimpleName() + " in class " + clazz.getName() + " is not static");
		}

		return field;
	}

	public static Method getSingularMethod(Class<?> clazz, Class<? extends Annotation> annotation, boolean mustBeStatic) throws AnnotatedLoadException {
		var methods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(annotation)).toList();
		if(methods.isEmpty()) {
			throw new AnnotatedLoadException("No method annotated with " + annotation.getSimpleName() + " in class " + clazz.getName());
		}
		if(methods.size() > 1) {
			throw new AnnotatedLoadException("Multiple methods annotated with " + annotation.getSimpleName() + " in class " + clazz.getName());
		}

		var field = methods.getFirst();
		if(mustBeStatic && !Modifier.isStatic(field.getModifiers())) {
			throw new AnnotatedLoadException("Method annotated with " + annotation.getSimpleName() + " in class " + clazz.getName() + " is not static");
		}

		return field;
	}

	public static <T> T getSingularFieldData(Class<?> clazz, Class<? extends Annotation> annotation, Class<T> fieldType) throws AnnotatedLoadException {
		var field = getSingularField(clazz, annotation, true);
		if(!fieldType.isAssignableFrom(field.getType())) {
			throw new AnnotatedLoadException("Field annotated with " + annotation.getSimpleName() + " in class " + clazz.getName() + " is not of type " + fieldType.getName());
		}
		T value;
		try {
			//noinspection unchecked
			value = (T)field.get(null);
		} catch (IllegalAccessException e) {
			throw new AnnotatedLoadException("Field annotated with " + annotation.getSimpleName() + " in class " + clazz.getName() + " could not be accessed", e);
		}

		return value;
	}

	public static <T> Class<T> getClassFromAnnotationData(Object annotationData, Class<T> expected) throws AnnotatedLoadException {
		if(!(annotationData instanceof Type type)) {
			throw new AnnotatedLoadException("Annotation data is not a class type");
		}

		Class<T> clazz;
		try {
			//noinspection unchecked
			clazz = (Class<T>) Class.forName(type.getClassName());
		} catch (ClassNotFoundException e) {
			throw new AnnotatedLoadException("Annotation data class could not be found: " + type.getClassName(), e);
		}

		if(!expected.isAssignableFrom(clazz)) {
			throw new AnnotatedLoadException("Annotation data class " + clazz.getName() + " is not of expected type " + expected.getName());
		}

		return clazz;
	}

	public static <T> Class<T> getAnnotatedClass(ModFileScanData.AnnotationData annotationData, Class<T> expected) throws AnnotatedLoadException {
		var sensorClassType = annotationData.clazz();
		var sensorClassClassName = sensorClassType.getClassName();
		Class<?> sensorClazz;
		try {
			sensorClazz = Class.forName(sensorClassClassName);
		} catch (ClassNotFoundException e) {
			throw new AnnotatedLoadException("Class " + sensorClassClassName + " could not be found!", e);
		}

		if(!expected.isAssignableFrom(sensorClazz)) {
			throw new AnnotatedLoadException("Class " + sensorClassClassName + " does not implement the " + expected.getSimpleName() + " interface!");
		}

		//noinspection unchecked
		return (Class<T>) sensorClazz;
	}

}
