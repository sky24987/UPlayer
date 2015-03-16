package com.alphanetworks.uplayer.upnp.dmc;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import android.os.Handler;
import android.util.Log;

import com.alphanetworks.uplayer.upnp.UpnpUtils;

public class DeviceRegistryListener extends DefaultRegistryListener {
	private static final String LOG_TAG = DeviceRegistryListener.class
			.getSimpleName();

	private Handler mHandler = null;

	public DeviceRegistryListener(Handler handler) {
		mHandler = handler;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deviceAdded(Registry registry, Device device) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "One device is added."
				+ device.getDetails().getFriendlyName());
		if (device.hasServices())
			refreshDevice(device, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deviceRemoved(Registry registry, Device device) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "One device is removed.");
		if (device.hasServices())
			refreshDevice(device, false);
	}

	@SuppressWarnings("rawtypes")
	public void refreshDevice(Device device, boolean isAdd) {
		if (device.getType().getType().equalsIgnoreCase(UpnpUtils.DMR)) {
			if (isAdd)
				mHandler.sendMessage(mHandler.obtainMessage(
						UpnpUtils.REFRESH_DMR_LIST, 1, 0, device));
			else
				mHandler.sendMessage(mHandler.obtainMessage(
						UpnpUtils.REFRESH_DMR_LIST, -1, 0, device));
		} else if (device.getType().getType().equalsIgnoreCase(UpnpUtils.DMS)) {
			if (isAdd)
				mHandler.sendMessage(mHandler.obtainMessage(
						UpnpUtils.REFRESH_DMS_LIST, 1, 0, device));
			else
				mHandler.sendMessage(mHandler.obtainMessage(
						UpnpUtils.REFRESH_DMS_LIST, -1, 0, device));
		}
	}
}
