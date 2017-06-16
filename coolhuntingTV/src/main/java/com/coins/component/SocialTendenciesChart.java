package com.coins.component;

import com.coins.util.PersonalityHelper;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.XAxis;

public class SocialTendenciesChart extends Chart {
	
	public SocialTendenciesChart() {
		super(ChartType.COLUMN);
		
		setCaption("Social Tendencies");
        getConfiguration().setTitle("");
        
        XAxis x = new XAxis();
        x.setCategories("Openess", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range");
        getConfiguration().addxAxis(x);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels
                .setFormatter("this.percentage");
        plotOptions.setDataLabels(dataLabels);
        getConfiguration().setPlotOptions(plotOptions);

	}
	
	public void buildData(String name) {
		
		PersonalityHelper helper = new PersonalityHelper();
		Double[] scores = helper.getSocialTendencies(name);
		
		final DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Openess", scores[0]));
        series.add(new DataSeriesItem("Conscientiousness", scores[1]));
        series.add(new DataSeriesItem("Extraversion", scores[2]));
        series.add(new DataSeriesItem("Agreeableness", scores[3]));
        series.add(new DataSeriesItem("Emotional Range", scores[4]));
        getConfiguration().setSeries(series);
	}

}
