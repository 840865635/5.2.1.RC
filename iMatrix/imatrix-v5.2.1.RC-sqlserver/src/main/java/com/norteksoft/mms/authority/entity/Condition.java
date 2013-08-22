package com.norteksoft.mms.authority.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.norteksoft.mms.authority.enumeration.ConditionType;
import com.norteksoft.mms.authority.enumeration.FieldOperator;
import com.norteksoft.mms.authority.enumeration.LeftBracket;
import com.norteksoft.mms.authority.enumeration.RightBracket;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.orm.IdEntity;
import com.norteksoft.mms.authority.enumeration.LogicOperator;
/**
 * 数据规则条件
 * @author Administrator
 *
 */
@Entity
@Table(name="MMS_CONDITION")
public class Condition extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fieldName;//字段名
	@Enumerated(EnumType.STRING)
	private FieldOperator operator;//比较符号
	private String conditionValue;//条件值
	@Enumerated(EnumType.STRING)
	private LogicOperator lgicOperator;//条件连接类型
	private String field;//数据表字段
	@Enumerated(EnumType.STRING)
	private DataType dataType;//字段数据类型
	private String enumPath;//当dataType值为枚举类型时，该值有用
	private String keyValue;//保存枚举类型，选项组和key：Value形式的值设置
	
	@ManyToOne
	@JoinColumn(name="FK_DATA_RULE_ID")
	private DataRule dataRule;
	private ConditionType relativeType;//条件为相对条件时，此字段有值，表示相对值
	
	private LeftBracket leftBracket;//左括号
	private RightBracket rightBracket;//右括号
	
	private Integer displayIndex;//显示顺序
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public FieldOperator getOperator() {
		return operator;
	}
	public void setOperator(FieldOperator operator) {
		this.operator = operator;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	public LogicOperator getLgicOperator() {
		return lgicOperator;
	}
	public void setLgicOperator(LogicOperator lgicOperator) {
		this.lgicOperator = lgicOperator;
	}
	public DataRule getDataRule() {
		return dataRule;
	}
	public void setDataRule(DataRule dataRule) {
		this.dataRule = dataRule;
	}
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getEnumPath() {
		return enumPath;
	}
	public void setEnumPath(String enumPath) {
		this.enumPath = enumPath;
	}
	public LeftBracket getLeftBracket() {
		return leftBracket;
	}
	public void setLeftBracket(LeftBracket leftBracket) {
		this.leftBracket = leftBracket;
	}
	public RightBracket getRightBracket() {
		return rightBracket;
	}
	public void setRightBracket(RightBracket rightBracket) {
		this.rightBracket = rightBracket;
	}
	public ConditionType getRelativeType() {
		return relativeType;
	}
	public void setRelativeType(ConditionType relativeType) {
		this.relativeType = relativeType;
	}
	public Integer getDisplayIndex() {
		return displayIndex;
	}
	public void setDisplayIndex(Integer displayIndex) {
		this.displayIndex = displayIndex;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
}
