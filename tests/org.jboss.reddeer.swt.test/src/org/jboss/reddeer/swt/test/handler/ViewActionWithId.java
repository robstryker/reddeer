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
package org.jboss.reddeer.swt.test.handler;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.ActionDelegate;

public class ViewActionWithId extends ActionDelegate implements IViewActionDelegate {

	

	private static boolean toggle = false;
	
	public void init(IViewPart view) {
	}

	@Override
	public void run(IAction action) {
		toggle=true;
	}
	
	public static boolean isToggled() {
		return toggle;
	}

}
