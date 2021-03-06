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
package org.jboss.reddeer.junit.internal.runner.statement;

import org.jboss.reddeer.common.logging.Logger;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Runs the test method and screates screenshot upon unexpected failure. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunTestMethod extends AbstractStatementWithScreenshot {
	
	private static final Logger log = Logger.getLogger(RunTestMethod.class);

    /**
     * Instantiates a new run test method.
     *
     * @param config the config
     * @param testClass the test class
     * @param testMethod the test method
     * @param target the target
     */
    public RunTestMethod(String config, TestClass testClass, FrameworkMethod testMethod, Object target) {
    	super(config, null, testClass, testMethod, target);
    }

    /* (non-Javadoc)
     * @see org.junit.runners.model.Statement#evaluate()
     */
    @Override
    public void evaluate() throws Throwable {
    	try{
    		frameworkMethod.invokeExplosively(target);	
    	} catch (Throwable t){
    		Test annotation = (Test) frameworkMethod.getAnnotations()[0];
    		if (annotation.expected().getName().equals("org.junit.Test$None") ||
    			!annotation.expected().isAssignableFrom(t.getClass())) {
    				log.error("Test " + target.getClass().getName() 
    					+ "." + frameworkMethod.getName()
    					+ " throws exception: ",t);
	    			createScreenshot();
    		}
    		throw t;
    	}
    }
}