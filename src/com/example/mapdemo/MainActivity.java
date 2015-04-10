package com.example.mapdemo;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;

public class MainActivity extends Activity implements OnMarkerClickListener,OnMapLoadedListener, InfoWindowAdapter {
	
	private MapView mapView;
	private AMap aMap;
	private MarkerOptions markerOption;
	
	private Marker marker2;// ������Ч����marker����
	private LatLng latlng = new LatLng(30.679879, 104.064855);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}
	
	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	private void setUpMap() {
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		addMarkersToMap();// ����ͼ�����marker
	}
	
	/**
	 * �ڵ�ͼ�����marker
	 */
	private void addMarkersToMap() {
		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(Constants.CHENGDU).title("�ɶ���")
				.snippet("�𾴵ĳ˿ͣ�\n������"+"�յ���Ͷ�߳��ƺ�Ϊ"+"�ĳ���"+"�ش˹������������£�\n"+"\n��л�������ǹ�����֧�֣�").draggable(true).icon(BitmapDescriptorFactory
				.fromResource(R.drawable.location)));

		markerOption = new MarkerOptions();
		markerOption.position(Constants.XIAN);
		markerOption.anchor(0.5f, 0.5f);
		markerOption.title("������").snippet("�𾴵ĳ˿ͣ�\n������"+"�յ���Ͷ�߳��ƺ�Ϊ"+"�ĳ���"+"�ش˹������������£�\n"+"\n��л�������ǹ�����֧�֣�");
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.location));
		marker2 = aMap.addMarker(markerOption);
		drawMarkers();// ���10������ϵͳĬ��icon��marker
	}
	
	/**
	 * ����ϵͳĬ�ϵ�1��marker����ͼƬ
	 */
	public void drawMarkers() {
		String data = "�𾴵ĳ˿ͣ�\n������"+"�յ���Ͷ�߳��ƺ�Ϊ"+"�ĳ���"+"�ش˹������������£�\n"+"\n��л�������ǹ�����֧�֣�";
		
		Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(Constants.ZHENGZHOU)
				.title(data).snippet(data)
				.draggable(true).icon(BitmapDescriptorFactory
				.fromResource(R.drawable.location)));
		marker.showInfoWindow();
	}
	
	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	
	@Override
	public boolean onMarkerClick(Marker marker) {
		if(marker.equals(marker2)) {
		if (aMap != null) {
			jumpPoint(marker);
		}
		}
		return false;
	}
	
	/**
	 * marker���ʱ����һ��
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(Constants.XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * Constants.XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * Constants.XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				aMap.invalidate();// ˢ�µ�ͼ
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});

	}

	@Override
	public void onMapLoaded() {
		// ��������maker��ʾ�ڵ�ǰ���������ͼ��
				LatLngBounds bounds = new LatLngBounds.Builder()
						.include(Constants.XIAN).include(Constants.CHENGDU)
						.include(Constants.ZHENGZHOU).build();
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}
	
	/**
	 * �Զ���infowinfow����
	 */
	public void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

}
