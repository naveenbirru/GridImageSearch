package com.example.gridimagesearch.activites;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.gridimagesearch.EditFilters;
import com.example.gridimagesearch.ImageDisplayActivity;
import com.example.gridimagesearch.R;
import com.example.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.gridimagesearch.models.ImageResult;
import com.example.gridimagesearch.models.SearchFilters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class Search extends Activity {
	
	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	public SearchFilters filters;
	public String googleQuery;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        filters = new SearchFilters(null,null,null, null);
        gvResults.setAdapter(aImageResults);
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
            
	    	Log.d("INFO", "page = " + page + "and total " + totalItemsCount );
	    	// Add whatever code is needed to append new items to your AdapterView
	        customLoadMoreDataFromApi(page, totalItemsCount); 
                // or customLoadMoreDataFromApi(totalItemsCount); 
	    }
        });
    }
    
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset, int totalItemsCount) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	int current_index = totalItemsCount+offset + 1;
    	String addQuery = "&start=" + current_index;
    	
    	client.get(googleQuery + addQuery, new JsonHttpResponseHandler(){
            
            @Override
            public void onSuccess(int statusCode, Header[] headers,
            		JSONObject response) {
            	JSONArray imageResultsJson = null;
            	//Log.d("Debug", response.toString());
            	try {
    				imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
    				//aImageResults.clear();
    				//when we make changes to adapter, it does modify the underlying data
    				aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	//Log.d("INFO", imageResults.toString());
            }
            });
    
    }

    private void setupViews(){
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Search.this, ImageDisplayActivity.class);
				ImageResult result = imageResults.get(position);
				i.putExtra("result", result);//need to serializable or parceble.
				startActivity(i);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
    	// Inflate the menu; this adds items to the action bar if it is present.
        
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.search, menu);
        //return super.onCreateOptionsMenu(menu);
    	
    	getMenuInflater().inflate(R.menu.filter_menu, menu);
        
        return true;
    }
    
    public void onImageSearch(View v) {
    	String query = etQuery.getText().toString();
    	//Toast.makeText(this, "Search for "+ query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q="+ query;
        String addfilters = filters.getSearchFilters();
        if(addfilters == null) {
        	 googleQuery = searchUrl;
        }else {
        	 googleQuery = searchUrl + addfilters;
        }
        //searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android" ;
       //Toast.makeText(this, "Search for "+ googleQuery, Toast.LENGTH_SHORT).show();
        
        client.get(searchUrl, new JsonHttpResponseHandler(){
        
        @Override
        public void onSuccess(int statusCode, Header[] headers,
        		JSONObject response) {
        	JSONArray imageResultsJson = null;
        	Log.d("Debug", response.toString());
        	try {
				imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
				//imageResults.clear();
				//when we make changes to adapter, it does modify the underlying data
				aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Log.d("INFO", imageResults.toString());
        }
        });
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
        case R.id.action_settings: 
        	return true;
        case R.id.miSettings: 
        	edit_filters();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }     
    }

	private void edit_filters() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Search ", Toast.LENGTH_SHORT).show();
		 
		Intent i = new Intent(Search.this, EditFilters.class);
		//ImageResult result = imageResults.get(position);
		i.putExtra("filters", filters);//need to serializable or parceble.
		startActivityForResult(i, 20);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		 if (resultCode == RESULT_OK && requestCode == 20) {
//			 	SearchFilters edfilters = (SearchFilters) data.getExtras().getSerializable("filters");
//				String query = edfilters.imgColor + edfilters.imgSize + edfilters.imgType + edfilters.siteSearch;
			 	filters.imgColor = data.getExtras().getString("imgColor");
			 	filters.imgSize = data.getExtras().getString("imgSize");
			 	filters.imgType = data.getExtras().getString("imgType");
			 	filters.siteSearch = data.getExtras().getString("siteSearch"); 	
				//Toast.makeText(this, "Search for "+ edit_filters, Toast.LENGTH_SHORT).show();
			 	View v = this.findViewById(android.R.id.content);
			 	onImageSearch(v);
			 
		  }
	}

}
