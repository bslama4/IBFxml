/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IB_App;
import IB_App.SimpleMovingAverage;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.TagValue;
import com.ib.contracts.StkContract;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
//import org.joda.time.DateTime;


/**
 * FXML Controller class
 *
 * @author Bishal
 */

class startTradeTask extends TimerTask
{

    public void run()
    {
        System.out.println("test1");
        //write your code here
    }
}

public class IB_AppFxmlController implements Initializable {

    //TextArea textArea = new TextArea();
    @FXML private Label label;
    @FXML private TextField instr;
    @FXML private TextField TimeInt;
    @FXML private TextField ObsTimeRange;
    @FXML private ComboBox<String> timeIntCbx;
    @FXML private ComboBox<String> instTypeCbx;
    @FXML private ComboBox<String> durCbx;
    @FXML public ComboBox<String> obsTimePeriod1Cbx;
    @FXML private ComboBox<String> obsTimePeriod2Cbx;
    @FXML private ComboBox<String> tradTimePeriod1Cbx;
    @FXML private ComboBox<String> tradTimePeriod2Cbx;
    @FXML private ComboBox<String> closeTimeCbx;
    @FXML private javafx.scene.control.Button closeButton;
    
    @FXML public TextArea Logs;
    
    private ObservableList<String> timeIntList = FXCollections.observableArrayList("1 min","2 mins","5 mins");
    private ObservableList<String> timePeriodList = FXCollections.observableArrayList("9","10","11","12","13","14","15","16");
    private ObservableList<String> timePeriodList1 = FXCollections.observableArrayList("10","11","12","13","14","15","16");
    private ObservableList<String> instType = FXCollections.observableArrayList("Stocks", "Futures", "Cash");
    private ObservableList<String> durStr = FXCollections.observableArrayList("1 M", "10 D", "1 D", "1 hour");
    public EClientSocket m_client;// = wrapper.getClient();
    public EReaderSignal m_signal;// = wrapper.getSignal();
    private List<TagValue> m_mktDataOptions = new ArrayList<>();;
    private int m_tickId = 1;
    Timer timer = new java.util.Timer();
    
    @FXML
    private void handleDisconnect(ActionEvent event) {
        m_client.eDisconnect();
        System.out.println("Client disconnected\n");
    }   
    
