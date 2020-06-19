package com.example.hbase.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;

public class FxApplication extends Application {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private long startTime = System.currentTimeMillis();

    private volatile static ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        logger.info("Javafx application initialize at: " + System.currentTimeMillis() + ", " + new Date());
        startTime = System.currentTimeMillis();
        String[] args = getParameters().getRaw().toArray(new String[0]);
        Thread thread = new Thread(()-> {
            applicationContext = new SpringApplicationBuilder()
                    .sources(ClientApplication.class)
                    .run(args);
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_view.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        logger.info("Javafx stage showing at: " + System.currentTimeMillis() + ", " + new Date());
        logger.info("Javafx startup time: " + (System.currentTimeMillis() - startTime) + " ms");
    }


    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    public static synchronized ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
