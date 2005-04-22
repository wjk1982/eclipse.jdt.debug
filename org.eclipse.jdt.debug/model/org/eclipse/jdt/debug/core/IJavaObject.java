/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.core;


import org.eclipse.debug.core.DebugException;

/**
 * A value referencing an object on a target VM.
 * <p>
 * Clients are not intended to implement this interface.
 * </p>
 * @see IJavaValue
 * @since 2.0
 */
public interface IJavaObject extends IJavaValue {
	
	/**
	 * Returns the result of sending the specified message to this object
	 * with the given arguments in the specified thread. The given
	 * thread is resumed to perform the method invocation. The thread will suspend
	 * in its original location when the method invocation is complete. This
	 * method does not return until the method invocation is complete.
	 * Invoking a method in the target VM can result in breakpoints being
	 * hit, infinite loops, and deadlock.
	 * 
	 * @param selector the selector of the method to be invoked
	 * @param signature the JNI style signature of the method to be invoked
	 * @param args the arguments of the method, which can be
	 * 	<code>null</code> or emtpy if there are none
	 * @param thread the thread in which to invoke the method
	 * @param superSend <code>true</code> if the method lookup should 
	 *  begin in this object's superclass
	 * @return the result of invoking the method
	 * @exception DebugException if this method fails. Reasons include:<ul>
	 * <li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li>
	 * <li>This object does not implement the specified method</li>
	 * <li>An exception occurs while invoking the specified method</li>
	 * <li>The given thread is already performing a message send,
	 * 	(status code <code>IJavaThread.ERR_NESTED_METHOD_INVOCATION</code>)</li>
	 * <li>The given thread is not currently suspended
	 *  (status code <code>IJavaThread.ERR_THREAD_NOT_SUSPENDED</code>)</li>
	 * <li>The given thread was explicitly suspended
	 *  (status code <code>IJavaThread.ERR_INCOMPATIBLE_THREAD_STATE</code>)</li>
	 * </ul>
	 */
	public IJavaValue sendMessage(String selector, String signature, IJavaValue[] args, IJavaThread thread, boolean superSend) throws DebugException;	
	/**
	 * Returns the result of sending the specified message on the specified declaring type
	 * to this object with the given arguments in the specified thread. The given
	 * thread is resumed to perform the method invocation. The thread will suspend
	 * in its original location when the method invocation is complete. This
	 * method does not return until the method invocation is complete.
	 * Invoking a method in the target VM can result in breakpoints being
	 * hit, infinite loops, and deadlock.
	 * 
	 * @param selector the selector of the method to be invoked
	 * @param signature the JNI style signature of the method to be invoked
	 * @param args the arguments of the method, which can be
	 * 	<code>null</code> or emtpy if there are none
	 * @param thread the thread in which to invoke the method
	 * @param typeSignature the signature of the type in which the method
	 *  is defined or <code>null</code> if the method should be invoked normally using
	 *  polymorphism
	 * @return the result of invoking the method
	 * @exception DebugException if this method fails. Reasons include:<ul>
	 * <li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li>
	 * <li>This object does not implement the specified method</li>
	 * <li>An exception occurs while invoking the specified method</li>
	 * <li>The given thread is already performing a message send,
	 * 	(status code <code>IJavaThread.ERR_NESTED_METHOD_INVOCATION</code>)</li>
	 * <li>The given thread is not currently suspended
	 *  (status code <code>IJavaThread.ERR_THREAD_NOT_SUSPENDED</code>)</li>
	 * <li>The given thread was explicitly suspended
	 *  (status code <code>IJavaThread.ERR_INCOMPATIBLE_THREAD_STATE</code>)</li>
	 * </ul>
	 * @since 2.0.1
	 */
	public IJavaValue sendMessage(String selector, String signature, IJavaValue[] args, IJavaThread thread, String typeSignature) throws DebugException;
	/**
	 * Returns a variable representing the field in this object
	 * with the given name, or <code>null</code> if there is no
	 * field with the given name, or the name is ambiguous.
	 * 
	 * @param name field name
	 * @param superField whether or not to get the field in the superclass
	 *  of this objects.
	 * @return the variable representing the field, or <code>null</code>
	 * @exception DebugException if this method fails.  Reasons include:
	 * <ul><li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li>
	 */
	public IJavaFieldVariable getField(String name, boolean superField) throws DebugException;
	/**
	 * Returns a variable representing the field in this object
	 * with the given name declared in the type with the given
	 * signature, or <code>null</code> if there is no
	 * field with the given name, or the name is ambiguous.
	 * 
	 * @param name field name
	 * @param typeSignature the signature of the type in which the field
	 *  is defined
	 * @return the variable representing the field, or <code>null</code>
	 * @exception DebugException if this method fails.  Reasons include:
	 * <ul><li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li>
	 */
	public IJavaFieldVariable getField(String name, String typeSignature) throws DebugException;
	
	/**
	 * Returns the threads waiting for the monitor associated to this object, or
	 * <code>null</code> if no thread is waiting for the monitor.
	 * 
	 * @return the thread waiting for the monitor, or <code>null</code>.
	 * @exception DebugException if this method fails.  Reasons include:
	 * <ul><li>The VM is not able to retrieve the monitor information</li>
	 * <li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li></ul>
	 * @since 3.1
	 */
	public IJavaThread[] getWaitingThreads() throws DebugException;
	
	/**
	 * Returns the threads which owns for the monitor associated to this object, or
	 * <code>null</code> if no thread owns the monitor.
	 * 
	 * @return the thread which owns the monitor, or <code>null</code>.
	 * @exception DebugException if this method fails.  Reasons include:
	 * <ul><li>The VM is not able to retrieve the monitor information</li>
	 * <li>Failure communicating with the VM.  The DebugException's
	 * status code contains the underlying exception responsible for
	 * the failure.</li></ul>
	 * @since 3.1
	 */
	public IJavaThread getOwningThread() throws DebugException;
}