    @FXML
    private void handleButtonConnect(ActionEvent event) throws ParseException {
        m_client.eConnect("127.0.0.1", 7497, 2);
        if(!(m_client.isConnected()))
                {
                    this.Logs.appendText("Could not connect to TWS"+ "\n");
                }
		//! [connect]
		//! [ereader]
		final EReader reader = new EReader(m_client, m_signal);   
		
		reader.start();
		//An additional thread is created in this program design to empty the messaging queue
		new Thread(() -> {
		    while (m_client.isConnected()) {
		        m_signal.waitForSignal();
		        try {
		            reader.processMsgs();
		        } catch (Exception e) {
		            System.out.println("Exception: "+e.getMessage());
		        }
		    }
		}).start();
                try {
                    //! [ereader]
                    // A pause to give the application time to establish the connection
                    // In a production application, it would be best to wait for callbacks to confirm the connection is complete
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IB_AppFxml.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    @FXML
    private void handleButtonReqData(ActionEvent event) throws ParseException
    {
        //xoxo
        //timeIntCbx 
        System.out.println("Requesting data for " + instr.getText());
        Contract contract = new Contract();
        contract.symbol(instr.getText());
        contract.secType(instTypeCbx.getValue());
        contract.currency("USD");
        contract.exchange("SMART");
        m_client.reqHistoricalData(4001, contract, "", durCbx.getSelectionModel().getSelectedItem(), timeIntCbx.getSelectionModel().getSelectedItem(), "MIDPOINT", 1, 1, false, null);
        m_client.reqMktData(m_tickId++, contract, "", false, false, m_mktDataOptions);
   }
    
    @FXML
    private void handleButtonAcctDetails(ActionEvent event) throws ParseException {
        m_client.reqAccountSummary(9004, "All", "BuyingPower");
    }
        
    @FXML
    private void handleButtonAction(ActionEvent event) throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
        
        Integer duration =  Integer.parseInt(obsTimePeriod2Cbx.getValue()) - Integer.parseInt(obsTimePeriod1Cbx.getValue());
        duration = duration * 3600;
        System.out.println("values are - " + instr.getText() + " " + obsTimePeriod1Cbx.getValue() + " " + obsTimePeriod2Cbx.getValue() + " " + tradTimePeriod1Cbx.getValue() + " " + timeIntCbx.getSelectionModel().getSelectedItem() + " " + duration);
        
                Date date1 = new Date();
                String modifiedDate1= new SimpleDateFormat("yyyyMMdd").format(date);
                String queryTime = modifiedDate1 + " " + obsTimePeriod2Cbx.getValue() + ":00:00";
                String tradTime = modifiedDate1 + " " + tradTimePeriod1Cbx.getValue() + ":00:00";
                System.out.println("obsStartTime and tradeStartTime are " + queryTime + " " + tradTime);
                Date tradeDate = dateFormatter.parse(tradTime);
                timer.schedule(new startTradeTask(), tradeDate);
    
                //DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                //Date tradeDate = dateFormatter.parse("16:57:00");//(modifiedDate + " " + tradTimePeriod1Cbx.getValue() + ":00:00");
                //Logs.appendText("date is " + queryTime + "\n");
                Contract contract = new Contract();
		/*contract.symbol("AAPL");
		contract.secType("STK");
		contract.currency("USD");
		contract.exchange("SMART");*/
		// Specify the Primary Exchange attribute to avoid contract ambiguity
		// (there is an ambiguity because there is also a MSFT contract with primary exchange = "AEB")
		//contract.primaryExch("ISLAND");
                /*contract.symbol("EUR");
		contract.secType("CASH");
		contract.currency("GBP");
		contract.exchange("IDEALPRO");*/
                //m_client.reqHistoricalData(4001, contract, queryTime, duration.toString(), timeIntCbx.getSelectionModel().getSelectedItem(), "MIDPOINT", 1, 1, false, null);
    }
    
    
    @FXML
    public void exitApplication(ActionEvent event) {
        System.out.println("exiting app");
        Platform.exit();
    }
    
        @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage)closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    public void test(){ System.out.println("test");}
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        timeIntCbx.setItems(timeIntList);
        timeIntCbx.getSelectionModel().selectFirst();
        instTypeCbx.setItems(instType);
        instTypeCbx.getSelectionModel().selectFirst();
        durCbx.setItems(durStr);
        durCbx.getSelectionModel().selectFirst();
        /*obsTimePeriod1Cbx.setItems(timePeriodList);
        timePeriodList.remove(1);
        obsTimePeriod2Cbx.setItems(timePeriodList1);
        //timeIntCbx.getSelectionModel().selectFirst();
        obsTimePeriod1Cbx.getSelectionModel().selectFirst();
        obsTimePeriod2Cbx.getSelectionModel().selectFirst();
        obsTimePeriod2Cbx.setItems(timePeriodList1);
        tradTimePeriod1Cbx.setItems(timePeriodList);
        tradTimePeriod2Cbx.setItems(timePeriodList1);
        tradTimePeriod1Cbx.getSelectionModel().selectFirst();
        tradTimePeriod2Cbx.getSelectionModel().selectFirst();
        closeTimeCbx.setItems(timePeriodList1);
        closeTimeCbx.getSelectionModel().selectFirst();*/
        IB_App.EWrapperImpl wrapper = new IB_App.EWrapperImpl(this.Logs);
        m_client = wrapper.getClient();
        //final EReaderSignal m_signal = wrapper.getSignal();
        m_signal = wrapper.getSignal();
        Logs.setScrollTop(Double.MAX_VALUE);
    }
}
