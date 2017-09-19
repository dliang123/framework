package com.yuntu.configuration;

import com.yuntu.base.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by linxiaohui on 16/1/11.
 */
public class ImmutableConfigurer extends PropertyPlaceholderConfigurer  {

    private static final Logger logger = LoggerFactory.getLogger(ImmutableConfigurer.class);

    /** 应用信息 **/
    private Application application;
    /** 配置服务器地址 **/
    private String configServerAddress;

    public ImmutableConfigurer(){

        super();
        setIgnoreResourceNotFound(true);
        setFileEncoding("UTF-8");
        setOrder(2);
    }



    @Override
    public void setLocation(Resource location) {
        setLocations(location);
    }

    @Override
    public void setLocations(Resource... locations) {

        if( null != configServerAddress  ){
            if( !configServerAddress.trim().startsWith("http://") ){
                throw new Error("error configServerAddress");
            }
            String url = configServerAddress;
            if( null != application ){
                url += MessageFormat.format("?key={0}&cluster={1}", application.getKey(),application.getCluster());
            }
            try {
                Arrays.fill(locations,new UrlResource(new URL(url)));
            } catch (MalformedURLException e) {
                logger.warn("",e);
                throw new Error("error configServerAddress");
            }
        }
        super.setLocations(locations);
    }


    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getConfigServerAddress() {
        return configServerAddress;
    }

    public void setConfigServerAddress(String configServerAddress) {
        this.configServerAddress = configServerAddress;
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {

        super.processProperties(beanFactoryToProcess,props);
        SpringConfig config = null;
        try {
            config = (SpringConfig) beanFactoryToProcess.getBean(SpringConfig.NAME);
        }catch (Exception e){

        }finally {
            if( null == config ){
                config = new SpringConfig();
                beanFactoryToProcess.initializeBean(config,SpringConfig.NAME);

            }
        }
    }
}
