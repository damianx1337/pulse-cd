package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.JSON;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class K8sConfigMapService {
    private final CoreV1Api coreV1Api;

    private final ObjectMapper objectMapper;

    public K8sConfigMapService(CoreV1Api coreV1Api, ObjectMapper objectMapper) {
        this.coreV1Api = coreV1Api;
        this.objectMapper = objectMapper;
    }

    /**
     * Patches a configmap by adding and/or overwriting fields in data.
     * Only updates fields you provide (safe).
     */
    public void patchConfigMap(String name, String namespace, Map<String, String> changeSetProperties) throws IOException, ApiException {

        // Build merge patch structure
        Map<String, Object> patchBody = Map.of("data", changeSetProperties);
        Map<String, String> patchBodyD = Map.of("data", "changeSetProperties_33333");


        V1ConfigMap cm = new V1ConfigMap().metadata(new V1ObjectMeta().name("my-config"));
        cm.setData(patchBodyD);
        String json = JSON.serialize(cm);
        log.info("JSON: {}", json);

        String url = coreV1Api.getApiClient().getBasePath() + "/api/v1/namespaces/" + namespace + "/configmaps/" + name;
        RequestBody body = RequestBody.create(json, MediaType.get("application/merge-patch+json"));

        Request.Builder rb = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Content-Type", "application/merge-patch+json");

        Request req = rb.build();
        Response resp = coreV1Api.getApiClient().getHttpClient().newCall(req).execute();

        if (!resp.isSuccessful()) {
            ResponseBody respBody = resp.body();
            String bodyStr = respBody != null ? respBody.string() : "";
            throw new ApiException(resp.code(), "Patch failed: " + bodyStr);
        }
     //   String respStr = resp.body().string();
       // return new ObjectMapper().readValue(respStr, V1ConfigMap.class);


        //return coreV1Api.patchNamespacedConfigMap(name, namespace, patch).execute();
    }
}
