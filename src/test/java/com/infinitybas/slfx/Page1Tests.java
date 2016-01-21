package com.infinitybas.slfx;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.infinitybas.slfx.samples.Page1Controller;
import com.infinitybas.slfx.samples.SampleSLFXApplicationConfig;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * This is a demonstration of how to structure a UI test using SLFX.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SampleSLFXApplicationConfig.class)
public class Page1Tests extends GuiTest {

	@Autowired
	SLFX slfx;

	@Autowired
	FXMLLoaderService loader;

	@Autowired
	Page1Controller controller;

	@Test
	public void verifyText() {
		Text t = find("#text");
		assertSame(message, t.getText());

		Button btn = find("#btn");
		assertSame(btnText, btn.getText());
	}

	static String message = UUID.randomUUID().toString();
	static String btnText = UUID.randomUUID().toString();

	static Intent initialIntent = new Intent("fxml/page1.fxml").withExtra("message", message).withExtra("btnText",
			btnText);

	static String resource = "fxml/page1.fxml";

	@Override
	protected Parent getRootNode() {
		try {
			Parent root = (Parent) loader.load(resource);
			loader.registerIntent(resource, initialIntent);
			return root;
		} catch (IOException e) {
			return null;
		}
	}

}