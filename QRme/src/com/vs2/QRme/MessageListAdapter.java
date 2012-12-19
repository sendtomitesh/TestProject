package com.vs2.QRme;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MessageListAdapter extends SimpleAdapter {

	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>> data;
	public MessageListAdapter(Context context,
			ArrayList<HashMap<String, String>> data, int resource, String[] from,
			int[] to) {
		
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		this.data = data;
		mInflater = LayoutInflater.from(context);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		   ViewHolder holder;
		   
		    
		    if (convertView == null) {
		        convertView = mInflater.inflate(R.layout.messages_list_item, null);

		        holder = new ViewHolder();
		        
		        holder.textId = (TextView) convertView.findViewById(R.id.txt_message_id);
		        holder.textMessage = (TextView) convertView.findViewById(R.id.txt_message);
		        
		        convertView.setTag(holder);
		    } else {
		        holder = (ViewHolder) convertView.getTag();
		    }

		    holder.textMessage.setText(data.get(position).get("message"));
		    holder.textId.setText(data.get(position).get("id"));
		    //holder.textMessage.setTextColor(Color.RED);
		    
		    

		return super.getView(position, convertView, parent);
	}
	
	public class ViewHolder{
		TextView textMessage,textId;
	}

}
