package com.infinitybas.slfx.samples;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.infinitybas.slfx.FXControllerFor;
import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFX;
import com.infinitybas.slfx.SLFXController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

@FXControllerFor("fxml/page1.fxml")
public class Page1Controller extends SLFXController {

	@FXML
	Text text;

	@FXML
	Button btn;

	@Autowired
	SLFX slfx;

	public void sayHello(ActionEvent action) {

		ArrayList<String> tabs =  new ArrayList<String>();
		tabs.add("One");
		tabs.add("Two");
		tabs.add("Three");
		
		// Intent by annotated controller
		slfx.show(new Intent(Page2Controller.class)
				.withExtra("tabs",tabs)
				.withExtra("message", "And this is page two."));
	}

	/**
	 * onShow is called when the linked view is shown by a call to slfx.show().
	 */
	@Override
	public void onShow(Intent intent) {

		Optional<String> message = intent.<String> getExtra(String.class, "message");
		Optional<String> btnText = intent.<String> getExtra(String.class, "btnText");

		if (message.isPresent()) {
			text.setText(message.get());
		}

		if (btnText.isPresent()) {
			btn.setText(btnText.get());
		}
	}
}
