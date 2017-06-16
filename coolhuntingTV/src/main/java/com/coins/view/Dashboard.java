package com.coins.view;

import com.coins.component.PersonalityChart;
import com.coins.component.SocialTendenciesChart;
import com.coins.component.ConsumerNeeds;
import com.coins.component.EmotionChart;
import com.coins.model.TvSerie;
import com.coins.services.TvSeriesService;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dashboard extends Panel implements View {

	public static final String TITLE_ID = "dashboard-title";
	public static final String COMBOBOX = "dashboard-combobox";

	private TvSeriesService service = TvSeriesService.getInstance();
	private final VerticalLayout root;

	private CssLayout dashboardPanels;
	private ComboBox<TvSerie> selectSeries;
	private Label titleLabel;
	private ConsumerNeeds consumerNeeds;
	private PersonalityChart personalityChart;
	private EmotionChart emotionChart;
	private SocialTendenciesChart socialTendenciesChart;

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

		HorizontalLayout toolbar = new HorizontalLayout();

		toolbar.addStyleName("viewheader");

		titleLabel = new Label("Coolhunting Tv Series - <strong>Team 11</strong>", ContentMode.HTML);
		titleLabel.setId(TITLE_ID);
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		toolbar.addComponent(titleLabel);

		selectSeries = new ComboBox<>();
		selectSeries.setSizeUndefined();
		selectSeries.addStyleName(ValoTheme.LABEL_H2);
		selectSeries.addStyleName(ValoTheme.LABEL_NO_MARGIN);
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
				updateCharts(selectSeries.getValue());
			}
		});

		return toolbar;
	}

	protected void updateCharts(TvSerie value) {

		consumerNeeds.buildData(value.getName());
		consumerNeeds.drawChart();

		personalityChart.buildData(value.getName());
		personalityChart.drawChart();
		
		emotionChart.buildData(value.getName());
		emotionChart.drawChart();
		
		socialTendenciesChart.buildData(value.getName());
		socialTendenciesChart.drawChart();
		
	}

	private void buildData(TvSerie value) {

	}

	private void setData() {
		//TvSerie serie = new TvSerie("Game of Thrones", "123");
		//selectSeries.setItems(serie);
		selectSeries.setItems(service.getSeries().values());
	}

	private Component buildCharts() {

		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		dashboardPanels.addComponent(buildPersonalityChart());
		dashboardPanels.addComponent(buildConsumerNeeds());
		
		dashboardPanels.addComponent(buildEmotionChart());
		dashboardPanels.addComponent(socialTendenciesChart());

		return dashboardPanels;
	}

	private Component socialTendenciesChart() {
		socialTendenciesChart = new SocialTendenciesChart();
		socialTendenciesChart.setSizeFull();
		return createContentWrapper(socialTendenciesChart);
	}

	private Component buildEmotionChart() {
		emotionChart = new EmotionChart();
		emotionChart.setSizeFull();
		return createContentWrapper(emotionChart);
	}

	private Component buildPersonalityChart() {
		personalityChart = new PersonalityChart();
		personalityChart.setSizeFull();
		return createContentWrapper(personalityChart);
	}

	private Component buildConsumerNeeds() {
		consumerNeeds = new ConsumerNeeds();
		consumerNeeds.setSizeFull();
		return createContentWrapper(consumerNeeds);
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

		MenuBar about = new MenuBar();
		about.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

		MenuItem help = about.addItem("", FontAwesome.COG, null);
		help.addItem("About", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
			}
		});

		toolbar.addComponents(caption, about);
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
