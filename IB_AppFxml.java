/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IB_App;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.ib.client.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;





/**
 *
 * @author Bishal
 */
public class IB_AppFxml extends Application {
    
    TextArea textArea;
    static IB_AppFxmlController controller;
    static EClientSocket m_client;
    static EReaderSignal m_signal;
    
                
    public EClientSocket getClient()
    {
        return m_client;
    }
    public void setClient( EClientSocket client)
    {
        m_client = client;
    }
    
    public EReaderSignal getSignal()
    {
        return m_signal;
    }
    public void setSignal( EReaderSignal signal)
    {
        m_signal = signal;
    }
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException, IOException {
             
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("IB_AppFxml.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IB_AppFxml.fxml"));
        //loader.setController(this);
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        textArea = new TextArea();
        //EWrapperImpl wrapper = new EWrapperImpl(textArea);
		
	//final EClientSocket m_client = wrapper.getClient();
        //final EReaderSignal m_signal = wrapper.getSignal();
        

        IB_AppFxmlController fxmlController = new IB_AppFxmlController();
        loader.setController(fxmlController);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(final WindowEvent event) {
            /*if(wrapper.getClient().isConnected())
            {
                System.out.println("Client is being disconected");
                wrapper.getClient().eDisconnect();
            }
            else
            {
                System.out.println("Client is not conected");
            }*/
            //m_client.eDisconnect();
            System.out.println("closing the app");
            Platform.exit();
            System.out.println("done");
            primaryStage.close();
            System.out.println("done1");
            System.exit(0);
        }
        });
        
        System.out.println("setting textarea to false");
        textArea.setEditable(false);
        System.out.println("done");
    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
