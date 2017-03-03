package com.flycode.transporttrackeryerevan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flycode.transporttrackeryerevan.adapter.BusRouteAdapter;

public class StartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] busRouteTitles = { "route 13", "route 48", "route 56", "route 65", "route 114"};
    String[] busRouteDescrs = { "Հայ ռուս գազարդ\nԹբիլիսյան խճ.\nՔանաքեռցու փ.\nԱզատության պ.\nԿոմիտասի պ-ներ",
                                "Կասյան փ.\nՄ. Բաղրամյան\nՍայաթ Նովա\nՌադիոտուն\nՆովայի պ.",
                                "Էրեբունու զ/ծ (ս.կ.)\nԽաղաղ Դոնի\nՏիտոգրադյան\nԱյվազովսկու\nԷրեբունու փ-ներ\nԱրցախի",
                                "Տ.Մեծի պ-ներ\nՀանրապետության հր.\nԱմիրյան փ.\nՄաշտոցի\nՄարշալ Բաղրամյան պ-ներ",
                                "Կասյան փ.\nԿոմիտասի\nԱզատության պ-ներ\nՊ.Սևակի\nԴրոյի\nԶեյթունի 5-րդ\nԼեփսիուսի փ-ներ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ListView busNumbersList = (ListView) findViewById(R.id.bus_numbers_list);
        busNumbersList.setOnItemClickListener(this);

        BusRouteAdapter adapt = new BusRouteAdapter(this, busRouteTitles, busRouteDescrs);
        busNumbersList.setAdapter(adapt);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CLICK_POSITION", position)
                .putExtra("BUS_NUMBERS_LIST", busRouteTitles);
        startActivity(intent);
    }
}
