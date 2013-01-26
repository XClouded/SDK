package me.yongbo.sdk.service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.yongbo.sdk.modle.App;

public class PushService {
	
	public static List<App> getPushs() throws Exception {
		String jsonStr = HttpService.getPushs();
		Gson gson = new Gson();
		List<App> apps = new ArrayList<App>();
		App app = null;
		
		//将字符串转化为JsonElement
		JsonElement je = new JsonParser().parse(jsonStr);
		//得到json数组
		JsonArray jsonArray = je.getAsJsonArray();
		//遍历数组
		Iterator<JsonElement> it = jsonArray.iterator();
		while(it.hasNext()){
			JsonElement e = it.next();
			//JsonElement转换为JavaBean对象
			app = gson.fromJson(e, App.class);
			apps.add(app);
		}
		return apps;
	}
}
