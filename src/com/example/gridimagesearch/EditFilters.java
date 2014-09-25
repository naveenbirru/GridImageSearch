package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gridimagesearch.models.SearchFilters;

public class EditFilters extends Activity {
	public SearchFilters filters;
	private Spinner imgType;
	private Spinner imgColor;
	private Spinner imgSize;
	private EditText etSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_filters);
		filters = (SearchFilters) getIntent().getSerializableExtra("filters");
		setupViews();
	}

	public void onFiltersDone(View v) {
		// TODO Auto-generated method stub
		filters.imgColor = String.valueOf(imgColor.getSelectedItem());
		filters.imgSize = String.valueOf(imgSize.getSelectedItem());
		filters.imgType = String.valueOf(imgType.getSelectedItem());
		filters.siteSearch = etSite.getText().toString();
		String query = filters.imgColor + filters.imgSize + filters.imgType + filters.siteSearch;
		//Toast.makeText(this, "Search for "+ query, Toast.LENGTH_SHORT).show();
		sendData();

}
	private void sendData() {
		Intent data = new Intent();
		data.putExtra("imgColor", filters.imgColor);
		data.putExtra("imgSize", filters.imgSize);
		data.putExtra("imgType", filters.imgType);
		data.putExtra("siteSearch", filters.siteSearch);
		setResult(RESULT_OK, data);
		finish();
		
	}
	public void onClearFilters(View v) {
		// TODO Auto-generated method stub
		filters.clear();
		sendData();
	}
	
	private void setupViews(){
		etSite = (EditText) findViewById(R.id.etSite);
		imgType = (Spinner) findViewById(R.id.imgTypeSpinner);
		imgColor = (Spinner) findViewById(R.id.imgColorSpinner);
		imgSize = (Spinner) findViewById(R.id.imgSizeSpinner);
		
		etSite.setText(filters.siteSearch);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_filters, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
