/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.reddeer.eclipse.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.jboss.reddeer.ui.Activator;

/**
 * Wizard for creating new RedDeer Test case. Wizard is based on JUnit Test Case wizard. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class NewRedDeerTestCaseWizard extends Wizard implements INewWizard {

	private NewRedDeerTestWizardPageOne wizardPageOne;

	private NewRedDeerTestWizardPageTwo wizardPageTwo;

	private IWorkbench workbench;

	private IStructuredSelection selection;

	/**
	 * Creates new instance of RedDeer Test Case wizard
	 */
	public NewRedDeerTestCaseWizard() {
		setWindowTitle("New RedDeer Test Case");
	}

	/**
	 * Adds all appropriate wizard pages.
	 */
	public void addPages() {
		wizardPageTwo = new NewRedDeerTestWizardPageTwo();
		wizardPageOne = new NewRedDeerTestWizardPageOne(wizardPageTwo);
		wizardPageOne.init(selection);

		addPage(wizardPageOne);
		addPage(wizardPageTwo);
	}

	/**
	 * Creates the Red Deer Test Case class
	 * 
	 * @see {@link org.eclipse.jface.wizard.performFinish}
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress runnable= wizardPageOne.getRunnable();

		if (finishPage(runnable)){
			IType newClass= wizardPageOne.getCreatedType();
			IResource resource= newClass.getCompilationUnit().getResource();
			
			if (resource != null) {
				selectAndReveal(resource);
				openResource(resource);
			}
			return true;	
		}
		return false;
	}

	protected boolean finishPage(IRunnableWithProgress runnable) {
		IRunnableWithProgress op= new WorkspaceModifyDelegatingOperation(runnable);
		try {
			PlatformUI.getWorkbench().getProgressService().runInUI(getContainer(), op, ResourcesPlugin.getWorkspace().getRoot());
		} catch (InvocationTargetException e) {
			Activator.log(e);
			return false;
		} catch  (InterruptedException e) {
			Activator.log(e);
			return false;
		}
		return true;
	}

	protected void  selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, workbench.getActiveWorkbenchWindow());
	}

	protected void openResource(final IResource resource) {
		if (resource.getType() == IResource.FILE) {
			final IWorkbenchPage activePage= workbench.getActiveWorkbenchWindow().getActivePage();
			if (activePage != null) {
				final Display display= Display.getDefault();
				if (display != null) {
					display.asyncExec(new Runnable() {
						public void run() {
							try {
								IDE.openEditor(activePage, (IFile)resource, true);
							} catch (PartInitException e) {
								Activator.log(e, "Cannot open newly created resource");
							}
						}
					});
				}
			}
		}
	}

	/**
	 * @see {@link org.eclipse.ui.iWorkbenchWizard.init}
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}
}
