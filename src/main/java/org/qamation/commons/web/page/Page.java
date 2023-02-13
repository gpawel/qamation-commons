package org.qamation.commons.web.page;

import org.openqa.selenium.support.ui.ExpectedCondition;

public interface Page {
	void openPage(String url);
	void refresh();
	void goBack(String url);
	String readTextFrom(String location);
	String readTextFrom(String location, int length);
	String getSource();
	boolean isReady();
	boolean isReady(ExpectedCondition condition);
}
