package com.mrghastien.quantum_machinery.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mrghastien.quantum_machinery.common.network.GuiSynced;
import com.mrghastien.quantum_machinery.common.network.SyncedField;

public class ClassUtils {

	public static List<SyncedField<?>> getSyncableFields(Object instance) {
		ArrayList<SyncedField<?>> fields = new ArrayList<SyncedField<?>>();
		Class<?> instanceClass = instance.getClass();
		while(instanceClass != null) {
			for(Field field : instanceClass.getDeclaredFields()) {
				Annotation annotation = field.getAnnotation(GuiSynced.class);
				if(annotation != null) {
					int index = ((GuiSynced) annotation).value();
					fields.ensureCapacity(index + 1);
					fields.add(index, SyncedField.create(instance, field));
				}
				instanceClass = instanceClass.getSuperclass();
			}
		}
		return fields;
	}
}
