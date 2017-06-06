package com.coins.view;

import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

	public MainView() {
		setSizeFull();
		setSpacing(false);
		
		addComponent(new Dashboard());
	}
}
