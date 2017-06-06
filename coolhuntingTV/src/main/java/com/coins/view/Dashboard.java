package com.coins.view;

import com.coins.component.PersonalityInsights;
import com.coins.component.ToneChart;
import com.coins.model.TvSerie;
import com.coins.services.TvSeriesService;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import fi.vtt.RVaadin.RContainer;

@SuppressWarnings("serial")
public class Dashboard extends Panel implements View {

	public static final String TITLE_ID = "dashboard-title";
	public static final String COMBOBOX = "dashboard-combobox";

	private TvSeriesService service = TvSeriesService.getInstance();
	private final VerticalLayout root;

	private CssLayout dashboardPanels;
	private ComboBox<TvSerie> selectSeries;
	private Label titleLabel;
	private ToneChart toneChart;
	private PersonalityInsights personalityInsights;

	public Dashboard() {

		addStyleName(ValoTheme.PANEL_BORDERLESS);
		setSizeFull();

		root = new VerticalLayout();
		root.setSizeFull();
		root.setSpacing(false);
		root.addStyleName("dashboard-view");
		setContent(root);
		
		root.addComponent(buildHeader());

		Component content = buildCharts();
		root.addComponent(content);

		root.setExpandRatio(content, 1);

		setData();
	}
	

	private Component buildHeader() {
		
		HorizontalLayout header = new HorizontalLayout();
		header.setPrimaryStyleName("valo-menu");
		header.setSizeUndefined();
		header.addStyleName("sidebar");
		header.addStyleName(ValoTheme.MENU_PART);
		header.addStyleName("no-vertical-drag-hints");
        header.addStyleName("no-horizontal-drag-hints");
		header.setWidth("100%");
		header.setHeight(null);
		
		titleLabel = new Label("Coolhunting Tv Series - <strong>Team 11</strong>", ContentMode.HTML);
		titleLabel.setId(TITLE_ID);
		titleLabel.setSizeUndefined();
		//titleLabel.addStyleName(ValoTheme.LABEL_H1);
		//titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		
		HorizontalLayout logoWrapper = new HorizontalLayout(titleLabel);
        logoWrapper.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);

		header.addComponents(titleLabel, buildToolbar());
		
		return header;
	}

	private Component buildToolbar() {
		
		HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("toolbar");
        
		selectSeries = new ComboBox<>();
		selectSeries.setItemCaptionGenerator(TvSerie::getName);
		selectSeries.addShortcutListener(new ShortcutListener("View", KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				buildData(selectSeries.getValue());
			}

		});

		final Button view = new Button("View");
		view.setEnabled(false);
		view.addStyleName(ValoTheme.BUTTON_PRIMARY);

		CssLayout group = new CssLayout(selectSeries, view);
		group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		toolbar.addComponent(group);

		selectSeries.addSelectionListener(event -> view.setEnabled(event.getValue() != null));

		view.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				drawCharts(selectSeries.getValue());
			}
		});

		return toolbar;
	}

	protected void drawCharts(TvSerie value) {
		
		DataSeries series = new DataSeries();
		series.add(new DataSeriesItem("35%", 35));
		series.add(new DataSeriesItem("40%", 40));
		DataSeriesItem s25 = new DataSeriesItem("25%", 25);
		s25.setSliced(true);
		series.add(s25);
		toneChart.getConfiguration().addSeries(series);
		toneChart.drawChart();
		
		personalityInsights.buildData();
		
		personalityInsights.getConfiguration().addSeries(series);
		personalityInsights.drawChart();
	}

	private void buildData(TvSerie value) {

	}
	
	private void setData() {
		selectSeries.setItems(service.getSeries().values());
	}

	private Component buildCharts() {

		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		dashboardPanels.addComponent(buildToneAnalyzer());
		dashboardPanels.addComponent(buildPersonalityInsights());
		
		return dashboardPanels;
	}

	private Component buildPersonalityInsights() {
		personalityInsights = new PersonalityInsights();
		return createContentWrapper(personalityInsights);
	}

	private Component buildToneAnalyzer() {
		toneChart = new ToneChart();
		return createContentWrapper(toneChart);
	}

	private Component createContentWrapper(final Component content) {
		final CssLayout slot = new CssLayout();
		slot.setWidth("100%");
		slot.addStyleName("dashboard-panel-slot");

		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");
		toolbar.setSpacing(false);

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		toolbar.addComponent(caption);
		toolbar.setExpandRatio(caption, 1);
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

		card.addComponents(toolbar, content);
		slot.addComponent(card);

		return slot;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}
