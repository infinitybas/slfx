# slfx - Spring Loaded JavaFX (v0.1.1)

Spring Loaded JavaFX (slfx) is a lightweight library for integrating Spring with JavaFX. It strives to allow quick development of simple multi-view applications, inspired by the `Intent` pattern familiar to Android developers. It is designed with single-`Stage` applications in mind. If you wish to manage additional `Stage`s (e.g. modals), you are responsible for their lifecycles.

#### Features
* Spring managed controllers - use `@Autowired` to inject services into your controllers
* Inject controllers into one another!
* `Intent` pattern view switching with model parameters
* View history navigation with forward and back

#### SampleSLFXApplication.java  
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
    		  .withExtra("btnText",	"Press me!"));
    
    		primaryStage.show();
    	}
  
    }

#### SampleSLFXApplicationConfig.java
    @Configuration
    @Import(SLFXConfig.class)
    public class SampleSLFXApplicationConfig {
    	
    	// Your configuration here
    
    }
#### Page1Controller.java  
    @SLFXControllerFor("fxml/page1.fxml")
    public class Page1Controller extends SLFXController {
    
      @FXML Text text;
      @FXML Button btn;
	    
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
