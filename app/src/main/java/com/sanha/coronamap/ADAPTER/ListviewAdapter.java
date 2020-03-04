package com.sanha.coronamap.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanha.coronamap.CLASS.News;
import com.sanha.coronamap.R;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    public ArrayList<News> listViewItemList = new ArrayList<News>() ;

    public ListviewAdapter() {}

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        News listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getNewsContent());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String title, String desc, String ndata) {
        News item = new News();
        item.setNewsContent(title);
        item.setNewsLink(desc);
        item.setNewsDate(ndata);
        listViewItemList.add(item);
    }

    public void noti(){
        notifyDataSetChanged();
    }

    public void deleteAll(){
        listViewItemList.clear();
    }
}
