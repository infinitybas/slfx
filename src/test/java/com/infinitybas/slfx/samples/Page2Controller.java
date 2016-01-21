package com.infinitybas.slfx.samples;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFXController;
import com.infinitybas.slfx.SLFXControllerFor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

@SLFXControllerFor("fxml/page2.fxml")
public class Page2Controller extends SLFXController implements Initializable {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(Page2Controller.class);

	@FXML TabPane tabPane;
	@FXML Text text;
	@FXML Button backBtn;
	@FXML Button forwardBtn;

	@Autowired HelloService helloService;

	@Override
	public void onShow(Intent intent) {

		log.info("{}", intent.getExtra(ArrayList.class, "tabs").get());

		Optional<List<String>> tab1name = intent.getExtra(ArrayList.class, "tabs");
		
		if (tab1name.isPresent()) {
			for (String s : tab1name.get()) {
				tabPane.getTabs().add(new Tab(s));
			}
		}

		text.setText(helloService.sayHello());
	}

	@FXML
	public void toPage1() {
		// Model values we left last time should still be there.
		slfx.show(new Intent("fxml/page1.fxml").withExtra("message", "Goodbye world"));
	}

	@FXML
	public void forward() {
		slfx.forward();
	}

	@FXML
	public void back() {
		slfx.back();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("{} {}", slfx.canGoForward().get(), slfx.canGoBackward().get());

		backBtn.disableProperty().bind(slfx.canGoBackward().not());
		forwardBtn.disableProperty().bind(slfx.canGoForward().not());
	}

}
