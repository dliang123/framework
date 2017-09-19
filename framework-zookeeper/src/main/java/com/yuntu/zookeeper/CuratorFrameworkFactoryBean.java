package com.yuntu.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.utils.ZookeeperFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Created by linxiaohui on 16/2/3.
 */
public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework> {


    private CuratorFrameworkFactory.Builder builder;

    private CuratorFramework curatorFramework;

    public CuratorFrameworkFactoryBean(){
        builder = CuratorFrameworkFactory.builder();
    }



    public void setAuthorization(List<AuthInfo> authInfos) {
        builder.authorization(authInfos);
    }

    public void setConnectString(String connectString) {
        builder.connectString(connectString);
    }

    public void ensembleProvider(EnsembleProvider ensembleProvider) {
        builder.ensembleProvider(ensembleProvider);
    }


    public void setNamespace(String namespace) {
        builder.namespace(namespace);
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        builder.sessionTimeoutMs(sessionTimeoutMs);
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        builder.connectionTimeoutMs(connectionTimeoutMs);
    }

    public void setMaxCloseWaitMs(int maxCloseWaitMs) {
        builder.maxCloseWaitMs(maxCloseWaitMs);
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        builder.retryPolicy(retryPolicy);
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        builder.threadFactory(threadFactory);
    }

    public void setCompressionProvider(CompressionProvider compressionProvider) {
        builder.compressionProvider(compressionProvider);
    }

    public void setZookeeperFactory(ZookeeperFactory zookeeperFactory) {
        builder.zookeeperFactory(zookeeperFactory);
    }

    public void setAclProvider(ACLProvider aclProvider) {
        builder.aclProvider(aclProvider);
    }

    public void setCanBeReadOnly(boolean canBeReadOnly) {
        builder.canBeReadOnly(canBeReadOnly);
    }

    public ACLProvider getAclProvider() {
        return builder.getAclProvider();
    }

    public ZookeeperFactory getZookeeperFactory() {
        return builder.getZookeeperFactory();
    }

    public CompressionProvider getCompressionProvider() {
        return builder.getCompressionProvider();
    }

    public ThreadFactory getThreadFactory() {
        return builder.getThreadFactory();
    }

    public EnsembleProvider getEnsembleProvider() {
        return builder.getEnsembleProvider();
    }

    public int getSessionTimeoutMs() {
        return builder.getSessionTimeoutMs();
    }

    public int getConnectionTimeoutMs() {
        return builder.getConnectionTimeoutMs();
    }

    public int getMaxCloseWaitMs() {
        return builder.getMaxCloseWaitMs();
    }

    public RetryPolicy getRetryPolicy() {
        return builder.getRetryPolicy();
    }

    public String getNamespace() {
        return builder.getNamespace();
    }

    public boolean useContainerParentsIfAvailable() {
        return builder.useContainerParentsIfAvailable();
    }

    @Deprecated
    public String getAuthScheme() {
        return builder.getAuthScheme();
    }

    @Deprecated
    public byte[] getAuthValue() {
        return builder.getAuthValue();
    }

    public List<AuthInfo> getAuthInfos() {
        return builder.getAuthInfos();
    }

    public byte[] getDefaultData() {
        return builder.getDefaultData();
    }

    public boolean canBeReadOnly() {
        return builder.canBeReadOnly();
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        if( null  == curatorFramework ){
            curatorFramework = builder.build();
            curatorFramework.start();
        }
        return curatorFramework;
    }

    @Override
    public Class<CuratorFramework> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @PreDestroy
    public void destroy(){
        curatorFramework.close();
    }
}
