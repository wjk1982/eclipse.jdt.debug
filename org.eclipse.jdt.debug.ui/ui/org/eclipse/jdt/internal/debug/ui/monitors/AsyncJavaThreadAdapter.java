/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui.monitors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.ui.JavaDebugUtils;

/**
 * Generates monitor information as well as stack frames
 */
public class AsyncJavaThreadAdapter extends AsyncMonitorAdapter {

	protected Object[] getChildren(Object parent, IPresentationContext context) throws CoreException {
		IJavaThread thread = (IJavaThread) parent;
		if (!thread.isSuspended()) {
			return EMPTY;
		}
		try {
			IStackFrame[] frames = thread.getStackFrames();
			if (!isDisplayMonitors()) {
				return frames;
			}

			Object[] children;
			int length = frames.length;
			if (((IJavaDebugTarget) thread.getDebugTarget()).supportsMonitorInformation()) {
				IDebugElement[] ownedMonitors = JavaDebugUtils.getOwnedMonitors(thread);
				IDebugElement contendedMonitor = JavaDebugUtils.getContendedMonitor(thread);

				if (ownedMonitors != null) {
					length += ownedMonitors.length;
				}
				if (contendedMonitor != null) {
					length++;
				}
				children = new Object[length];
				if (ownedMonitors != null && ownedMonitors.length > 0) {
					System.arraycopy(ownedMonitors, 0, children, 0, ownedMonitors.length);
				}
				if (contendedMonitor != null) {
					// Insert the contended monitor after the owned monitors
					children[ownedMonitors.length] = contendedMonitor;
				}
			} else {
				children = new Object[length + 1];
				children[0] = new NoMonitorInformationElement(thread.getDebugTarget());
			}
			int offset = children.length - frames.length;
			System.arraycopy(frames, 0, children, offset, frames.length);
			return children;
		} catch (DebugException e) {
			return EMPTY;
		}
	}

	protected boolean hasChildren(Object element, IPresentationContext context) throws CoreException {
		IJavaThread thread = (IJavaThread) element;
		return thread.hasStackFrames();
	}

}
