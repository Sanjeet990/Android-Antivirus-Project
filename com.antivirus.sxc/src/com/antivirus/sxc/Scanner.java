package com.antivirus.sxc;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Scanner extends ActionBarActivity {

	ProgressBar loader, progress;
	TextView actions, filename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		///Get controls
		loader = (ProgressBar) findViewById(R.id.loader);
		progress = (ProgressBar) findViewById(R.id.progress);
		actions = (TextView) findViewById(R.id.action);
		filename = (TextView) findViewById(R.id.filename);
		
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			handleSingle(intent);
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				// handleSendMultipleImages(intent); // Handle multiple images
				// being sent
			}
		} else {
			Toast.makeText(Scanner.this, "I/O Error!", Toast.LENGTH_SHORT)
					.show();
			finish();
		}

	}

	private void handleSingle(Intent intent) {
		// TODO Auto-generated method stub
		Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (uri != null) {
			// Update UI to reflect image being shared
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File (sdCard.getAbsolutePath() + "/Antivirus");
				dir.mkdirs();
				File file = new File(dir, "myfile.hex");
				
				String Fpath = uri.getPath() ;
				File filex = new File(Fpath);
				String fn = filex.getName();
				filename.setText(fn);
				//Update controls
				actions.setText("Dumping Hex");
				progress.setMax(100);
				progress.setProgress(0);
				SignatureUnpacker sup = new SignatureUnpacker(Scanner.this);
				sup.execute();
				Dumper hexdumper = new Dumper(Scanner.this, file, new File(uri.getPath()), progress, actions, uri);
				hexdumper.execute();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(Scanner.this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(Scanner.this, "I/O Error!", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

}
