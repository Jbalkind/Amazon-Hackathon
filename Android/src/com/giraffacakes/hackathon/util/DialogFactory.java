package com.giraffacakes.hackathon.util;

import java.util.List;

import com.giraffacakes.hackathon.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class DialogFactory {
	public static AlertDialog confirmDialog(Context context, String title, String message,
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, positiveListener);
        builder.setNegativeButton(R.string.no, negativeListener);
        
        return builder.create();
	}
	
	public static AlertDialog choiceDialog(Context context, String title, String message, final List<String> choices,
			final Handler resultHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
        builder.setMessage(message);
        
        final ListView list = new ListView(context);
        list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, choices));
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Message result = new Message();
				result.obj = choices.get(position);
				resultHandler.sendMessage(result);
				
				
			}
        });
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        
        builder.setView(list);
        
        return builder.create();
	}
	
	public static AlertDialog warningDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        
        return builder.create();
	}
	
	public static AlertDialog textEntryDialog(Context context, String title, String message,
			String defaultText, String hintText, final Handler resultHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
        builder.setMessage(message);
		
		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		
		if (hintText != null) {
			input.setHint(hintText);
		}
		
		if (defaultText != null) {
			input.setText(defaultText);
		}
		
		builder.setView(input);
		
		if (resultHandler != null) {
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Message result = new Message();
					result.obj = input.getText().toString();
					resultHandler.sendMessage(result);
				}
			});
		} else {
			builder.setPositiveButton(R.string.ok, null);
		}
		

		builder.setNegativeButton(R.string.cancel, null);
		
		return builder.create();
	}
}
