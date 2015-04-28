package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShopItem;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;


public class StatisticsFragment extends Fragment {
    private XYPlot plot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //here you can accses R or in on Created with view
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        plot = (XYPlot) view.findViewById(R.id.mySimpleXYPlot);

        // initialize our XYPlot reference:
        DatabaseHelper db = new DatabaseHelper(view.getContext());
        List<ShopItem> ls = db.getShoppingLists();
        Number[] series1Numbers = new Number[ls.size()*2];


       /* for(ShopItem s: ls){
            Log.d("Stat", "stat: " + Integer.parseInt(s.getDate().split("/")[0]));
            if(ls.contains(Integer.parseInt(s.getDate().split("/")[0]))){
                int index =series1Numbers.indexOf(Integer.parseInt(s.getDate().split("/")[0]));
                Number i = series1Numbers.get(index+1);
               series1Numbers.set(index+1,(double)i+s.getmCO2());
            }else{
               series1Numbers.add(Integer.parseInt(s.getDate().split("/")[0]));
               series1Numbers.add(s.getmCO2());
            }*/
        for(int i=0 ;i<ls.size();i++ ){
            if(Arrays.asList(series1Numbers).contains(Integer.parseInt(ls.get(i).getDate().split("/")[0]))){
                int index = Arrays.asList(series1Numbers).indexOf(Integer.parseInt(ls.get(i).getDate().split("/")[0]));
                series1Numbers[index+1] = (series1Numbers[index+1].doubleValue())+ls.get(i).getmCO2();
            }else{
                series1Numbers[2*i+1] = ls.get(i).getmCO2();
                series1Numbers[2*i] =Integer.parseInt(ls.get(i).getDate().split("/")[0]) ;
            }
        }

        List<Number> list = new ArrayList<Number>();
        for(Number s : series1Numbers) {
            if(s != null) {
                list.add(s);
            }
        }
        series1Numbers = list.toArray(new Number[list.size()]);

        /** for(int i=0; i<series1Numbers.length;i++){
             if(series1Numbers[i] == null){
                 for(int j=i+1; j<series1Numbers.length; j++){
                     series1Numbers[i+(j-i)]=series1Numbers[j];
                 }
             }
         }*/

        for(Number s: series1Numbers){
            Log.d("Stat", "stat: " + s);
        }
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),
                // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Y_VALS_ONLY means use the element index as the x value
                "Mat");                             // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getActivity().getApplicationContext(),R.xml.lpf1);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);


        // reduce the number of range labels

        plot.setTicksPerRangeLabel(1);
        plot.setTicksPerDomainLabel(1);
        plot.setRangeValueFormat(
                new DecimalFormat("#####.##"));
        plot.setDomainValueFormat(
                new DecimalFormat("#####.##"));
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.calculateMinMaxVals();
        plot.setDomainBoundaries(((plot.getCalculatedMinX().doubleValue()-10)),((int) plot.getCalculatedMaxX().doubleValue()+10), BoundaryMode.FIXED);
        plot.setRangeBoundaries(Math.floor((double) plot.getCalculatedMinY().doubleValue()-10),Math.floor((double) plot.getCalculatedMaxY().doubleValue()+10), BoundaryMode.FIXED);
        plot.redraw();
    }
}