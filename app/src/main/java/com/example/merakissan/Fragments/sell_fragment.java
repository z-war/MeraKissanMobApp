package com.example.merakissan.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merakissan.R;


public class sell_fragment extends Fragment {

    private View frag;
    private Spinner dropdown;
    private String[] items = {"Cattle", "Machinery"};
    private ArrayAdapter<String> adapter;

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
            dropdown = frag.findViewById(R.id.product_type);
            adapter = new ArrayAdapter<>( getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,items);
            dropdown.setAdapter(adapter);
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
