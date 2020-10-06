package com.admin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TABLE149")
public class EjModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "T149C1")
	private Integer id;

	@Column(name = "T149C2")
	private String Transdate;

	@Column(name = "T149C3")
	private String Time;

	@Column(name = "T149C4")
	private String Terminal_ID;

	@Column(name = "T149C5")
	private String AccountNo;

	@Column(name = "T149C6")
	private String CardNumber;

	@Column(name = "T149C7")
	private String TraceNo;

	@Column(name = "T149C8")
	private String Amount;

	@Column(name = "T149C9")
	private String WithdrawalFlag;

	@Column(name = "T149C10")
	private String Response;

	@Column(name = "T149C11")
	private String ReversalFlag;

	@Column(name = "T149C12")
	private String Db_Cr;

	@Column(name = "T149C13")
	private String TransType;

	@Column(name = "T149C14")
	private String MerchantType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransdate() {
		return Transdate;
	}

	public void setTransdate(String transdate) {
		Transdate = transdate;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getTerminal_ID() {
		return Terminal_ID;
	}

	public void setTerminal_ID(String terminal_ID) {
		Terminal_ID = terminal_ID;
	}

	public String getAccountNo() {
		return AccountNo;
	}

	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}

	public String getCardNumber() {
		return CardNumber;
	}

	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	public String getTraceNo() {
		return TraceNo;
	}

	public void setTraceNo(String traceNo) {
		TraceNo = traceNo;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getWithdrawalFlag() {
		return WithdrawalFlag;
	}

	public void setWithdrawalFlag(String withdrawalFlag) {
		WithdrawalFlag = withdrawalFlag;
	}

	public String getResponse() {
		return Response;
	}

	public void setResponse(String response) {
		Response = response;
	}

	public String getReversalFlag() {
		return ReversalFlag;
	}

	public void setReversalFlag(String reversalFlag) {
		ReversalFlag = reversalFlag;
	}

	public String getDb_Cr() {
		return Db_Cr;
	}

	public void setDb_Cr(String db_Cr) {
		Db_Cr = db_Cr;
	}

	public String getTransType() {
		return TransType;
	}

	public void setTransType(String transType) {
		TransType = transType;
	}

	public String getMerchantType() {
		return MerchantType;
	}

	public void setMerchantType(String merchantType) {
		MerchantType = merchantType;
	}

}
