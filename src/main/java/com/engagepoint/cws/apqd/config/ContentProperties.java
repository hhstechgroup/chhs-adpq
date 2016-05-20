package com.engagepoint.cws.apqd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by dmytro.palczewski on 3/31/2016.
 */
@ConfigurationProperties(prefix = "content", ignoreUnknownFields = false)
public class ContentProperties {

    private  String username;

    private  String password;

    private  String bindingType;

    private  String repositoryId;

    private  boolean cookiesEnabled;

    private  boolean responsesCompressionEnabled;

    private  boolean requestsCompressionEnabled;

    private int objectsCacheTtl;

    private final Atompub atompub = new Atompub();

    private final Webservices webservices = new Webservices();

    private final Poc poc = new Poc();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBindingType() {
        return bindingType;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public boolean isCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(boolean cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
    }

    public boolean isResponsesCompressionEnabled() {
        return responsesCompressionEnabled;
    }

    public void setResponsesCompressionEnabled(boolean responsesCompressionEnabled) {
        this.responsesCompressionEnabled = responsesCompressionEnabled;
    }

    public boolean isRequestsCompressionEnabled() {
        return requestsCompressionEnabled;
    }

    public void setRequestsCompressionEnabled(boolean requestsCompressionEnabled) {
        this.requestsCompressionEnabled = requestsCompressionEnabled;
    }

    public int getObjectsCacheTtl() {
        return objectsCacheTtl;
    }

    public void setObjectsCacheTtl(int objectsCacheTtl) {
        this.objectsCacheTtl = objectsCacheTtl;
    }

    public Atompub getAtompub() {
        return atompub;
    }

    public Webservices getWebservices() {
        return webservices;
    }

    public Poc getPoc() {
        return poc;
    }

    public static class Atompub {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Webservices {

        private String url;

        private String jaxwsimpl;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getJaxwsimpl() {
            return jaxwsimpl;
        }

        public void setJaxwsimpl(String jaxwsimpl) {
            this.jaxwsimpl = jaxwsimpl;
        }
    }

    public static class Poc {

        private String rootFolder;

        private int maxItemsPerPage;

        public String getRootFolder() {
            return rootFolder;
        }

        public void setRootFolder(String rootFolder) {
            this.rootFolder = rootFolder;
        }

        public int getMaxItemsPerPage() {
            return maxItemsPerPage;
        }

        public void setMaxItemsPerPage(int maxItemsPerPage) {
            this.maxItemsPerPage = maxItemsPerPage;
        }
    }
}
