package com.yuntu.jpa.web;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 *  2016/10/25.
 */
public class SortHandlerMethodArgumentResolver extends org.springframework.data.web.SortHandlerMethodArgumentResolver  {


    private SortArgumentResolver sortArgumentResolver = null;

    public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        SortHandlerMethodArgumentResolverRequest _webRequest = new SortHandlerMethodArgumentResolverRequest(
                (HttpServletRequest) webRequest.getNativeRequest(),sortArgumentResolver ,getSortParameter(parameter));
        return super.resolveArgument(parameter, mavContainer, _webRequest , binderFactory);
    }

    public SortArgumentResolver getSortArgumentResolver() {
        return sortArgumentResolver;
    }

    public void setSortArgumentResolver(SortArgumentResolver sortArgumentResolver) {
        this.sortArgumentResolver = sortArgumentResolver;
    }
}
