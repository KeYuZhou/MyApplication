package com.example.keyu.myapplication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by effy on 2018/3/11.
 */

public class FragmentOne extends Fragment {


    List<String> mAllValues;



    private ListView mListView;
    private ArrayAdapter mAdapter;

    private Context mContext;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
       populateList();

    }


//    public void onListItemClick(ListView listView, View v, int position, long id) {
//        String item = (String) listView.getAdapter().getItem(position);
//        if (getActivity() instanceof OnItem1SelectedListener) {
//            ((OnItem1SelectedListener) getActivity()).OnItem1SelectedListener(item);
//        }
//        getFragmentManager().popBackStack();
//    }


    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_one,container,false);



//        ListView listView = (ListView) view.findViewById(android.R.id.list);
//        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);
//        listView.setEmptyView(emptyTextView);
//


        ArrayList<String> arrayBook =new ArrayList<>();
        arrayBook.addAll(Arrays.asList(getResources().getStringArray(R.array.Books)));

        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, mAllValues);
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Intent intent = new Intent(getActivity(),MainActivity.class);
//                startActivity(intent);
            }
        });




        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_books);
        final SearchView searchView = (SearchView) searchItem.getActionView();


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {



                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        });






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
                FragmentOne fragmentOne=new FragmentOne();
                Fragment fragmentTwo=new FragmentTwo();
                Fragment fragmentThree=new FragmentThree();

                FragmentTransaction beginTransaction=getFragmentManager().beginTransaction();

                beginTransaction.add(R.id.content,fragmentOne).add(R.id.content,fragmentTwo).add(R.id.content,fragmentThree);//开启一个事务将fragment动态加载到组件
                beginTransaction.hide(fragmentOne).hide(fragmentThree);
                beginTransaction.show(fragmentTwo);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);




                if (s == null || s.trim().isEmpty()) {
                    resetSearch();
                    return false;
                }

                List<String> filteredValues = new ArrayList<String>(mAllValues);
                for (String value : mAllValues) {
                    if (!value.toLowerCase().contains(s.toLowerCase())) {
                        filteredValues.remove(value);
                    }
                }

                mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
                mListView.setAdapter(mAdapter);

                return false;





            }
        });
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);


//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                // TODO: 处理一些关闭搜索框后的逻辑，比如还原之前数据
//                return false;
//            }
//        });





    }

    public void resetSearch() {
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
        mListView.setAdapter(mAdapter);
    }

