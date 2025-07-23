package com.davenonymous.pipegoggles.setup;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.stream.Collectors;

public class ClassInheritances {
	Map<String, Set<String>> classToParents;
	Map<String, Set<String>> parentToChildren;

	public ClassInheritances() {
		this.classToParents = new HashMap<>();
		this.parentToChildren = new HashMap<>();

		for(ModFileScanData scanData : ModList.get().getAllScanData()) {
			for (var classData : scanData.getClasses()) {
				String className = classData.clazz().getClassName();
				String parentName = classData.parent().getClassName();
				Set<String> interfaces = classData.interfaces().stream().map(Type::getClassName).collect(Collectors.toSet());

				var parentSet = classToParents.computeIfAbsent(className, k -> new HashSet<>());
				parentSet.add(parentName);
				parentSet.addAll(interfaces);

				var childrenSet = parentToChildren.computeIfAbsent(parentName, k -> new HashSet<>());
				childrenSet.add(className);
			}
		}
	}

	public boolean doesInherit(ModFileScanData.ClassData classData, String parentClassName) {
		String className = classData.clazz().getClassName();
		if (!classToParents.containsKey(className)) {
			return false;
		}

		Queue<String> toCheck = new LinkedList<>(classToParents.get(className));
		Set<String> visited = new HashSet<>();
		while (!toCheck.isEmpty()) {
			String current = toCheck.poll();
			if (current.equals(parentClassName)) {
				return true;
			}
			if (visited.contains(current)) {
				continue;
			}
			visited.add(current);
			if (classToParents.containsKey(current)) {
				toCheck.addAll(classToParents.get(current));
			}
		}

		return false;
	}
}
