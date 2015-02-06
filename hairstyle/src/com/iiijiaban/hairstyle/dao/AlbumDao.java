package com.iiijiaban.hairstyle.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iiijiaban.hairstyle.beans.Album;
import com.iiijiaban.hairstyle.beans.Picture;

public class AlbumDao {

	/**
	 * ��ȡ���е�ר�������ƺ�id��
	 * @return
	 */
	public static List<Album> getAllAlbums(){
		/** ר����ַ	 */
		String url ="http://www.iiijiaba.com/hairstyle/HAIRSTYLE-BP?phonetype=getallcategory"; 
		String json ="";
		JSONArray jsonArray = null;
		List<Album> albums = new ArrayList<Album>();
		
		try {
			json = CommonDao.getDataFromServer(url); 
		} catch (Exception e) { 
			// TODO ��������쳣
			System.out.println(e.getMessage().toString());
		}
		try {
			jsonArray = new JSONArray(json);
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Album album = new Album(); 
				album.setCid(obj.getString("cid"));
				album.setCname(obj.getString("cname"));
				albums.add(album);
			} 
		} catch (JSONException e) {  
			// TODO jsonת������   
			System.out.println(e.getMessage().toString());
		} 
		return albums;
	}
}
