package com.iiijiaban.hairstyle.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.iiijiaban.hairstyle.beans.DuitangImageBean;
import com.iiijiaban.hairstyle.beans.Picture;
import com.iiijiaban.hairstyle.beans.YmtBean;

public class PictureDao {

	/**
	 * 获取某一专辑的图片集
	 * @param cid 专辑id
	 * @param pageNo 第几页
	 * @return
	 */
	public static List<Picture> getPicsByAlbIdUrl(String cid,String pageNo){
		String url ="http://www.iiijiaba.com/hairstyle/HAIRSTYLE-BP?phonetype=getpicbycat"+"&pageNo="+pageNo+"&cid="+cid; 
		String json ="";
		JSONArray jsonArray = null;
		List<Picture> pics = new ArrayList<Picture>();
		
		try {
			json = CommonDao.getDataFromServer(url); 
		} catch (Exception e) { 
			// TODO 网络出现异常
			System.out.println(e.getMessage().toString());
		}
		try {
			jsonArray = new JSONArray(json);
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Picture pic = new Picture();
				pic.setPid(obj.getString("pid"));
				pic.setPaddr(obj.getString("paddr"));
				pic.setCid(obj.getString("cid"));
				pics.add(pic);
			} 
		} catch (JSONException e) {  
			// TODO json转换错误   
			System.out.println(e.getMessage().toString());
		} 
		return pics;
	}
	   public static  ArrayList<DuitangImageBean> getDuitangsbyPage(int page){
//	        String  url="http://www.duitang.com/search/?kw=%E5%8F%91%E5%9E%8B&type=feed&page="+page;
		   String url="http://www.duitang.com/search/?kw=%E7%BE%8E%E5%8F%91&type=feed#!s-p"+page;
//	        String url="http://www.topit.me/items/search?query=%E6%9E%97%E4%BF%8A%E6%9D%B0";
		   ArrayList<DuitangImageBean>  images =new ArrayList<DuitangImageBean>();
	        try {
	            Document doc=Jsoup.connect(url).timeout(10000).get();
//	            Elements eee=doc.select("div[class=catalog]");
	            Element element0=doc.select("div[class=pbr woo-mpbr]").last();
	            Element element00=element0.select("li[class=pbrw]").first();
	            Element element000=element00.select("span").first();

	            String pages=element000.text();
	            Elements htmlimages=doc.select("div[class=woo]");
	            for(Element e:htmlimages){
	                DuitangImageBean imageBean=new DuitangImageBean();
	                imageBean.setPages(pages);
	                Element element1=e.select("div[class=mbpho]").first();
	                Element element2=element1.getElementsByTag("a").first();
	                Element element3=element2.getElementsByTag("img").first();
	                imageBean.setId(element3.attr("data-rootid"));
	                imageBean.setDesc(element3.attr("alt"));
	                imageBean.setThumburl(element3.attr("src"));
	                images.add(imageBean);


	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return  images;
	    }
	   public static ArrayList<DuitangImageBean> getymtImages(){
		   String url="http://www.topit.me/items/search?query=%E6%9E%97%E4%BF%8A%E6%9D%B0&p=1";
		   ArrayList<DuitangImageBean>  images =new ArrayList<DuitangImageBean>();
		   Document doc;
		try {
			YmtBean  ymtBean=new YmtBean();
			doc = Jsoup.connect(url).timeout(10000).get();
			   Element catalog=doc.select("div[class=catalog]").first();
			   Elements  ems=catalog.select("div[class=e m]");
			   for(Element e:ems){
				   Element ymt=e.getElementsByTag("a").last();
				   Element img=ymt.select("img[class=img]").first();
				   ymtBean.setMurl(img.attr("src"));
				   ymtBean.setTitle(img.attr("alt"));
				   
				   
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		   return null;
	   }
}
