package common.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TheUnsafe {
	private static final Unsafe unsafe;
	
	static {
		Unsafe theUnsafe = null;
		try {
			for (Field declaredField : Unsafe.class.getDeclaredFields()) {
				try {
					declaredField.setAccessible(true);
					theUnsafe = (Unsafe) declaredField.get(null);
				} catch (Throwable ignored) {
				}
			}
		} catch (Throwable ignored) {
			throw new RuntimeException("Failed to get an instance of Unsafe.");
		}
		unsafe = theUnsafe;
	}
	
	public static void throwUnchecked(Throwable err) {
		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
		ArrayList<StackTraceElement> elements = new ArrayList<>();
		for (StackTraceElement traceElement : traceElements) {
			if (!traceElement.getClassName().equals(TheUnsafe.class.getName())) {
				elements.add(traceElement);
			}
		}
		err.setStackTrace(elements.toArray(new StackTraceElement[0]));
		unsafe.throwException(err);
	}
}
