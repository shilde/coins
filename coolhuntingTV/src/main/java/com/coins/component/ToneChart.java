package com.coins.component;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;

@SuppressWarnings("serial")
public class ToneChart extends Chart {

	public ToneChart() {
		super(ChartType.PIE);
		
		setCaption("Tone Analysis");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.PIE);
		getConfiguration().getChart().setAnimation(false);
		getConfiguration().getxAxis().getLabels().setEnabled(false);
		getConfiguration().getyAxis().setTitle("");
		setSizeFull();
		
		PlotOptionsPie options = new PlotOptionsPie();
		options.setInnerSize("50px");
		options.setSize("75%");
		options.setCenter("50%", "50%");
		getConfiguration().setPlotOptions(options);

	}
	
	public void buildData() {
		DataSeries series = new DataSeries();
		series.add(new DataSeriesItem("35%", 35));
		series.add(new DataSeriesItem("40%", 40));
		DataSeriesItem s25 = new DataSeriesItem("25%", 25);
		s25.setSliced(true);
		series.add(s25);
		getConfiguration().addSeries(series);
	}
}
