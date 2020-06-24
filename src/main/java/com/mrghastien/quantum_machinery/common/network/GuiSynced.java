package com.mrghastien.quantum_machinery.common.network;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author MrGhastien
 */

@Retention(RUNTIME)
@Target(FIELD)
public @interface GuiSynced {
	short value();
}
