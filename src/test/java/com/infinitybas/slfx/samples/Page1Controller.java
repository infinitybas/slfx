package com.infinitybas.slfx.samples;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFXController;
import com.infinitybas.slfx.SLFXControllerFor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

@SLFXControllerFor("fxml/page1.fxml")
public class Page1Controller extends SLFXController implements Initializable {

	@FXML Text text;
	@FXML Button btn;
	@FXML Button backBtn;
	@FXML Button forwardBtn;

	public void sayHello(ActionEvent action) {

		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("Tabs");
		tabs.add("Sent");
		tabs.add("From");
		tabs.add("Page");
		tabs.add("One");

		// Intent by annotated controller
		slfx.show(new Intent(Page2Controller.class).withExtra("tabs", tabs));
	}

	/**
	 * onShow is called when the linked view is shown by a call to slfx.show().
	 */
	@Override
	public void onShow(Intent intent) {

		Optional<String> message = intent.getExtra(String.class, "message");
		Optional<String> btnText = intent.getExtra(String.class, "btnText");

		if (message.isPresent()) {
			text.setText(message.get());
		}

		if (btnText.isPresent()) {
			btn.setText(btnText.get());
		}
	}

	@FXML
	public void back() {
		slfx.back();
	}

	@FXML
	public void forward() {
		slfx.forward();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		backBtn.disableProperty().bind(slfx.canGoBackward().not());
		forwardBtn.disableProperty().bind(slfx.canGoForward().not());
	}
}
