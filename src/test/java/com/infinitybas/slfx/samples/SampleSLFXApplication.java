package com.infinitybas.slfx.samples;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.infinitybas.slfx.Intent;
import com.infinitybas.slfx.SLFX;

import javafx.application.Application;
import javafx.stage.Stage;

public class SampleSLFXApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("resource")
	@Override
	public void start(Stage primaryStage) throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SampleSLFXApplicationConfig.class);
		SLFX slfx = ctx.getBean(SLFX.class);
		slfx.setPrimaryStage(primaryStage);

		// Intent from FXML string
		// Add model parameters if here if you wish!
		slfx.show(new Intent("fxml/page1.fxml")
				.withExtra("message", "This is the first page") 	
				.withExtra("btnText", "Press me!"));
		
		primaryStage.show();

	}

}