//read from database
    private void populateList(){

        mAllValues = new ArrayList<>();

        mAllValues.add("Afghanistan");
        mAllValues.add("Åland Islands");
        mAllValues.add("Albania");
        mAllValues.add("Algeria");
        mAllValues.add("American Samoa");
        mAllValues.add("AndorrA");
        mAllValues.add("Angola");
        mAllValues.add("Anguilla");
        mAllValues.add("Antarctica");
        mAllValues.add("Antigua and Barbuda");
        mAllValues.add("Argentina");
        mAllValues.add("Armenia");
        mAllValues.add("Aruba");
        mAllValues.add("Australia");
        mAllValues.add("Austria");
        mAllValues.add("Azerbaijan");
        mAllValues.add("Bahamas");
        mAllValues.add("Bahrain");
        mAllValues.add("Bangladesh");
        mAllValues.add("Barbados");
        mAllValues.add("Belarus");
        mAllValues.add("Belgium");
        mAllValues.add("Belize");
        mAllValues.add("Benin");
        mAllValues.add("Bermuda");
        mAllValues.add("Bhutan");
        mAllValues.add("Bolivia");
        mAllValues.add("Bosnia and Herzegovina");
        mAllValues.add("Botswana");
        mAllValues.add("Bouvet Island");
        mAllValues.add("Brazil");
        mAllValues.add("British Indian Ocean Territory");
        mAllValues.add("Brunei Darussalam");
        mAllValues.add("Bulgaria");
        mAllValues.add("Burkina Faso");
        mAllValues.add("Burundi");
        mAllValues.add("Cambodia");
        mAllValues.add("Cameroon");
        mAllValues.add("Canada");
        mAllValues.add("Cape Verde");
        mAllValues.add("Cayman Islands");
        mAllValues.add("Central African Republic");
        mAllValues.add("Chad");
        mAllValues.add("Chile");
        mAllValues.add("China");
        mAllValues.add("Christmas Island");
        mAllValues.add("Cocos (Keeling) Islands");
        mAllValues.add("Colombia");
        mAllValues.add("Comoros");
        mAllValues.add("Congo");
        mAllValues.add("Congo, The Democratic Republic of the");
        mAllValues.add("Cook Islands");
        mAllValues.add("Costa Rica");
        mAllValues.add("Cote D\'Ivoire");
        mAllValues.add("Croatia");
        mAllValues.add("Cuba");
        mAllValues.add("Cyprus");
        mAllValues.add("Czech Republic");
        mAllValues.add("Denmark");
        mAllValues.add("Djibouti");
        mAllValues.add("Dominica");
        mAllValues.add("Dominican Republic");
        mAllValues.add("Ecuador");
        mAllValues.add("Egypt");
        mAllValues.add("El Salvador");
        mAllValues.add("Equatorial Guinea");
        mAllValues.add("Eritrea");
        mAllValues.add("Estonia");
        mAllValues.add("Ethiopia");
        mAllValues.add("Falkland Islands (Malvinas)");
        mAllValues.add("Faroe Islands");
        mAllValues.add("Fiji");
        mAllValues.add("Finland");
        mAllValues.add("France");
        mAllValues.add("French Guiana");
        mAllValues.add("French Polynesia");
        mAllValues.add("French Southern Territories");
        mAllValues.add("Gabon");
        mAllValues.add("Gambia");
        mAllValues.add("Georgia");
        mAllValues.add("Germany");
        mAllValues.add("Ghana");
        mAllValues.add("Gibraltar");
        mAllValues.add("Greece");
        mAllValues.add("Greenland");
        mAllValues.add("Grenada");
        mAllValues.add("Guadeloupe");
        mAllValues.add("Guam");
        mAllValues.add("Guatemala");
        mAllValues.add("Guernsey");
        mAllValues.add("Guinea");
        mAllValues.add("Guinea-Bissau");
        mAllValues.add("Guyana");
        mAllValues.add("Haiti");
        mAllValues.add("Heard Island and Mcdonald Islands");
        mAllValues.add("Holy See (Vatican City State)");
        mAllValues.add("Honduras");
        mAllValues.add("Hong Kong");
        mAllValues.add("Hungary");
        mAllValues.add("Iceland");
        mAllValues.add("India");
        mAllValues.add("Indonesia");
        mAllValues.add("Iran, Islamic Republic Of");
        mAllValues.add("Iraq");
        mAllValues.add("Ireland");
        mAllValues.add("Isle of Man");
        mAllValues.add("Israel");
        mAllValues.add("Italy");
        mAllValues.add("Jamaica");
        mAllValues.add("Japan");
        mAllValues.add("Jersey");
        mAllValues.add("Jordan");
        mAllValues.add("Kazakhstan");
        mAllValues.add("Kenya");
        mAllValues.add("Kiribati");
        mAllValues.add("Korea, Democratic People\'S Republic of");
        mAllValues.add("Korea, Republic of");
        mAllValues.add("Kuwait");
        mAllValues.add("Kyrgyzstan");
        mAllValues.add("Lao People\'S Democratic Republic");
        mAllValues.add("Latvia");
        mAllValues.add("Lebanon");
        mAllValues.add("Lesotho");
        mAllValues.add("Liberia");
        mAllValues.add("Libyan Arab Jamahiriya");
        mAllValues.add("Liechtenstein");
        mAllValues.add("Lithuania");
        mAllValues.add("Luxembourg");
        mAllValues.add("Macao");
        mAllValues.add("Macedonia, The Former Yugoslav Republic of");
        mAllValues.add("Madagascar");
        mAllValues.add("Malawi");
        mAllValues.add("Malaysia");
        mAllValues.add("Maldives");
        mAllValues.add("Mali");
        mAllValues.add("Malta");
        mAllValues.add("Marshall Islands");
        mAllValues.add("Martinique");
        mAllValues.add("Mauritania");
        mAllValues.add("Mauritius");
        mAllValues.add("Mayotte");
        mAllValues.add("Mexico");
        mAllValues.add("Micronesia, Federated States of");
        mAllValues.add("Moldova, Republic of");
        mAllValues.add("Monaco");
        mAllValues.add("Mongolia");
        mAllValues.add("Montserrat");
        mAllValues.add("Morocco");
        mAllValues.add("Mozambique");
        mAllValues.add("Myanmar");
        mAllValues.add("Namibia");
        mAllValues.add("Nauru");
        mAllValues.add("Nepal");
        mAllValues.add("Netherlands");
        mAllValues.add("Netherlands Antilles");
        mAllValues.add("New Caledonia");
        mAllValues.add("New Zealand");
        mAllValues.add("Nicaragua");
        mAllValues.add("Niger");
        mAllValues.add("Nigeria");
        mAllValues.add("Niue");
        mAllValues.add("Norfolk Island");
        mAllValues.add("Northern Mariana Islands");
        mAllValues.add("Norway");
        mAllValues.add("Oman");
        mAllValues.add("Pakistan");
        mAllValues.add("Palau");
        mAllValues.add("Palestinian Territory, Occupied");
        mAllValues.add("Panama");
        mAllValues.add("Papua New Guinea");
        mAllValues.add("Paraguay");
        mAllValues.add("Peru");
        mAllValues.add("Philippines");
        mAllValues.add("Pitcairn");
        mAllValues.add("Poland");
        mAllValues.add("Portugal");
        mAllValues.add("Puerto Rico");
        mAllValues.add("Qatar");
        mAllValues.add("Reunion");
        mAllValues.add("Romania");
        mAllValues.add("Russian Federation");
        mAllValues.add("RWANDA");
        mAllValues.add("Saint Helena");
        mAllValues.add("Saint Kitts and Nevis");
        mAllValues.add("Saint Lucia");
        mAllValues.add("Saint Pierre and Miquelon");
        mAllValues.add("Saint Vincent and the Grenadines");
        mAllValues.add("Samoa");
        mAllValues.add("San Marino");
        mAllValues.add("Sao Tome and Principe");
        mAllValues.add("Saudi Arabia");
        mAllValues.add("Senegal");
        mAllValues.add("Serbia and Montenegro");
        mAllValues.add("Seychelles");
        mAllValues.add("Sierra Leone");
        mAllValues.add("Singapore");
        mAllValues.add("Slovakia");
        mAllValues.add("Slovenia");
        mAllValues.add("Solomon Islands");
        mAllValues.add("Somalia");
        mAllValues.add("South Africa");
        mAllValues.add("South Georgia and the South Sandwich Islands");
        mAllValues.add("Spain");
        mAllValues.add("Sri Lanka");
        mAllValues.add("Sudan");
        mAllValues.add("Suriname");
        mAllValues.add("Svalbard and Jan Mayen");
        mAllValues.add("Swaziland");
        mAllValues.add("Sweden");
        mAllValues.add("Switzerland");
        mAllValues.add("Syrian Arab Republic");
        mAllValues.add("Taiwan, Province of China");
        mAllValues.add("Tajikistan");
        mAllValues.add("Tanzania, United Republic of");
        mAllValues.add("Thailand");
        mAllValues.add("Timor-Leste");
        mAllValues.add("Togo");
        mAllValues.add("Tokelau");
        mAllValues.add("Tonga");
        mAllValues.add("Trinidad and Tobago");
        mAllValues.add("Tunisia");
        mAllValues.add("Turkey");
        mAllValues.add("Turkmenistan");
        mAllValues.add("Turks and Caicos Islands");
        mAllValues.add("Tuvalu");
        mAllValues.add("Uganda");
        mAllValues.add("Ukraine");
        mAllValues.add("United Arab Emirates");
        mAllValues.add("United Kingdom");
        mAllValues.add("United States");
        mAllValues.add("United States Minor Outlying Islands");
        mAllValues.add("Uruguay");
        mAllValues.add("Uzbekistan");
        mAllValues.add("Vanuatu");
        mAllValues.add("Venezuela");
        mAllValues.add("Viet Nam");
        mAllValues.add("Virgin Islands, British");
        mAllValues.add("Virgin Islands, U.S.");
        mAllValues.add("Wallis and Futuna");
        mAllValues.add("Western Sahara");
        mAllValues.add("Yemen");
        mAllValues.add("Zambia");
        mAllValues.add("Zimbabwe");

//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
//        mListView.setAdapter(mAdapter);
    }
}
