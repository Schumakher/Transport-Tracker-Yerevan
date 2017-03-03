package com.flycode.transporttrackeryerevan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.flycode.transporttrackeryerevan.R;

/**
 * Created by Schumakher on 3/3/17.
 */

public class BusRouteAdapter extends ArrayAdapter<String> {
    private Context context;
    private final String[] routeTitles;
    private final String[] routeDescrs;

    public BusRouteAdapter(Context context, String[] routeTitles, String[] routeDescrs) {
        super(context, R.layout.item_bus_routes);

        this.context = context;
        this.routeTitles = routeTitles;
        this.routeDescrs = routeDescrs;
    }

    @Override
    public int getCount() {
        return routeTitles.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemBus = inflater.inflate(R.layout.item_bus_routes, parent, false);
        TextView routeTitle = (TextView) itemBus.findViewById(R.id.route_title);
        TextView routeDescr = (TextView) itemBus.findViewById(R.id.route_descr);

        routeTitle.setText(routeTitles[position]);
        routeDescr.setText(routeDescrs[position]);

        return  itemBus;
    }
}
