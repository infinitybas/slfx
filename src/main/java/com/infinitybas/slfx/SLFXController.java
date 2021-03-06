package com.infinitybas.slfx;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;

@Lazy
@Scope("singleton")
@Controller
public abstract class SLFXController implements IntentAware {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SLFXController.class);

	@FXML Parent root;
	Intent intent;
	
	@Autowired
	protected SLFX slfx;
	
	protected Parent getRoot() {
		return root;
	}
	
	public void setIntent(Intent intent) {
		if(log.isDebugEnabled()) {
			log.debug("\n{}", intent.toString());
		}
		this.intent = intent;
		onShow(intent);
	}
	
	public void onShow(Intent intent) {

	}
}
