package com.coins.component;

import java.util.ArrayList;
import java.util.List;

import com.coins.util.PersonalityHelper;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;

@SuppressWarnings("serial")
public class ConsumerNeeds extends Chart {

	public ConsumerNeeds() {
		super(ChartType.BAR);

		setCaption("Consumer Needs");
        getConfiguration().setTitle("");

        XAxis x = new XAxis();
        x.setCategories("Harmony", "Curiosity", "Stability", "Self-expression",
				"Closeness");
        x.setTitle((String) null);
        getConfiguration().addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle title = new AxisTitle("Percentage %");
        title.setAlign(VerticalAlign.MIDDLE);
        y.setTitle(title);
        getConfiguration().addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.y +' %'");
        getConfiguration().setTooltip(tooltip);

        PlotOptionsBar plot = new PlotOptionsBar();
        plot.setDataLabels(new DataLabels(true));
        getConfiguration().setPlotOptions(plot);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-100);
        legend.setY(100);
        legend.setFloating(true);
        legend.setBorderWidth(1);
        legend.setBackgroundColor(new SolidColor("#FFFFFF"));
        legend.setShadow(true);
        legend.setEnabled(false);
        getConfiguration().setLegend(legend);

        getConfiguration().disableCredits();

        List<Series> series = new ArrayList<Series>();
        series.add(new ListSeries("Percentage", 0, 0, 0, 0, 0));
        getConfiguration().setSeries(series);

	}
	
	public void buildData(String value) {
		PersonalityHelper helper = new PersonalityHelper();
		ListSeries line1 = helper.getConsumerNeeds(value);
		getConfiguration().setSeries(line1);
	}
}
