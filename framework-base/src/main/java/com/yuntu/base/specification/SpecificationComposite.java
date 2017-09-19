package com.yuntu.base.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 查询规则集合( 根据指定逻辑链接 )
 * Created by linxiaohui on 2016/10/25.
 */
public final class SpecificationComposite implements ISpecification {

    //默认逻辑
    private static final Logic DEFAULT_LOGIC = Logic.AND;
    //规约组合
    private List<ISpecification> specifications = new ArrayList();
    //组合逻辑
    private Logic logic = DEFAULT_LOGIC ;
    //是否是 否定的
    private boolean negated;
    @Override
    public boolean isNegated() {
        return negated;
    }


    public SpecificationComposite(){

    }

    public SpecificationComposite(Logic logic){
        if( null != logic ){
            this.logic = logic;
        }
    }
    public Logic logic(){
        return logic;
    }

    public <S extends ISpecification> SpecificationComposite add(S specification){
        if( null !=specification && specification != Nothing.NOTHING){
            this.specifications.add(specification);
        }
        return this;
    }

    public <S extends ISpecification> SpecificationComposite add(S ... specification){
        for(S i : specification ){
            this.specifications.add(i);
        }
        return this;
    }

    public <S extends ISpecification> SpecificationComposite add(Collection<S> specification){
        for(S i : specification ){
            this.specifications.add(i);
        }
        return this;
    }

    public List<ISpecification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ISpecification> specifications) {
        this.specifications = specifications;
    }



    @Override
    public ISpecification not(boolean isNew) {

        if( isNew ){
            SpecificationComposite junctionSpecification = new SpecificationComposite(this.logic);
            junctionSpecification.add( this.specifications);
            junctionSpecification.negated = true;
            return junctionSpecification;
        }
        this.negated = true;
        return this;
    }

    /**
     * 改变逻辑
     * @param logic
     * @return
     */
    public SpecificationComposite changeLogic(Logic logic){
        this.logic = logic;
        return this;
    }

    public static SpecificationComposite logic(String logic){

        if( (null == logic || logic.trim().isEmpty() ) || "AND".equalsIgnoreCase( logic )){
            return SpecificationComposite.and();
        }else {
            return SpecificationComposite.or();
        }

    }
    /**
     * and
     * @return
     */
    public static SpecificationComposite and(){
        return new SpecificationComposite(Logic.AND);
    }

    /**
     * and
     * @return
     */
    public static <S extends ISpecification> SpecificationComposite and(S ... speJunctions){
        return new SpecificationComposite(Logic.AND).add(speJunctions);
    }

    /**
     * and
     * @return
     */
    public static <S extends ISpecification> SpecificationComposite and(Collection<S>  speJunctions){
        return new SpecificationComposite(Logic.AND).add(speJunctions);
    }


    /**
     * or
     * @return
     */
    public static SpecificationComposite or(){
        return new SpecificationComposite(Logic.OR);
    }

    /**
     * or
     * @return
     */
    public static <S extends ISpecification> SpecificationComposite or(S ... speJunctions){
        return new SpecificationComposite(Logic.OR).add(speJunctions);
    }

    /**
     * or
     * @return
     */
    public static <S extends ISpecification> SpecificationComposite or(Collection<S>  speJunctions){
        return new SpecificationComposite(Logic.OR).add(speJunctions);
    }



    public boolean evaluate( Object context ){

        boolean predicate = true;
        if( null != specifications && !specifications.isEmpty() ){
            switch ( this.logic ){
                case AND:
                    predicate = specifications.stream().allMatch(e -> e.evaluate(context) ) ;
                    break;
                case OR:
                    predicate = specifications.stream().anyMatch(e -> e.evaluate(context) ) ;
                    break;
            }
        }

        return isNegated() ? !predicate : predicate;
    }
}