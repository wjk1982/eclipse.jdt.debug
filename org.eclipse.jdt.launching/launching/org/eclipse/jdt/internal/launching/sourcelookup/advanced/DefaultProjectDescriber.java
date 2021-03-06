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
package org.eclipse.jdt.internal.launching.sourcelookup.advanced;

import static org.eclipse.jdt.launching.sourcelookup.advanced.AdvancedSourceLookup.getClasspath;
import static org.eclipse.jdt.launching.sourcelookup.advanced.AdvancedSourceLookup.getOutputDirectories;
import static org.eclipse.jdt.launching.sourcelookup.advanced.AdvancedSourceLookup.isSourceProject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.sourcelookup.advanced.IWorkspaceProjectDescriber;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaProjectSourceContainer;

public class DefaultProjectDescriber implements IWorkspaceProjectDescriber {

	@Override
	public void describeProject(IJavaProject project, IJavaProjectSourceDescription description) throws CoreException {
		if (isSourceProject(project)) {
			description.addDependencies(getClasspath(project));
			getOutputDirectories(project).forEach(f -> description.addLocation(f));
			description.addSourceContainerFactory(() -> new JavaProjectSourceContainer(project));
		}
	}

}
