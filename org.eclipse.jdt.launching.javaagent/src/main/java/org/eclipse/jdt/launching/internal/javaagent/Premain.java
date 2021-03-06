/*******************************************************************************
 * Copyright (c) 2011-2016 Igor Fedorenko
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Igor Fedorenko - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.launching.internal.javaagent;

import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.eclipse.jdt.launching.internal.weaving.ClassfileTransformer;
import org.objectweb.asm.ClassReader;

public class Premain {
	private static final ClassfileTransformer transformer = new ClassfileTransformer();

	public static void premain(final String agentArgs, final Instrumentation inst) {
		final boolean debuglog = "debuglog".equals(agentArgs); //$NON-NLS-1$

		// disable instrumentation if ASM is not able to read Object.class definition
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream("java/lang/Object.class"); //$NON-NLS-1$
			try {
				new ClassReader(is);
			}
			finally {
				is.close();
			}
		}
		catch (Exception e) {
			String vendor = System.getProperty("java.vendor"); //$NON-NLS-1$
			String version = System.getProperty("java.version"); //$NON-NLS-1$
			System.err.printf("JRE %s/%s is not supported, advanced source lookup disabled: %s.\n", vendor, version, e.getMessage()); //$NON-NLS-1$
			if (debuglog) {
				e.printStackTrace(System.err);
			}
			return;
		}

		inst.addTransformer(new ClassFileTransformer() {
			public byte[] transform(ClassLoader loader, final String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				try {
					if (protectionDomain == null) {
						return null;
					}

					if (className == null) {
						return null;
					}

					final CodeSource codeSource = protectionDomain.getCodeSource();
					if (codeSource == null) {
						return null;
					}

					final URL locationUrl = codeSource.getLocation();
					if (locationUrl == null) {
						return null;
					}

					final String location = locationUrl.toExternalForm();

					return transformer.transform(classfileBuffer, location);
				}
				catch (Exception e) {
					System.err.printf("Could not instrument class %s: %s.\n", className, e.getMessage()); //$NON-NLS-1$
					if (debuglog) {
						e.printStackTrace(System.err);
					}
				}
				return null;
			}
		});

		if (debuglog) {
			System.err.println("Advanced source lookup enabled."); //$NON-NLS-1$
		}
	}
}
