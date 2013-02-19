package com.viva.gallery.payment;

import java.math.BigDecimal;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;
 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MobilePaymentActivity extends Activity implements OnClickListener {
	
	   // The PayPal server to be used - can also be ENV_NONE and ENV_LIVE
		private static final int server = PayPal.ENV_SANDBOX;
		// The ID of your application that you received from PayPal
		private static final String appID = "APP-80W284485P519543T";
		// This is passed in for the startActivityForResult() android function, the value used is up to you
		private static final int request = 1;
				
		protected static final int INITIALIZE_SUCCESS = 0;
		protected static final int INITIALIZE_FAILURE = 1;
		
		BigDecimal amount = new BigDecimal(10);
		
		// You will need at least one CheckoutButton, this application has four for examples
		CheckoutButton launchSimplePayment;
  
        LinearLayout layout;
        Button button;
		
	  
		// This handler will allow us to properly update the UI. You cannot touch Views from a non-UI thread.
		Handler hRefresh = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
			    	case INITIALIZE_SUCCESS:
			    		Log.d("Handler", "Success"); 	 
			    		
			     		setupButtons();
			            break;
			    	case INITIALIZE_FAILURE:
			    		Log.d("Handler", "Failure");
			    		
			      		showFailure();
			    		break;
				}
			}
		};
		
	 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        button = (Button) findViewById(R.id.buttonPay);
        layout = (LinearLayout) findViewById(R.id.layout);
    }
    
    public void onClickPayButton(View v){
    	Toast t = Toast.makeText(getApplicationContext(), "Please Wait", Toast.LENGTH_SHORT);
    	t.show();
    	 
    	     // Initialize the library. We'll do it in a separate thread because it requires communication with the server
    			// which may take some time depending on the connection strength/speed.
    			Thread libraryInitializationThread = new Thread() {
    				public void run() {
    					initLibrary();
    					Log.d("Thread", "Initialization Complete");
    					// The library is initialized so let's create our CheckoutButton and update the UI.
    					if (PayPal.getInstance().isLibraryInitialized()) {
    						hRefresh.sendEmptyMessage(INITIALIZE_SUCCESS);
    					}
    					else {
    						hRefresh.sendEmptyMessage(INITIALIZE_FAILURE);
    					}  
    				}
    			};
    			
    			
    			libraryInitializationThread.start();
    			
    			 
    }
    
    private void initLibrary(){
    	
    	Log.d("InitLib", "Initializing Library"); 
    	 
    	PayPal pp = PayPal.getInstance();
		// If the library is already initialized, then we don't need to initialize it again.
		if(pp == null) {
			
			Log.d("PayPal", "Creating Paypal instance"); 
			// This is the main initialization call that takes in your Context, the Application ID, and the server you would like to connect to.
			pp = PayPal.initWithAppID(this, appID, server);
   			
			// -- These are required settings.
        	pp.setLanguage("en_US"); // Sets the language for the library.
        	// --
        	
        	// -- These are a few of the optional settings.
        	// Sets the fees payer. If there are fees for the transaction, this person will pay for them. Possible values are FEEPAYER_SENDER,
        	// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY.
        	pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER); 
        	// Set to true if the transaction will require shipping.
        	pp.setShippingEnabled(false);
        	// Dynamic Amount Calculation allows you to set tax and shipping amounts based on the user's shipping address. Shipping must be
        	// enabled for Dynamic Amount Calculation. This also requires you to create a class that implements PaymentAdjuster and Serializable.
        	pp.setDynamicAmountCalculationEnabled(false);
        	// --
		}
		 
    }
    
    public void setupButtons(){
    	
    	button.setVisibility(Button.GONE);
    	Log.d("PayPalButton", "Adding Button");
    	
    	PayPal pp = PayPal.getInstance();
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_152x33, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchSimplePayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layout.addView(launchSimplePayment);
    	
    }
    
    public void showFailure(){
    	Toast t = Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG);
    	t.show();
    }
    
   @Override
    public void onClick(View v) {
	 
	   Log.d("PayPalButton", "You Clicked Paypal Button");
	   
	           // Use our helper function to create the simple payment.
				PayPalPayment payment = exampleSimplePayment();	
				// Use checkout to create our Intent.
				
				//Can do it two ways : 1.Implementing ResultDelegate 2.OnActivityResult()
				
				//Intent checkoutIntent = PayPal.getInstance().checkout(payment, this);
 				Intent checkoutIntent = PayPal.getInstance().checkout(payment, this, new ResultDelegate());
				// Use the android's startActivityForResult() and pass in our Intent. This will start the library.
 		    	startActivityForResult(checkoutIntent, request);

    }
   
   private PayPalPayment exampleSimplePayment(){
	       // Create a basic PayPalPayment.
			PayPalPayment payment = new PayPalPayment();
			// Sets the currency type for this payment.
	    	payment.setCurrencyType("USD");
	    	// Sets the recipient for the payment. This can also be a phone number.
	    	payment.setRecipient("moviva_1328076397_biz@gmail.com");
	    	// Sets the amount of the payment, not including tax and shipping amounts.
	    	payment.setSubtotal(amount);
	    	// Sets the payment type. This can be PAYMENT_TYPE_GOODS, PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE.
	    	payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
	    	
	    	Log.d("payment", "Processing");
	    	
	    	return payment;
   }
   
   @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   launchSimplePayment.setVisibility(CompoundButton.INVISIBLE);
	   	  
	   switch(resultCode) {
		case Activity.RESULT_OK:
		    Toast t = Toast.makeText(getApplicationContext(), "Payment Successfull", Toast.LENGTH_LONG);
		    t.show();
		   
		   Log.d("ActivityResult", "Success"); 
		   break;
			
		case Activity.RESULT_CANCELED:
			 Toast t1 = Toast.makeText(getApplicationContext(), "Payment Canceled", Toast.LENGTH_LONG);
		     t1.show();
		     
		     Log.d("ActivityResult", "Canceled"); 
			 break;
			
		case PayPalActivity.RESULT_FAILURE:
		     Toast t2 = Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_LONG);
		     t2.show();
		   
		     Log.d("ActivityResult", "Failed"); 

			 break;
			 
		default :
		    Toast t3 = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
		    t3.show();
	
	   }
	   
	super.onActivityResult(requestCode, resultCode, data);
}
     
    
}