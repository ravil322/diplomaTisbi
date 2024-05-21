package ru.ravnasybullin.DoiReg.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ConversationIdRequestProcessor implements RequestDataValueProcessor {

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        return action;
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
        return value;
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        Map<String, String> hiddenFields = new HashMap<>();
        // cid token
        if (request.getAttribute(ConversationalSessionAttributeStore.CID_FIELD) != null) {
            hiddenFields.put(ConversationalSessionAttributeStore.CID_FIELD,
                    request.getAttribute(ConversationalSessionAttributeStore.CID_FIELD).toString());
        }
        // auth token
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            hiddenFields.put(token.getParameterName(), token.getToken());
        }
        return hiddenFields;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return url;
    }

}
