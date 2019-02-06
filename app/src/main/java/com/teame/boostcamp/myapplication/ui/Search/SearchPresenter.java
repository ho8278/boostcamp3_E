package com.teame.boostcamp.myapplication.ui.Search;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.adapter.SearchAdapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.InputKeyboardUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.collection.CircularArray;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private Geocoder geocoder=null;
    private ExListAdapterContract.Model adapterModel;
    private ExListAdapterContract.View adapterView;
    private ResourceProvider provider;
    private SharedPreferences prefExSearch;
    private ArrayList<String> exList;
    private static final String PREF_EX_SEARCH="PREF_EX_SEARCH";
    private static final int STRING_CAPACITY=20;
    private Gson gson;

    @Override
    public void setAdpaterView(ExListAdapterContract.View adapter) {
        adapterView=adapter;
    }

    @Override
    public void setAdpaterModel(ExListAdapterContract.Model adapter) {
        adapterModel=adapter;
    }

    public SearchPresenter(SearchContract.View view, ResourceProvider provider){
        this.view=view;
        this.provider=provider;
        prefExSearch = PreferenceManager.getDefaultSharedPreferences(provider.getApplicationContext());
        gson=new Gson();
        String list=prefExSearch.getString(PREF_EX_SEARCH,"");
        if(list.equals("")){
            exList=new ArrayList<>();
        }else{
            exList=gson.fromJson(list,new TypeToken<ArrayList<String>>(){}.getType());
        }
        DLogUtil.e(exList.toString());
    }

    @Override
    public void onTextChange(String text) {
        adapterModel.searchList(text);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onSearchSubmit(String place) {
        if(geocoder==null){
            geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
        }
        try{
            List<Address> geoResult=geocoder.getFromLocationName(place,1);
            LatLng latlon=new LatLng(geoResult.get(0).getLatitude(),geoResult.get(0).getLongitude());
            view.showPositionInMap(latlon);
        }catch(Exception e){
            view.hideExSearchView();
            view.showFragmentToast("검색 결과가 없습니다.");
            return;
        }
        if(adapterModel.searchList(place)){
            if(exList.size()>=STRING_CAPACITY){
                exList.remove(0);
            }
            exList.add(place);
            adapterModel.add(place);
        }
    }

    @Override
    public void initView() {
        adapterModel.setList(exList);
    }

    @Override
    public void onDetach() {
        geocoder=null;
        view=null;
        //Test용
        if(exList.size()>0)
            exList.clear();
        SharedPreferences.Editor editor=prefExSearch.edit();
        String json=gson.toJson(exList);
        editor.putString(PREF_EX_SEARCH,json);
        editor.apply();
    }
}