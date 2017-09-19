package com.yuntu.jpa.extend;

import com.google.common.collect.Lists;
import com.yuntu.base.specification.*;
import com.yuntu.common.utils.DateUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ISpecification 工具类
 */
public class SpecificationUtils {

    /**
     * 解析规则
     * @param specification
     * @return
     */
    public static <T> Specification<T>  specification(ISpecification<T> specification){
        return (root,criteriaQuery,criteriaBuilder)->parse(specification,root,criteriaBuilder);
    }

    /**
     * 判断是否是数字
     * @param javaType
     * @return
     */
    private static boolean isNumber(Class javaType){
        try {
            return javaType.asSubclass(Number.class) == javaType;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 对象转换为数组
     * @param value
     * @return
     */
    private static Object [] toArray(Object value, String split){

        if( null == value)
            throw new IllegalArgumentException("null not convert array");

        if( value instanceof String ){
            return ((String) value).split(split);
        }else if( value instanceof Collection){
            return ((Collection) value).toArray();
        }else if ( value.getClass().isArray()){
            return (Object []) value;
        }else {
            throw new IllegalArgumentException(value+" not convert array");
        }

    }

    /**
     *  类型转换
     * @param value     值
     * @param javaType  要转换的类型
     * @param isThrow   转换失败是否抛出异常( true IllegalArgumentException  | false 返回 null  )
     * @return
     */
    public static <T>T convert(Object value,Class<T> javaType,boolean isThrow){



        try {
            //( 临时方案,后续需在 convert 中注册 )

            //时间转换
            if(  javaType.isAssignableFrom(java.util.Date.class) ){
                try {
                    value = DateUtils.parse(String.valueOf(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //枚举支持
            if( javaType.isEnum() ){
                if( value instanceof String ){
                   return (T)Enum.valueOf((Class)javaType,(String)value);
                }else if ( value instanceof Number){
                    return javaType.getEnumConstants()[((Number) value).intValue()];
                }
            }
            return (T) ConvertUtils.convert(value,javaType);
        }catch (Exception e){
            if( isThrow ){
                throw new IllegalArgumentException(value + "convert to " + javaType+" error", e);
            }
            return null;
        }
    }


    public static Path getPath(Root<?> root,Property property){

        return getPath(root,property.name());
    }

    public static Path getPath(Root<?> root,String property){
        //获取 path
        Path path = root;
        if( property.contains(".") ){
            for (String name : property.split("\\.")) {

                path = path.get(name);
                if( path.getModel() == null && path.getParentPath() == root){
                    if( root.getModel().getAttribute(name).isCollection() ){
                        path = root.join(name);
                    }
                }
            }
        }else {
            path = root.get(property);
        }
        return path;
    }

    /**
     * 把 expression 解析为相应的 Predicate
     * @param expression    要解析的规则
     * @param root          root
     * @param builder       builder
     * @return
     */
    public static Predicate parseExpression(Expression expression, Root<?> root, CriteriaBuilder builder) {


        Object value = expression.getValue();
        Operator operator = expression.getOperator();

        //获取 path
        Path path = getPath(root,expression.getProperty());

        //需要的数据类型
        Class javaType = path.getJavaType();


        if( value instanceof Property ){

            value = getPath(root,((Property) value).name());
        } else if ( operator == Operator.IN || operator == Operator.NIN ) { // in 和 not ni 情况处理

            value = Stream.of(toArray(value, ",")).map(e -> convert(e, javaType, false))
                    .filter(e -> e != null)
                    .collect(Collectors.toList());
        }else if( !javaType.isInstance( value ) ){ //类型不正确

            value = convert(value,javaType,true);
        }


        switch (operator) {

            case EQ:    return builder.equal(path,value);
            case NEQ:   return builder.notEqual(path, value);
            case IN:    return builder.in(path).value(value);
            case NIN:   return builder.in(path).value(value).not();
            case NU:    return path.isNull();
            case NNU:   return path.isNotNull();

            case GE:    return value instanceof Path
                            ? builder.greaterThanOrEqualTo(path, (Path) value)
                            : builder.greaterThanOrEqualTo(path, (Comparable) value);

            case LT:    return value instanceof Path
                            ? builder.lessThan(path,(Path)value)
                            : builder.lessThan(path,(Comparable)value);

            case LE:    return value instanceof Path
                            ? builder.lessThanOrEqualTo(path,(Path)value)
                            : builder.lessThanOrEqualTo(path, (Comparable) value);

            case GT:    return value instanceof Path
                            ? builder.greaterThan(path,(Path)value)
                            : builder.greaterThan(path, (Comparable) value);

            case CI:    return value instanceof Path
                            ? builder.like(path,like(builder,true,(Path)value,true))
                            : builder.like(path,"%"+value+"%");

            case NCI:   return value instanceof Path
                            ? builder.notLike(path,like(builder,true,(Path)value,true))
                            : builder.notLike(path,"%"+value+"%");

            case BW:    return value instanceof Path
                            ? builder.like(path,like(builder,false,(Path)value,true))
                            : builder.like(path,  value + "%" );

            case NBW:   return value instanceof Path
                            ? builder.notLike(path,like(builder,false,(Path)value,true))
                            : builder.notLike(path, value + "%");

            case EW:    return value instanceof Path
                            ? builder.like(path,like(builder,true,(Path)value,false))
                            : builder.like(path, "%" + value );

            case NEW:   return value instanceof Path
                            ? builder.notLike(path,like(builder,true,(Path)value,false))
                            : builder.notLike(path, "%" + value);

            default:    return builder.disjunction();
        }
    }



    private static javax.persistence.criteria.Expression like(CriteriaBuilder builder ,
                                                              boolean left,
                                                              javax.persistence.criteria.Expression value,
                                                              boolean right){

        if( left ){
            value = builder.concat("%",value);
        }
        if( right ){
            value = builder.concat(value,"%");
        }
        return value;
    }



    /**
     * 解析规则
     * @param spe       要解析的规则
     * @param root
     * @param builder
     * @return
     */
    public static Predicate parse(ISpecification spe , Root<?> root, CriteriaBuilder builder){

        Predicate predicate = builder.conjunction();
        if( spe == null ){
            return predicate;
        } else if( spe instanceof Expression){
            predicate = parseExpression((Expression)spe,root,builder);
        }else if( spe instanceof SpecificationComposite){

            SpecificationComposite group = (SpecificationComposite)spe;
            List<Predicate> predicates = Lists.newArrayListWithExpectedSize(group.getSpecifications().size());

            predicates.addAll(group.getSpecifications().stream()
                    .map(_specification -> parse(_specification, root, builder))
                    .collect(Collectors.toList()));
            if( group.logic() == Logic.AND  ){
                predicate = builder.and(predicates.toArray(new Predicate[]{} ));
            }else {
                predicate = builder.or(predicates.toArray(new Predicate[]{} ));
            }
        }
        return spe.isNegated() ? predicate.not() : predicate;
    }

}
