package com.admin.model;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;


@Entity
//@EntityScan(basePackageClasses=com.admin.model.NpciAcqModel.java)
@EntityScan({"com.admin.model"})
@Table(name="TABLE0023")
public class NpciAcqModel {

	long T23C1;
	long T23C2;
	String T23C3;
	String T23C4;
	String T23C5;
	String T23C6;
	String T23C7;
	String T23C8;
	String T23C9;
	String T23C10;
	String T23C11;
	long T23C12;
	String T23C13;
	long T23C14;
	long T23C15;
	long T23C16;
	String T23C17;
	String T23C18;
	String T23C19;
	String T23C20;
	long T23C21;
	String T23C22;
	long T23C23;
	long T23C24;
	long T23C25;
	String T23C26;
	long T23C27;
	long T23C28;
	long T23C29;
	long T23C30;
	String T23C31;
	String T23C32;
	String T23C33;
	String T23C34;
	String T23C35;
	String T23C36;
	String T23C37;
	
	
	public NpciAcqModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NpciAcqModel(long t23c2, String t23c3, String t23c4, String t23c5, String t23c6, String t23c7,
			String t23c8, String t23c9, String t23c10, String t23c11, long t23c12, String t23c13, long t23c14, long t23c15,
			long t23c16, String t23c17, String t23c18, String t23c19, String t23c20, long t23c21, String t23c22,
			long t23c23, long t23c24, long t23c25, String t23c26, long t23c27, long t23c28, long t23c29, long t23c30,
			String t23c31, String t23c32, String t23c33, String t23c34, String t23c35, String t23c36, String t23c37) {
		this.T23C2 = t23c2;
		this.T23C3 = t23c3;
		this.T23C4 = t23c4;
		this.T23C5 = t23c5;
		this.T23C6 = t23c6;
		this.T23C7 = t23c7;
		this.T23C8 = t23c8;
		this.T23C9 = t23c9;
		this.T23C10 = t23c10;
		this.T23C11 = t23c11;
		this.T23C12 = t23c12;
		this.T23C13 = t23c13;
		this.T23C14 = t23c14;
		this.T23C15 = t23c15;
		this.T23C16 = t23c16;
		this.T23C17 = t23c17;
		this.T23C18 = t23c18;
		this.T23C19 = t23c19;
		this.T23C20 = t23c20;
		this.T23C21 = t23c21;
		this.T23C22 = t23c22;
		this.T23C23 = t23c23;
		this.T23C24 = t23c24;
		this.T23C25 = t23c25;
		this.T23C26 = t23c26;
		this.T23C27 = t23c27;
		this.T23C28 = t23c28;
		this.T23C29 = t23c29;
		this.T23C30 = t23c30;
		this.T23C31 = t23c31;
		this.T23C32 = t23c32;
		this.T23C33 = t23c33;
		this.T23C34 = t23c34;
		this.T23C35 = t23c35;
		this.T23C36 = t23c36;
		this.T23C37 = t23c37;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getT23C1() {
		return T23C1;
	}
	public void setT23C1(long t23c1) {
		this.T23C1 = t23c1;
	}
	@Column(name="T23C2")
	public long getT23C2() {
		return T23C2;
	}
	public void setT23C2(long t23c2) {
		this.T23C2 = t23c2;
	}
	@Column(name="T23C3")
	public String getT23C3() {
		return T23C3;
	}
	public void setT23C3(String t23c3) {
		this.T23C3 = t23c3;
	}
	@Column(name="T23C4")
	public String getT23C4() {
		return T23C4;
	}
	public void setT23C4(String t23c4) {
		this.T23C4 = t23c4;
	}
	@Column(name="T23C5")
	public String getT23C5() {
		return T23C5;
	}
	public void setT23C5(String t23c5) {
		this.T23C5 = t23c5;
	}
	@Column(name="T23C6")
	public String getT23C6() {
		return T23C6;
	}
	public void setT23C6(String t23c6) {
		this.T23C6 = t23c6;
	}
	@Column(name="T23C7")
	public String getT23C7() {
		return T23C7;
	}
	public void setT23C7(String t23c7) {
		this.T23C7 = t23c7;
	}
	@Column(name="T23C8")
	public String getT23C8() {
		return T23C8;
	}
	public void setT23C8(String t23c8) {
		this.T23C8 = t23c8;
	}
	@Column(name="T23C9")
	public String getT23C9() {
		return T23C9;
	}
	public void setT23C9(String t23c9) {
		this.T23C9 = t23c9;
	}
	@Column(name="T23C10")
	public String getT23C10() {
		return T23C10;
	}
	public void setT23C10(String t23c10) {
		this.T23C10 = t23c10;
	}
	@Column(name="T23C11")
	public String getT23C11() {
		return T23C11;
	}
	public void setT23C11(String t23c11) {
		this.T23C11 = t23c11;
	}
	@Column(name="T23C12")
	public long getT23C12() {
		return T23C12;
	}
	public void setT23C12(long t23c12) {
		this.T23C12 = t23c12;
	}
	@Column(name="T23C13")
	public String getT23C13() {
		return T23C13;
	}
	public void setT23C13(String t23c13) {
		this.T23C13 = t23c13;
	}
	@Column(name="T23C14")
	public long getT23C14() {
		return T23C14;
	}
	public void setT23C14(long t23c14) {
		this.T23C14 = t23c14;
	}
	@Column(name="T23C15")
	public long getT23C15() {
		return T23C15;
	}
	public void setT23C15(long t23c15) {
		this.T23C15 = t23c15;
	}
	@Column(name="T23C16")
	public long getT23C16() {
		return T23C16;
	}
	public void setT23C16(long t23c16) {
		this.T23C16 = t23c16;
	}
	@Column(name="T23C17")
	public String getT23C17() {
		return T23C17;
	}
	public void setT23C17(String t23c17) {
		this.T23C17 = t23c17;
	}
	@Column(name="T23C18")
	public String getT23C18() {
		return T23C18;
	}
	public void setT23C18(String t23c18) {
		this.T23C18 = t23c18;
	}
	@Column(name="T23C19")
	public String getT23C19() {
		return T23C19;
	}
	public void setT23C19(String t23c19) {
		this.T23C19 = t23c19;
	}
	@Column(name="T23C20")
	public String getT23C20() {
		return T23C20;
	}
	public void setT23C20(String t23c20) {
		this.T23C20 = t23c20;
	}
	@Column(name="T23C21")
	public long getT23C21() {
		return T23C21;
	}
	public void setT23C21(long t23c21) {
		this.T23C21 = t23c21;
	}
	@Column(name="T23C22")
	public String getT23C22() {
		return T23C22;
	}
	public void setT23C22(String t23c22) {
		this.T23C22 = t23c22;
	}
	@Column(name="T23C23")
	public long getT23C23() {
		return T23C23;
	}
	public void setT23C23(long t23c23) {
		this.T23C23 = t23c23;
	}
	@Column(name="T23C24")
	public long getT23C24() {
		return T23C24;
	}
	public void setT23C24(long t23c24) {
		this.T23C24 = t23c24;
	}
	@Column(name="T23C25")
	public long getT23C25() {
		return T23C25;
	}
	public void setT23C25(long t23c25) {
		this.T23C25 = t23c25;
	}
	@Column(name="T23C26")
	public String getT23C26() {
		return T23C26;
	}
	public void setT23C26(String t23c26) {
		this.T23C26 = t23c26;
	}
	@Column(name="T23C27")
	public long getT23C27() {
		return T23C27;
	}
	public void setT23C27(long t23c27) {
		this.T23C27 = t23c27;
	}
	@Column(name="T23C28")
	public long getT23C28() {
		return T23C28;
	}
	public void setT23C28(long t23c28) {
		this.T23C28 = t23c28;
	}
	@Column(name="T23C29")
	public long getT23C29() {
		return T23C29;
	}
	public void setT23C29(long t23c29) {
		this.T23C29 = t23c29;
	}
	@Column(name="T23C30")
	public long getT23C30() {
		return T23C30;
	}
	public void setT23C30(long t23c30) {
		this.T23C30 = t23c30;
	}
	@Column(name="T23C31")
	public String getT23C31() {
		return T23C31;
	}
	public void setT23C31(String t23c31) {
		this.T23C31 = t23c31;
	}
	@Column(name="T23C32")
	public String getT23C32() {
		return T23C32;
	}
	public void setT23C32(String t23c32) {
		this.T23C32 = t23c32;
	}
	@Column(name="T23C33")
	public String getT23C33() {
		return T23C33;
	}
	public void setT23C33(String t23c33) {
		this.T23C33 = t23c33;
	}
	@Column(name="T23C34")
	public String getT23C34() {
		return T23C34;
	}
	public void setT23C34(String t23c34) {
		this.T23C34 = t23c34;
	}
	@Column(name="T23C35")
	public String getT23C35() {
		return T23C35;
	}
	public void setT23C35(String t23c35) {
		this.T23C35 = t23c35;
	}
	@Column(name="T23C36")
	public String getT23C36() {
		return T23C36;
	}
	public void setT23C36(String t23c36) {
		this.T23C36 = t23c36;
	}
	@Column(name="T23C37")
	public String getT23C37() {
		return T23C37;
	}
	public void setT23C37(String t23c37) {
		this.T23C37 = t23c37;
	}
	@Override
	public String toString() {
		return "NpciAcqModel [T23C1=" + T23C1 + ", T23C2=" + T23C2 + ", T23C3=" + T23C3 + ", T23C4=" + T23C4
				+ ", T23C5=" + T23C5 + ", T23C6=" + T23C6 + ", T23C7=" + T23C7 + ", T23C8=" + T23C8 + ", T23C9=" + T23C9
				+ ", T23C10=" + T23C10 + ", T23C11=" + T23C11 + ", T23C12=" + T23C12 + ", T23C13=" + T23C13
				+ ", T23C14=" + T23C14 + ", T23C15=" + T23C15 + ", T23C16=" + T23C16 + ", T23C17=" + T23C17
				+ ", T23C18=" + T23C18 + ", T23C19=" + T23C19 + ", T23C20=" + T23C20 + ", T23C21=" + T23C21
				+ ", T23C22=" + T23C22 + ", T23C23=" + T23C23 + ", T23C24=" + T23C24 + ", T23C25=" + T23C25
				+ ", T23C26=" + T23C26 + ", T23C27=" + T23C27 + ", T23C28=" + T23C28 + ", T23C29=" + T23C29
				+ ", T23C30=" + T23C30 + ", T23C31=" + T23C31 + ", T23C32=" + T23C32 + ", T23C33=" + T23C33
				+ ", T23C34=" + T23C34 + ", T23C35=" + T23C35 + ", T23C36=" + T23C36 + ", T23C37=" + T23C37 + "]";
	}
	
}
