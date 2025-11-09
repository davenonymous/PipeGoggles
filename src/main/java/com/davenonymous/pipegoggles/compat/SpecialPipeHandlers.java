package com.davenonymous.pipegoggles.compat;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.neoforged.fml.ModList;

import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SpecialPipeHandlers {
	private static final Map<String, ISpecialPipeHandler> HANDLERS = new HashMap<>();

	public static void find() throws AnnotationHelpers.AnnotatedLoadException {
		HANDLERS.clear();

		for(var scanData : ModList.get().getAllScanData()) {
			for(var annotationData : scanData.getAnnotatedBy(SpecialPipeHandler.class, ElementType.TYPE).toList()) {
				String modId = annotationData.annotationData().get("value").toString();
				if(!ModList.get().isLoaded(modId)) {
					continue;
				}

				Class<ISpecialPipeHandler> clazz = AnnotationHelpers.getAnnotatedClass(annotationData, ISpecialPipeHandler.class);

				try {
					ISpecialPipeHandler instance = clazz.getDeclaredConstructor().newInstance();
					HANDLERS.put(modId, instance);
					PipeGoggles.LOGGER.info("Loaded special pipe handler for mod {}: {}", modId, clazz.getSimpleName());
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
					throw new AnnotationHelpers.AnnotatedLoadException("Error loading special pipe handler for mod " + modId, e);
				} catch (NoSuchMethodException e) {
					throw new AnnotationHelpers.AnnotatedLoadException("Missing argument-less constructor in special pipe handler for mod " + modId, e);
				}
			}
		}
	}

	public static ISpecialPipeHandler forMod(String modId) {
		if(!ModList.get().isLoaded(modId)) {
			return ISpecialPipeHandler.NOOP;
		}

		if(!HANDLERS.containsKey(modId)) {
			return ISpecialPipeHandler.NOOP;
		}

		return HANDLERS.get(modId);
	}
}
