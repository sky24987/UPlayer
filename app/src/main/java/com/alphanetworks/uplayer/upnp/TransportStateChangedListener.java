package com.alphanetworks.uplayer.upnp;

import org.fourthline.cling.support.model.TransportState;

public interface TransportStateChangedListener {
	
	public void transportStateChanged(TransportState state);
}
