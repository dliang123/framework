package com.yuntu.jpa.naming;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Objects;

/**
 *  15/12/3.
 */
@SuppressWarnings("serial")
public class PhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {


    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        String text = warp(name.getText());
        if(Objects.equals(text.charAt(0) , '_')){
            text = text.replaceFirst("_","");
        }
        return super.toPhysicalColumnName(new Identifier(text,name.isQuoted()), context);
    }


    public static String warp(String text){
        text = text.replaceAll("([A-Z])","_$1").toLowerCase();
        if(Objects.equals(text.charAt(0) , '_')){
            text = text.replaceFirst("_","");
        }
        return text;
    }


}
