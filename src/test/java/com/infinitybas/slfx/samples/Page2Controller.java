package com.infinitybas.slfx.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.infinitybas.slfx.SLFXControllerFor;
import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFX;
import com.infinitybas.slfx.SLFXController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

@SLFXControllerFor("fxml/page2.fxml")
public class Page2Controller extends SLFXController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(Page2Controller.class);

	@FXML TabPane tabPane;
	@FXML Text text;
	
	@Autowired SLFX slfx;

	@Override
	public void onShow(Intent intent) {
		
		log.info("{}", intent.getExtra(ArrayList.class, "tabs").get());

		Optional<List<String>> tab1name = intent.getExtra(ArrayList.class, "tabs");

		if(tab1name.isPresent()) {
			for(String s : tab1name.get()) {
				tabPane.getTabs().add(new Tab(s));
			}
		}
		
		Optional<String> message = intent.getExtra(String.class, "message");
		
		if(message.isPresent()) {
			text.setText(message.get());
		}
		
	}

	@FXML public void toPage1() {
		// Model values we left last time should still be there.
		slfx.show(new Intent("fxml/page1.fxml")
				.withExtra("message", "Goodbye world"));
	}
	
}
