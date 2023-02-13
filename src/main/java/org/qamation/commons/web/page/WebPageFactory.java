package org.qamation.commons.web.page;

import org.openqa.selenium.WebDriver;
import java.lang.reflect.Constructor;


public class WebPageFactory {
	public static  <A extends Page> A createPageInstance  (String pageImplementationClass, WebDriver driver) {
		A page = (A)createInstance(pageImplementationClass, driver);
		return page;
	}

	private static Object createInstance(String className, WebDriver driver) {
		try {
			Class<?> pageClass = Class.forName(className);
			Constructor<?> cons = pageClass.getConstructor(WebDriver.class);
			Object obj = cons.newInstance(driver);
			return obj;
		}
		catch (Exception e) {
			throw new RuntimeException("Cannot create page for class "+className,e);
		}
	}
}
