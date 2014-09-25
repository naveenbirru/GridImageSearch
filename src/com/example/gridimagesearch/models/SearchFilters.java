package com.example.gridimagesearch.models;

import java.io.Serializable;

public class SearchFilters implements Serializable{

	private static final long serialVersionUID = -2149751218857070602L;
	public String imgSize;
	public String imgColor;
	public String imgType;
	public String siteSearch;
	
	public SearchFilters(String imgSize, String imgColor, String imgType, 
			String siteSearch) {
		// TODO Auto-generated constructor stub
		this.imgSize = imgSize;
		this.imgColor = imgType;
		this.imgType = imgType;
		this.siteSearch = siteSearch;
	}
	
	public String getSearchFilters() {
		String filters = null;
        
		if(imgSize!=null) {
			filters = "&imgsz=" + imgSize;
		}
		if(imgColor  !=null) {
			filters = filters + "&imgcolor=" + imgColor;
		}
		if(imgType !=null) {
			filters = filters + "&imgtype=" + imgType;
		}
		if(siteSearch!= null){
			if(siteSearch.length() != 0){
			filters = filters + "&as_sitesearch=" + siteSearch;  
		}
		}
		
		return filters;
	}
	public void clear() {
		// TODO Auto-generated method stub
		
		imgSize = null;
		imgColor = null;
		imgType = null;
		siteSearch = null;
	}
}
