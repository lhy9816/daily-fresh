package com.example.lenovo.dailyfresh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.lenovo.dailyfresh.R;
import com.example.lenovo.dailyfresh.fruit_db.Fruit;
import com.example.lenovo.dailyfresh.fruit_db.FruitActivity;
import com.example.lenovo.dailyfresh.fruit_db.FruitAdapter;
import com.example.lenovo.dailyfresh.fruit_db.FruitDao;
import com.example.lenovo.dailyfresh.shop_db.ShopActivity;

import java.util.List;


public class SearchFragment extends Fragment {

	private int flag;
	private SearchView mSearchView;
	private ListView listView;
	private FruitDao fruidao;
	private ArrayAdapter<String> mAdapter;
	private List<Fruit> list;
	final private String[] fruitstring = {"苹果","柠檬","红毛丹","桃子","柿子","李子","榴莲","荔枝","草莓","猕猴桃"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_search,null);

		mSearchView = (SearchView) view.findViewById(R.id.search);
		mSearchView.setIconifiedByDefault(true);
		mSearchView.setFocusable(false);
		mSearchView.clearFocus();
		flag = 0;
		listView = (ListView) view.findViewById(R.id.list_view_search);

		listView.setAdapter(mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,fruitstring));
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(flag==1||flag==0){
					Intent intent = new Intent();
					intent.setClass(SearchFragment.this.getActivity(), FruitActivity.class);
					startActivity(intent);
				}

			}
		});

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String queryText) {

				if (TextUtils.isEmpty(queryText)) {
					listView.clearTextFilter();
				} else {
					listView.setFilterText(queryText.toString());
				}
				flag = 0;

				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String queryText) {

				switch (queryText) {
					case "柠檬":
						Toast.makeText(getActivity(), "柠檬在数据库中，其价格为8", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "苹果":
						Toast.makeText(getActivity(), "苹果在数据库中，其价格为10", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "红毛丹":
						Toast.makeText(getActivity(), "红毛丹在数据库中，其价格为12", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "桃子":
						Toast.makeText(getActivity(), "桃子在数据库中，其价格为13", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "柿子":
						Toast.makeText(getActivity(), "柿子在数据库中，其价格为11", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "李子":
						Toast.makeText(getActivity(), "李子在数据库中，其价格为9", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "榴莲":
						Toast.makeText(getActivity(), "榴莲在数据库中，其价格为20", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "荔枝":
						Toast.makeText(getActivity(), "荔枝在数据库中，其价格为15", Toast.LENGTH_SHORT).show();
						break;
					case "猕猴桃":
						Toast.makeText(getActivity(), "猕猴桃在数据库中，其价格为20", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
					case "草莓":
						Toast.makeText(getActivity(), "草莓在数据库中，其价格为16", Toast.LENGTH_SHORT).show();
						flag = 1;
						break;
				}
				for(String str:fruitstring){
					if(str.indexOf(queryText)>=0){
						Intent intent = new Intent();
						intent.setClass(SearchFragment.this.getActivity(), FruitActivity.class);
						startActivity(intent);
					}
				}
				mSearchView.setIconified(true);
				return true;
			}
		});
		return view;
	}

}
