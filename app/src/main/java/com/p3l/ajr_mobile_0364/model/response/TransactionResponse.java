package com.p3l.ajr_mobile_0364.model.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.Transaction;

public class TransactionResponse{

	private String message;
	@SerializedName("data")
	private Transaction transaction;
	@SerializedName("data_array")
	private List<Transaction> transactionList;


	public TransactionResponse(String message, Transaction transaction) {
		this.message = message;
		this.transaction = transaction;
	}

	public TransactionResponse(String message, List<Transaction> transactionList) {
		this.message = message;
		this.transactionList = transactionList;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
}