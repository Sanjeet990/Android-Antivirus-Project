package com.antivirus.sxc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SignatureUnpacker extends AsyncTask {

	Context context;

	public SignatureUnpacker(Context c) {
		context = c;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		File f = new File(Environment.getExternalStorageDirectory().toString()
				+ "/Antivirus/signature.db");
		if (!f.exists()) {
			InputStream in = null;
			OutputStream out = null;
			AssetManager assetManager = context.getAssets();
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File (sdCard.getAbsolutePath() + "/Antivirus");
				dir.mkdirs();
				in = assetManager.open("signature.db"); // if files
																// resides
																// inside the
																// "Files"
																// directory
																// itself
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/Antivirus/signature.db");
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
		}
		return null;
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

}
