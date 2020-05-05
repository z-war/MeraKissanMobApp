package com.example.merakissan.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merakissan.R;


public class sell_fragment extends Fragment {

    private View frag;
    private Spinner dropdown , Dropdown_cat;
    private String[] items = {"LiveStock", "Equipment"};
    private String[] items_cattle = {"Cow" ,"Buffalo", "Sheep"  , "Goat" ,"Poultry" , "Camel" };
    private String[] items_equipment = {"Tractor" , "Cultivator" , "Plower" , "harrows" , "Sprayers" ,"Seed Drills" ,"Scythe" ,"Sickle","Harvester"};
    private ArrayAdapter<String> adapter ,adapter_for_cateory;

    public sell_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.fragment_sell_fragment, container, false);
        init();
        return frag;
    }

    private void init() {
        try {
            Dropdown_cat = frag.findViewById(R.id.product_category);
            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,items_cattle);
            Dropdown_cat.setAdapter(adapter_for_cateory);
            dropdown = frag.findViewById(R.id.product_type);
            adapter = new ArrayAdapter<>( getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,items_cattle);
                            Dropdown_cat.setAdapter(adapter_for_cateory);
                            break;
                        case 1:
                            adapter_for_cateory = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,items_equipment);
                            Dropdown_cat.setAdapter(adapter_for_cateory);

                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void change_categries()
    {
        try {

        }catch (Exception e)
        {
            Toast.makeText(getContext(), "change_categries error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
