package com.infinitybas.slfx.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.infinitybas.slfx.FXControllerFor;
import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFX;
import com.infinitybas.slfx.SLFXController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

@FXControllerFor("fxml/page2.fxml")
public class Page2Controller extends SLFXController {

	@FXML TabPane tabPane;
	@FXML Text text;
	
	@Autowired SLFX slfx;

	@Override
	public void onShow(Intent intent) {

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
		// TODO: Model values we left last time should still be there.
		// Need to implement some sort of caching in SLFXImpl
		slfx.show(new Intent(Page1Controller.class));
	}
	
}
