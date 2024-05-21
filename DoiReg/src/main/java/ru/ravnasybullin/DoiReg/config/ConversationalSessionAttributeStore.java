package ru.ravnasybullin.DoiReg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class ConversationalSessionAttributeStore implements SessionAttributeStore {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private int keepAliveConversations = 0;

    public final static String CID_FIELD = "_cid";
    public final static String SESSION_MAP = "sessionConversationMap";

    @Override
    public void storeAttribute(WebRequest request, String attributeName, Object attributeValue) {
        String cId = getConversationId(request);
        if (cId == null || cId.trim().length() == 0) {
            cId = UUID.randomUUID().toString();
        }
        request.setAttribute(CID_FIELD, cId, WebRequest.SCOPE_REQUEST);
        store(request, attributeName, attributeValue, cId);
    }

    @Override
    public Object retrieveAttribute(WebRequest request, String attributeName) {
        if (getConversationId(request) != null) {
            return getConversationStore(request, getConversationId(request)).get(attributeName);
        } else {
            return null;
        }
    }

    @Override
    public void cleanupAttribute(WebRequest request, String attributeName) {
        String conversationId = getConversationId(request);
        if (conversationId == null) {
            return;
        }
        Map<String, Object> conversationStore = getConversationStore(request, conversationId);
        conversationStore.remove(attributeName);
        // Delete the conversation store from the session if empty
        if (conversationStore.isEmpty()) {
            getSessionConversationsMap(request).remove(conversationId);
        }
    }

    private Map<String, Object> getConversationStore(WebRequest request, String conversationId) {
        Map<String, Object> conversationMap = getSessionConversationsMap(request).get(conversationId);
        if (conversationId != null && conversationMap == null) {
            conversationMap = new HashMap<>();
            getSessionConversationsMap(request).put(conversationId, conversationMap);
        }
        return conversationMap;
    }

    private LinkedHashMap<String, Map<String, Object>> getSessionConversationsMap(WebRequest request) {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Map<String, Object>> sessionMap = (LinkedHashMap<String, Map<String, Object>>) request.getAttribute(
                SESSION_MAP, WebRequest.SCOPE_SESSION);
        if (sessionMap == null) {
            sessionMap = new LinkedHashMap<>();
            request.setAttribute(SESSION_MAP, sessionMap, WebRequest.SCOPE_SESSION);
        }
        return sessionMap;
    }

    private void store(WebRequest request, String attributeName, Object attributeValue, String cId) {
        LinkedHashMap<String, Map<String, Object>> sessionConversationsMap = getSessionConversationsMap(request);
        if (keepAliveConversations > 0 && sessionConversationsMap.size() >= keepAliveConversations
                && !sessionConversationsMap.containsKey(cId)) {
            // clear oldest conversation
            String key = sessionConversationsMap.keySet().iterator().next();
            sessionConversationsMap.remove(key);
        }
        getConversationStore(request, cId).put(attributeName, attributeValue);

    }

    public int getKeepAliveConversations() {
        return keepAliveConversations;
    }

    public void setKeepAliveConversations(int numConversationsToKeep) {
        keepAliveConversations = numConversationsToKeep;
    }

    private String getConversationId(WebRequest request) {
        String cid = request.getParameter(CID_FIELD);
        if (cid == null) {
            cid = (String) request.getAttribute(CID_FIELD, WebRequest.SCOPE_REQUEST);
        }
        return cid;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        requestMappingHandlerAdapter.setSessionAttributeStore(this);
    }

}