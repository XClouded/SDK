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
		
		//���ַ���ת��ΪJsonElement
		JsonElement je = new JsonParser().parse(jsonStr);
		//�õ�json����
		JsonArray jsonArray = je.getAsJsonArray();
		//��������
		Iterator<JsonElement> it = jsonArray.iterator();
		while(it.hasNext()){
			JsonElement e = it.next();
			//JsonElementת��ΪJavaBean����
			app = gson.fromJson(e, App.class);
			apps.add(app);
		}
		return apps;
	}
}
