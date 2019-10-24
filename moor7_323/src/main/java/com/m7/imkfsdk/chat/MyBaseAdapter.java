package com.m7.imkfsdk.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的适配器
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected Context context;
	protected LayoutInflater inflater;
	protected List<T> myList = new ArrayList<T>();

	public MyBaseAdapter(Context context) {
		this.context = context;
		if(context!=null){
			this.inflater = LayoutInflater.from(context);
		}
	}

	public List<T> getAdapterData() {
		return myList;
	}

	public void appendData(T t, boolean isClearOld) {
		if (t == null)
			return;
		if (isClearOld)
			myList.clear();
		myList.add(t);
	}

	public void appendData(List<T> data, boolean isClearOld) {
		if (data == null)
			return;
		if (isClearOld)
			myList.clear();
		myList.addAll(data);
	}

	public void appendDataTop(T t, boolean isClearOld) {
		if (t == null)
			return;
		if (isClearOld)
			myList.clear();
		myList.add(0, t);
	}

	public void appendDataTop(List<T> data, boolean isClearOld) {
		if (data == null)
			return;
		if (isClearOld)
			myList.clear();
		myList.addAll(0, data);
	}

	public void update() {
		this.notifyDataSetChanged();
	}

	public void clear() {
		myList.clear();
	}

	public int getCount() {
		if (myList == null)
			return 0;
		return myList.size();
	}

	public T getItem(int position) {
		if (myList == null || myList.size() <= 0)
			return null;
		if (position > myList.size())
			return null;
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getMyView(position, convertView, parent);
	}

	public abstract View getMyView(int position, View convertView,
								   ViewGroup parent);

}
