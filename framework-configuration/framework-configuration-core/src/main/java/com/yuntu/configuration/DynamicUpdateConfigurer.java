package com.yuntu.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by linxiaohui on 16/1/12.
 */
public class DynamicUpdateConfigurer extends ImmutableConfigurer implements DynamicUpdateWatcher  {

    private static final Logger logger = LoggerFactory.getLogger(DynamicUpdateConfigurer.class);

    private Properties config = null;
    private Object lock = new Object();

    private DynamicUpdateWatched watched;

    private DynamicUpdateConfigurer(){

        super();
        setIgnoreResourceNotFound(true);
        setIgnoreUnresolvablePlaceholders(true);
        setOrder(1);
        setFileEncoding("UTF-8");
    }

    public DynamicUpdateWatched getWatched() {
        return watched;
    }

    public void setWatched(DynamicUpdateWatched watched) {
        this.watched = watched;
    }



    protected void loadProperties(Properties props) throws IOException {

        super.loadProperties(props);
        config = props;

        if( null == watched ){
            logger.warn("没有注入 watched 动态配置更新将无效!~");
        }else {
            Map<String,byte[]> defaultConfig = watched.register(this);

            synchronized ( lock ){
                for(Map.Entry<String,byte[]> entry : defaultConfig.entrySet() ){

                    config.put( entry.getKey(),new String(entry.getValue()) );
                    logger.info(" init config key {} value {} success",entry.getKey(),new String(entry.getValue()));

                }
            }

        }
    }


    public void change(ChangeEvent event) {


        if (event == null || event.getData() == null) {
            return;
        }

        logger.debug(" config change  key {} type  ",event.getKey(),event.getType());

        Object ov = config.getProperty(event.getKey());

        synchronized ( lock ) {

            switch (event.getType()) {

                case ADDED: {
                    config.put(event.getKey(),new String(event.getData()));
                    break;
                }
                case UPDATED: {
                    config.put(event.getKey(),new String(event.getData()));
                    break;
                }
                case REMOVED: {
                    config.remove(event.getKey());
                    break;
                }
            }
        }

        if( logger.isDebugEnabled() ){
            logger.debug(" config change key {} value {} >>> {} event{}",
                    event.getKey(),ov,config.get(event.getKey()),event.getData());
        }
    }

    @PreDestroy
    public void destroy(){
        watched.remove(this);
    }


}
