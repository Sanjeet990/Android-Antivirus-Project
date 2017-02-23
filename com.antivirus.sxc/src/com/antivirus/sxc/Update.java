package com.antivirus.sxc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class Update extends AsyncTask<Object, Integer, Boolean> {

	Context context;
	ProgressDialog pd;

	Update(Context c, ProgressDialog pd) {
		context = c;
		this.pd = pd;
	}

	@Override
	protected Boolean doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		int downloadedSize = 0;
		HttpURLConnection urlConnection;
		URL url;
		FileOutputStream fileOutput = null;
		InputStream inputStream = null;
		int totalSize = 0;
		try {
			url = new URL(
					"http://www.nlnetlabs.nl/downloads/antivirus/antivirus/virussignatures.strings");
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// connect
			urlConnection.connect();
			// set the path where we want to save the file
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// create a new file, to save the downloaded file
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File (sdCard.getAbsolutePath() + "/Antivirus");
			dir.mkdirs();
			File file = new File(dir, "myfile.hex");
			
			fileOutput = new FileOutputStream(file);

			// Stream used for reading the data from the internet
			inputStream = urlConnection.getInputStream();

			// this is the total size of the file which we are downloading
			totalSize = urlConnection.getContentLength();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pd.setMax(totalSize);

		byte[] buffer = new byte[1024];
		int bufferLength = 0;

		try {
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize = bufferLength;
				publishProgress(downloadedSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// close the output stream when complete //
		try {
			fileOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		pd.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pd.dismiss();
	}

}
