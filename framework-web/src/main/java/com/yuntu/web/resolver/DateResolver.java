package com.yuntu.web.resolver;

import com.yuntu.common.utils.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by linxiaohui on 15/11/12.
 */
@ControllerAdvice
public class DateResolver {

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {

        binder.registerCustomEditor(Date.class,new PropertyEditorSupport(){
            @Override
            public void setAsText(String value) throws IllegalArgumentException {
                try {
                    if( value != null ){
                        Object v = DateUtils.parse(value);
                        super.setValue(v);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
