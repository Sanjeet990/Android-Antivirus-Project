package com.antivirus.sxc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Tester extends AsyncTask<Object, Integer, Boolean> {

	ProgressBar pb;
	TextView tv;
	Context context;
	int num_virus = 0;
	ArrayList<String> viruses;
	Uri uri;
	Activity a=null;
	
	public Tester(Context c, ProgressBar pb, TextView tv, Uri uri) {
		context = c;
		this.pb = pb;
		this.tv = tv;
		viruses = new ArrayList<String>();
		this.uri = uri;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		tv.setText("Scanning");
		pb.setProgress(0);
		a = (Activity) context;
	}

	@Override
	protected Boolean doInBackground(Object... arg0) {
		FileInputStream fstream = null, fstream2=null, fstream3=null;
		int line = 0;
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/Antivirus");
			dir.mkdirs();
			File file = new File(dir, "myfile.hex");

			TextView num_vir= (TextView) a.findViewById(R.id.num_v);
			TextView vir_info = (TextView) a.findViewById(R.id.v_text);
			
			
			fstream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			
			String strLine = br.readLine();

			// /Read virus signature file
			File signature = new File(dir, "signature.db");

			fstream2 = new FileInputStream(signature);
			fstream3 = new FileInputStream(signature);
			BufferedReader signs = new BufferedReader(new InputStreamReader(
					fstream2));
			BufferedReader signs2 = new BufferedReader(new InputStreamReader(
					fstream3));
		
			String one_item;

			// Read File Line By Line

			while ((one_item = signs2.readLine()) != null) {
				line++;
			}

			int i = 0;

			// Read File Line By Line
			while ((one_item = signs.readLine()) != null) {
				String[] virus = one_item.split("=");

				String virusname = virus[0];
				String virussig = virus[1];
				String[] virussigx = virussig.split(" ");
				virussig = virussigx[0];

				int index = strLine.indexOf(virussig);
				if (index != -1) // -1 means "not found"
				{
					// virusfound
					num_virus++;
					viruses.add(virusname);
					num_vir.setText("" + num_virus);
					if(num_virus >= 2) vir_info.setText("viruses found!");
					MediaPlayer mPlayer2;
					mPlayer2 = MediaPlayer.create(context, R.raw.found);
					mPlayer2.start();
				}

				publishProgress((i * 100) / line);
				i++;
			}

			br.close();
			signs.close();
			signs2.close();
			fstream.close();
			fstream2.close();
			fstream3.close();
		
			publishProgress(100);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			publishProgress(40);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			publishProgress(60);
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		pb.setProgress(values[0]);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		String[] actions = {"Delete this file", "Leave as it is"};
		
		a.setContentView(R.layout.virusstatus);

		TextView num_virux = (TextView) a.findViewById(R.id.num_virus);
		TextView virus_name = (TextView) a.findViewById(R.id.virus_name);
		TextView virus_text = (TextView) a.findViewById(R.id.virus_text);
		LinearLayout container = (LinearLayout) a.findViewById(R.id.container);
		LinearLayout ui = (LinearLayout) a.findViewById(R.id.ui);
		final LinearLayout opt_container = (LinearLayout) a.findViewById(R.id.opt_container);
		final Spinner spin = (Spinner) a.findViewById(R.id.options);
		Button go = (Button) a.findViewById(R.id.go);

		if (num_virus > 0) {
			num_virux.setText("" + num_virus);
			Iterator<String> iterator = viruses.iterator();
			String all_viruses = "";
			ui.setBackgroundColor(0xffcc0000);
			if(num_virus >=2) virus_text.setText("viruses found!");
			while (iterator.hasNext()) {
				if (all_viruses.isEmpty())
					all_viruses = iterator.next();
				else
					all_viruses = all_viruses + ", " + iterator.next();
			}
			virus_name.setText(all_viruses);
			container.setVisibility(View.VISIBLE);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, actions);
			spin.setAdapter(adapter);
			
			go.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(spin.getSelectedItemPosition() == 0)
					{
						new File(uri.getPath()).delete();
						opt_container.setVisibility(View.GONE);
						Toast.makeText(context, "The infected file has been deleted!", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

}
