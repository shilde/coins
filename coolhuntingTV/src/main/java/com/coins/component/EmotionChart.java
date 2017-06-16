package com.coins.component;

import com.coins.util.PersonalityHelper;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;

public class EmotionChart extends Chart {
	
	public EmotionChart() {
		super(ChartType.PIE);

		setCaption("Emotion");
        getConfiguration().setTitle("");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels
                .setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage");
        plotOptions.setDataLabels(dataLabels);
        getConfiguration().setPlotOptions(plotOptions);

	}
	
	public void buildData(String name) {
		
		PersonalityHelper helper = new PersonalityHelper();
		Double[] scores = helper.getEmotion(name);
		
		final DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Anger", scores[0]));
        series.add(new DataSeriesItem("Disguit", scores[1]));
        series.add(new DataSeriesItem("Fear", scores[2]));
        series.add(new DataSeriesItem("Joy", scores[3]));
        series.add(new DataSeriesItem("Sadness", scores[4]));
        getConfiguration().setSeries(series);
	}

}
