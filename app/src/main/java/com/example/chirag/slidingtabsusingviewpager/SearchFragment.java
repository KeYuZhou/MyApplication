package com.example.chirag.slidingtabsusingviewpager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SwipeGestureHelper.OnSwipeListener {


    String[] values = new String[100];

    private Context mContext;
    HashMap<String, String> map = new HashMap<>();
    RecommendAdapter recommendAdapter;
    String accountNo;
    ArrayAdapter mAdapter;
    ListAdapter adapter;

    List<String> mAllValues = new ArrayList<>();
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        accountNo = getActivity().getIntent().getStringExtra("accountNo");
        Log.e("searchFragment", accountNo);
        read();
//populateList();



    }

    private void save() {


        try {

            FileOutputStream outputStream = getActivity().openFileOutput("query.txt",
                    Activity.MODE_PRIVATE);
            String s = new String();
            for (int i = 0; i < mAllValues.size(); i++) {
                s = s + " " + mAllValues.get(i);
            }
            outputStream.write(s.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {


        super.onResume();
        //save();
        // read ();
    }

    private void read() {
        try {
            FileInputStream inputStream = getActivity().openFileInput("query.txt");
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());
            String temp[] = content.split(" ");


            for (int i = 0; i < temp.length; i++) {
                mAllValues.add(temp[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.search_fragment, container, false);

        //    mAdapter = new ArrayAdapter(layout.getContext(), android.R.layout.simple_list_item_1, mAllValues);
        //   listView = (ListView) layout.findViewById(R.id.list);

//        listView.setAdapter(mAdapter);
        //   listView.setTextFilterEnabled(true);

        RelativeLayout rl = (RelativeLayout) layout.findViewById(R.id.container);


        recommendAdapter = new RecommendAdapter(getActivity(), accountNo);

        RecyclerView recyclerView = new SnappingSwipingViewBuilder(getActivity())
                .setAdapter(recommendAdapter)
                .setHeadTailExtraMarginDp(17F)
                .setItemMarginDp(8F, 20F, 8F, 20F)
                .setOnSwipeListener(this)
                .setSnapMethod(SnappyLinearLayoutManager.SnappyLinearSmoothScroller.SNAP_CENTER)
                .build();

        if (rl != null) {
            recyclerView.setLayoutParams(new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rl.addView(recyclerView);
        }


        recommendAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("accountNo", accountNo);
                intent.putExtra("query", recommendAdapter.dateList.get(position));
                startActivity(intent);

            }
        });

//        listView.setVisibility(View.GONE);

        return layout;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final MaterialSearchView searchView = getActivity().findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(this);
        String[] temp = new String[mAllValues.size()];
        for (int i = 0; i < mAllValues.size(); i++) {
            temp[i] = mAllValues.get(i);
        }
        searchView.setSuggestions(temp);
        searchView.setHint("Search Here");

        //  searchView.setAdapter(mAdapter);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

//                populateList();
//
////
////                  listView = (ListView) getView().findViewById(android.R.id.list);
////                  listView.setAdapter(mAdapter);
//listView.setVisibility(View.VISIBLE);


            }

            @Override
            public void onSearchViewClosed() {

                //       listView.setVisibility(View.GONE);
//                FragmentManager fragmentManager = getFragmentManager();
//
//
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                Fragment fragment = fragmentManager.findFragmentByTag("search");
//                fragmentTransaction.remove(fragment);
//                fragmentTransaction.commit();
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        Bundle bundle = new Bundle();
        bundle.putString("query", query);//这里的values就是我们要传的值



        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


        mAllValues.add(query);
        values[mAllValues.size()] = query;



        save();

        // read();



        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("accountNo", accountNo);

        startActivity(intent);


        return true;
    }


    @Override
    public boolean onQueryTextChange(String s) {

//        mAdapter.getFilter().filter(s);
//
//        if (s == null || s.trim().isEmpty()) {
//            resetSearch();
//            return false;
//        }
//
//        List<String> filteredValues = new ArrayList<String>(mAllValues);
//        for (String value : mAllValues) {
//            if (!value.toLowerCase().contains(s.toLowerCase())) {
//                filteredValues.remove(value);
//            }
//        }
//
//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
//        listView.setAdapter(mAdapter);

        return false;

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {


        return true;
    }

    @Override
    public void onSwipe(RecyclerView rv, int adapterPosition, float dy) {
        recommendAdapter.removeItem(adapterPosition);
        rv.invalidateItemDecorations();
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }
//
//    public void resetSearch() {
//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
//        listView.setAdapter(mAdapter);
//    }
//
//    private void populateList(){
//
//        mAllValues = new ArrayList<>();
//
//        mAllValues.add("Afghanistan");
//        mAllValues.add("Åland Islands");
//        mAllValues.add("Albania");
//        mAllValues.add("Algeria");
//        mAllValues.add("American Samoa");
//        mAllValues.add("AndorrA");
//        mAllValues.add("Angola");
//        mAllValues.add("Anguilla");
//        mAllValues.add("Antarctica");
//        mAllValues.add("Antigua and Barbuda");
//        mAllValues.add("Argentina");
//        mAllValues.add("Armenia");
//        mAllValues.add("Aruba");
//        mAllValues.add("Australia");
//        mAllValues.add("Austria");
//        mAllValues.add("Azerbaijan");
//        mAllValues.add("Bahamas");
//        mAllValues.add("Bahrain");
//        mAllValues.add("Bangladesh");
//        mAllValues.add("Barbados");
//        mAllValues.add("Belarus");
//        mAllValues.add("Belgium");
//        mAllValues.add("Belize");
//        mAllValues.add("Benin");
//        mAllValues.add("Bermuda");
//        mAllValues.add("Bhutan");
//        mAllValues.add("Bolivia");
//        mAllValues.add("Bosnia and Herzegovina");
//        mAllValues.add("Botswana");
//        mAllValues.add("Bouvet Island");
//        mAllValues.add("Brazil");
//        mAllValues.add("British Indian Ocean Territory");
//        mAllValues.add("Brunei Darussalam");
//        mAllValues.add("Bulgaria");
//        mAllValues.add("Burkina Faso");
//        mAllValues.add("Burundi");
//        mAllValues.add("Cambodia");
//        mAllValues.add("Cameroon");
//        mAllValues.add("Canada");
//        mAllValues.add("Cape Verde");
//        mAllValues.add("Cayman Islands");
//        mAllValues.add("Central African Republic");
//        mAllValues.add("Chad");
//        mAllValues.add("Chile");
//        mAllValues.add("China");
//        mAllValues.add("Christmas Island");
//        mAllValues.add("Cocos (Keeling) Islands");
//        mAllValues.add("Colombia");
//        mAllValues.add("Comoros");
//        mAllValues.add("Congo");
//        mAllValues.add("Congo, The Democratic Republic of the");
//        mAllValues.add("Cook Islands");
//        mAllValues.add("Costa Rica");
//        mAllValues.add("Cote D\'Ivoire");
//        mAllValues.add("Croatia");
//        mAllValues.add("Cuba");
//        mAllValues.add("Cyprus");
//        mAllValues.add("Czech Republic");
//        mAllValues.add("Denmark");
//        mAllValues.add("Djibouti");
//        mAllValues.add("Dominica");
//        mAllValues.add("Dominican Republic");
//        mAllValues.add("Ecuador");
//        mAllValues.add("Egypt");
//        mAllValues.add("El Salvador");
//        mAllValues.add("Equatorial Guinea");
//        mAllValues.add("Eritrea");
//        mAllValues.add("Estonia");
//        mAllValues.add("Ethiopia");
//        mAllValues.add("Falkland Islands (Malvinas)");
//        mAllValues.add("Faroe Islands");
//        mAllValues.add("Fiji");
//        mAllValues.add("Finland");
//        mAllValues.add("France");
//        mAllValues.add("French Guiana");
//        mAllValues.add("French Polynesia");
//        mAllValues.add("French Southern Territories");
//        mAllValues.add("Gabon");
//        mAllValues.add("Gambia");
//        mAllValues.add("Georgia");
//        mAllValues.add("Germany");
//        mAllValues.add("Ghana");
//        mAllValues.add("Gibraltar");
//        mAllValues.add("Greece");
//        mAllValues.add("Greenland");
//        mAllValues.add("Grenada");
//        mAllValues.add("Guadeloupe");
//        mAllValues.add("Guam");
//        mAllValues.add("Guatemala");
//        mAllValues.add("Guernsey");
//        mAllValues.add("Guinea");
//        mAllValues.add("Guinea-Bissau");
//        mAllValues.add("Guyana");
//        mAllValues.add("Haiti");
//        mAllValues.add("Heard Island and Mcdonald Islands");
//        mAllValues.add("Holy See (Vatican City State)");
//        mAllValues.add("Honduras");
//        mAllValues.add("Hong Kong");
//        mAllValues.add("Hungary");
//        mAllValues.add("Iceland");
//        mAllValues.add("India");
//        mAllValues.add("Indonesia");
//        mAllValues.add("Iran, Islamic Republic Of");
//        mAllValues.add("Iraq");
//        mAllValues.add("Ireland");
//        mAllValues.add("Isle of Man");
//        mAllValues.add("Israel");
//        mAllValues.add("Italy");
//        mAllValues.add("Jamaica");
//        mAllValues.add("Japan");
//        mAllValues.add("Jersey");
//        mAllValues.add("Jordan");
//        mAllValues.add("Kazakhstan");
//        mAllValues.add("Kenya");
//        mAllValues.add("Kiribati");
//        mAllValues.add("Korea, Democratic People\'S Republic of");
//        mAllValues.add("Korea, Republic of");
//        mAllValues.add("Kuwait");
//        mAllValues.add("Kyrgyzstan");
//        mAllValues.add("Lao People\'S Democratic Republic");
//        mAllValues.add("Latvia");
//        mAllValues.add("Lebanon");
//        mAllValues.add("Lesotho");
//        mAllValues.add("Liberia");
//        mAllValues.add("Libyan Arab Jamahiriya");
//        mAllValues.add("Liechtenstein");
//        mAllValues.add("Lithuania");
//        mAllValues.add("Luxembourg");
//        mAllValues.add("Macao");
//        mAllValues.add("Macedonia, The Former Yugoslav Republic of");
//        mAllValues.add("Madagascar");
//        mAllValues.add("Malawi");
//        mAllValues.add("Malaysia");
//        mAllValues.add("Maldives");
//        mAllValues.add("Mali");
//        mAllValues.add("Malta");
//        mAllValues.add("Marshall Islands");
//        mAllValues.add("Martinique");
//        mAllValues.add("Mauritania");
//        mAllValues.add("Mauritius");
//        mAllValues.add("Mayotte");
//        mAllValues.add("Mexico");
//        mAllValues.add("Micronesia, Federated States of");
//        mAllValues.add("Moldova, Republic of");
//        mAllValues.add("Monaco");
//        mAllValues.add("Mongolia");
//        mAllValues.add("Montserrat");
//        mAllValues.add("Morocco");
//        mAllValues.add("Mozambique");
//        mAllValues.add("Myanmar");
//        mAllValues.add("Namibia");
//        mAllValues.add("Nauru");
//        mAllValues.add("Nepal");
//        mAllValues.add("Netherlands");
//        mAllValues.add("Netherlands Antilles");
//        mAllValues.add("New Caledonia");
//        mAllValues.add("New Zealand");
//        mAllValues.add("Nicaragua");
//        mAllValues.add("Niger");
//        mAllValues.add("Nigeria");
//        mAllValues.add("Niue");
//        mAllValues.add("Norfolk Island");
//        mAllValues.add("Northern Mariana Islands");
//        mAllValues.add("Norway");
//        mAllValues.add("Oman");
//        mAllValues.add("Pakistan");
//        mAllValues.add("Palau");
//        mAllValues.add("Palestinian Territory, Occupied");
//        mAllValues.add("Panama");
//        mAllValues.add("Papua New Guinea");
//        mAllValues.add("Paraguay");
//        mAllValues.add("Peru");
//        mAllValues.add("Philippines");
//        mAllValues.add("Pitcairn");
//        mAllValues.add("Poland");
//        mAllValues.add("Portugal");
//        mAllValues.add("Puerto Rico");
//        mAllValues.add("Qatar");
//        mAllValues.add("Reunion");
//        mAllValues.add("Romania");
//        mAllValues.add("Russian Federation");
//        mAllValues.add("RWANDA");
//        mAllValues.add("Saint Helena");
//        mAllValues.add("Saint Kitts and Nevis");
//        mAllValues.add("Saint Lucia");
//        mAllValues.add("Saint Pierre and Miquelon");
//        mAllValues.add("Saint Vincent and the Grenadines");
//        mAllValues.add("Samoa");
//        mAllValues.add("San Marino");
//        mAllValues.add("Sao Tome and Principe");
//        mAllValues.add("Saudi Arabia");
//        mAllValues.add("Senegal");
//        mAllValues.add("Serbia and Montenegro");
//        mAllValues.add("Seychelles");
//        mAllValues.add("Sierra Leone");
//        mAllValues.add("Singapore");
//        mAllValues.add("Slovakia");
//        mAllValues.add("Slovenia");
//        mAllValues.add("Solomon Islands");
//        mAllValues.add("Somalia");
//        mAllValues.add("South Africa");
//        mAllValues.add("South Georgia and the South Sandwich Islands");
//        mAllValues.add("Spain");
//        mAllValues.add("Sri Lanka");
//        mAllValues.add("Sudan");
//        mAllValues.add("Suriname");
//        mAllValues.add("Svalbard and Jan Mayen");
//        mAllValues.add("Swaziland");
//        mAllValues.add("Sweden");
//        mAllValues.add("Switzerland");
//        mAllValues.add("Syrian Arab Republic");
//        mAllValues.add("Taiwan, Province of China");
//        mAllValues.add("Tajikistan");
//        mAllValues.add("Tanzania, United Republic of");
//        mAllValues.add("Thailand");
//        mAllValues.add("Timor-Leste");
//        mAllValues.add("Togo");
//        mAllValues.add("Tokelau");
//        mAllValues.add("Tonga");
//        mAllValues.add("Trinidad and Tobago");
//        mAllValues.add("Tunisia");
//        mAllValues.add("Turkey");
//        mAllValues.add("Turkmenistan");
//        mAllValues.add("Turks and Caicos Islands");
//        mAllValues.add("Tuvalu");
//        mAllValues.add("Uganda");
//        mAllValues.add("Ukraine");
//        mAllValues.add("United Arab Emirates");
//        mAllValues.add("United Kingdom");
//        mAllValues.add("United States");
//        mAllValues.add("United States Minor Outlying Islands");
//        mAllValues.add("Uruguay");
//        mAllValues.add("Uzbekistan");
//        mAllValues.add("Vanuatu");
//        mAllValues.add("Venezuela");
//        mAllValues.add("Viet Nam");
//        mAllValues.add("Virgin Islands, British");
//        mAllValues.add("Virgin Islands, U.S.");
//        mAllValues.add("Wallis and Futuna");
//        mAllValues.add("Western Sahara");
//        mAllValues.add("Yemen");
//        mAllValues.add("Zambia");
//        mAllValues.add("Zimbabwe");
//
//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
//
//        listView.setAdapter(mAdapter);
//
//
//
//    }

}


