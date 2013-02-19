

package com.viva.gallery.payment;

import java.io.Serializable;

import android.util.Log;

import com.paypal.android.MEP.PayPalResultDelegate;

public class ResultDelegate implements PayPalResultDelegate, Serializable {
 
	private static final long serialVersionUID = 10001L;

	/**
	 * Notification that the payment has been completed successfully.
	 * 
	 * @param payKey			the pay key for the payment
	 * @param paymentStatus		the status of the transaction
	 */
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
		
		Log.d("PaymentSuccess", paymentStatus);
		/*
		SimpleDemo.resultTitle = "SUCCESS";
		SimpleDemo.resultInfo = "You have successfully completed your transaction.";
		SimpleDemo.resultExtra = "Key: " + payKey;
		*/
	}

	/**
	 * Notification that the payment has failed.
	 * 
	 * @param paymentStatus		the status of the transaction
	 * @param correlationID		the correlationID for the transaction failure
	 * @param payKey			the pay key for the payment
	 * @param errorID			the ID of the error that occurred
	 * @param errorMessage		the error message for the error that occurred
	 */
	public void onPaymentFailed(String paymentStatus, String correlationID,
			String payKey, String errorID, String errorMessage) {
		
		Log.d("PaymentFailed", paymentStatus);
		/*
		SimpleDemo.resultTitle = "FAILURE";
		SimpleDemo.resultInfo = errorMessage;
		SimpleDemo.resultExtra = "Error ID: " + errorID + "\nCorrelation ID: "
				+ correlationID + "\nPay Key: " + payKey;
				*/
	}

	/**
	 * Notification that the payment was canceled.
	 * 
	 * @param paymentStatus		the status of the transaction
	 */
	public void onPaymentCanceled(String paymentStatus) {
		/*
		SimpleDemo.resultTitle = "CANCELED";
		SimpleDemo.resultInfo = "The transaction has been cancelled.";
		SimpleDemo.resultExtra = "";
		*/
		Log.d("PaymentCanceled", paymentStatus);
	}
	
	 
}