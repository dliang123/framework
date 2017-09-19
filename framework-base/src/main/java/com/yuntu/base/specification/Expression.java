package com.yuntu.base.specification;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.Objects;

/**
 * 表达式
 */
public class Expression implements ISpecification {

    private boolean negated = false;

    private Property property;
    private Object value;
    private Operator operator;

    @Override
    public boolean isNegated() {
        return negated;
    }


    public Expression(String propertyName, Operator operator, Object value) {


        Objects.requireNonNull(propertyName,"参数 propertyName 不能为空");
        Objects.requireNonNull(operator,"参数 operator 不能为空");

        this.property = Property.name(propertyName);
        this.value = value;
        this.operator = operator;
    }

    public Expression(Property property, Operator operator, Object value) {

        Objects.requireNonNull(property,"参数 property 不能为空");
        Objects.requireNonNull(operator,"参数 operator 不能为空");

        this.property = property;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public ISpecification not(boolean isNew) {
        if( isNew ){
            Expression expression = new Expression(this.property,this.operator,this.value);
            expression.negated = true;
            return expression;
        }
        this.negated = true;
        return this;
    }

    public static Expression expression(String propertyName,Operator operator,Object value){
        return new Expression(propertyName,operator,value);
    }

    public static Expression expression(Property propertyName,Operator operator,Object value){
        return new Expression(propertyName,operator,value);
    }
    public static Expression expression(String field,String operatorName,Object value){
        return expression(field,Operator.valueOf(operatorName),value);
    }

    public static Expression equals(String field,Object value){
        return expression(field,Operator.EQ,value);
    }

    public static Expression notEquals(String field,Object value){
        return expression(field,Operator.NEQ,value);
    }

    public static Expression less(String field,Object value){
        return expression(field,Operator.LT,value);
    }

    public static Expression lessOrEqual(String field,Object value){
        return expression(field,Operator.LE,value);
    }

    public static Expression greater(String field,Object value){
        return expression(field,Operator.GT,value);
    }

    public static Expression greaterOrEqual(String field,Object value){
        return expression(field,Operator.GE,value);
    }

    public static Expression containIn(String field,Object value){
        return expression(field,Operator.CI,value);
    }

    public static Expression notContainIn(String field,Object value){
        return expression(field,Operator.NCI,value);
    }

    public static Expression beginWith(String field,Object value){
        return expression(field,Operator.BW,value);
    }

    public static Expression notBeginWith(String field,Object value){
        return expression(field,Operator.NBW,value);
    }

    public static Expression endWith(String field,Object value){
        return expression(field,Operator.EW,value);
    }

    public static Expression notEndWith(String field,Object value){
        return expression(field,Operator.NEW,value);
    }

    public static Expression in(String field,Object value){
        return expression(field,Operator.IN,value);
    }

    public static Expression notIn(String field,Object value){
        return expression(field,Operator.NIN,value);
    }

    public static Expression isNull(String field){
        return expression(field,Operator.NU,null);
    }

    public static Expression isNotNull(String field){
        return expression(field,Operator.NNU,null);
    }

    public Property getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public ISpecification ifValue(IfValue ifValue){
        return ifValue.ifValue(this)
                ? this
                : Nothing.NOTHING;
    }

    public ISpecification ifValueNotBlank(){
        return ifValue(IfValue.ifValueNotBlank);
    }

    public ISpecification ifValueBlank(){
        return ifValue(IfValue.ifValueBlank);
    }

    public ISpecification ifValueNull(){
        return ifValue(IfValue.ifValueNull);
    }
    public ISpecification ifValueNotNull(){
        return ifValue(IfValue.ifValueNotNull);
    }



    @Override
    public boolean evaluate(Object context) {


        if( null == context || null == property  ){
            return false;
        }

        Object _value = value;
        if( _value instanceof Property ){
            _value = get(context,((Property)value).name());
        }

        try {
            boolean result = this.getOperator().evaluate(get(context,property.name()),_value);
            return !isNegated() ? result : !result ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object get( Object context ,String name ) throws UnknownPropertyException {

        try {
            //新版的 PropertyUtils 支持 map 获取属性,切支持 参数名包含.的级联查询
            return PropertyUtils.getProperty(context,name);
        } catch (Exception e) {
            throw new UnknownPropertyException("未知的属性 "+name);
        }
    }
}