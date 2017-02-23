package com.antivirus.sxc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Dumper extends AsyncTask<Object, Integer, Boolean> {

	InputStream is;
	int i;
	FileOutputStream os;
	PrintStream out;
	File file, hex;
	Context context;
	double size;
	ProgressBar pb;
	TextView tv;
	Uri uri;
	
	public Dumper(Context c, File hex, File file, ProgressBar pb, TextView tv, Uri uri) {
		context = c;
		this.file = file;
		this.hex = hex;
		this.pb = pb;
		this.tv = tv;
		this.uri = uri;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		try {
			is = new FileInputStream(file);
			os = new FileOutputStream(hex);
			size = is.available();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i = 0;
		out = new PrintStream(os);
		tv.setText("Hex Dumping");
	}

	@Override
	protected Boolean doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		try {
			while (is.available() > 0) {
				StringBuilder sb1 = new StringBuilder();
				// out.printf("--- ", i * 16);
				for (int j = 0; j < 16; j++) {
					if (is.available() > 0) {
						int value = (int) is.read();
						sb1.append(String.format("%02X", value));
					}
				}
				out.print(sb1);
				i++;
				int per = 100 - (int)((is.available() * 100)/size);
				publishProgress(per);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		pb.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tester test = new Tester(context, pb, tv, uri);
		test.execute();
	}
}
