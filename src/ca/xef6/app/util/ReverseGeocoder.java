package ca.xef6.app.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocoder {

    public static interface Callback {
	void onAddressSuggestionsLoaded(List<String> addressSuggestions);
    }

    private static String downloadUrl(String url) {
	String result = null;
	try {
	    HttpClient client = new DefaultHttpClient();
	    HttpGet request = new HttpGet(url);
	    HttpResponse response = client.execute(request);
	    HttpEntity entity = response.getEntity();
	    result = EntityUtils.toString(entity);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return result;
    }

    public static List<String> getAddressSuggestions(LatLng position, boolean sensor) {
	String json = downloadUrl(buildUrl(position, sensor));
	List<String> list = null;
	if (json != null) {
	    list = parseJsonToList(json);
	}
	return list;
    }

    private static class DlTask extends AsyncTask<String, Void, List<String>> {

	private final Callback callback;
	private final LatLng   position;
	private final boolean  sensor;

	public DlTask(LatLng position, boolean sensor, Callback callback) {
	    this.position = position;
	    this.sensor = sensor;
	    this.callback = callback;
	}

	@Override
	protected List<String> doInBackground(String... params) {
	    List<String> data = getAddressSuggestions(position, sensor);
	    return data;
	}

	@Override
	protected void onPostExecute(List<String> result) {
	    if (callback != null) {
		callback.onAddressSuggestionsLoaded(result);
	    }
	}

    }

    public static void getAddressSuggestionsAsync(LatLng position,
						  boolean sensor,
						  Callback callback) {
	new DlTask(position, sensor, callback).execute();
    }

    private static String buildUrl(LatLng position, boolean sensor) {
	String url = "https://maps.googleapis.com/maps/api/geocode/json";
	url += "?latlng=" + position.latitude + "," + position.longitude;
	url += "&sensor=" + (sensor ? "true" : "false");
	url += "&key=" + "AIzaSyAPEEZJXqLi7mobq0bwjKHMSkVODHYDzeo";
	return url;
    }

    private static List<String> parseJsonToList(String json) {
	List<String> list = new ArrayList<String>();
	JSONTokener tokener = new JSONTokener(json);
	try {
	    JSONObject obj = (JSONObject) tokener.nextValue();
	    JSONArray results = obj.getJSONArray("results");
	    for (int i = 0; i < results.length(); ++i) {
		JSONObject result = results.getJSONObject(i);
		String fa = result.getString("formatted_address");
		Log.w("parseJsonToList", fa);
		list.add(fa);
	    }
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	return list;
    }
}
