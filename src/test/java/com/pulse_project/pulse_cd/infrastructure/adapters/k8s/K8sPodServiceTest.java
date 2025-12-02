package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;

import com.pulse_project.pulse_cd.config.MockK8sClientConfig;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MockK8sClientConfig.class, K8sPodService.class })
public class K8sPodServiceTest {

    @Autowired
    private CoreV1Api coreV1Api;

    @Autowired
    private K8sPodService k8sPodService;

    @Mock
    private CoreV1Api.APIlistPodForAllNamespacesRequest apIlistPodForAllNamespacesRequest;

    @BeforeEach
    public void setup(){
        reset(coreV1Api);
        assertNotNull(coreV1Api);
    }

    private V1Pod makePod(String name, String namespace, String image) {
        V1ObjectMeta meta = new V1ObjectMeta().name(name).namespace(namespace).labels(Map.of("app", name));
        V1Container container = new V1Container().name(name + "-ctr").image(image);
        V1PodSpec podSpec = new V1PodSpec().containers(List.of(container));
        V1PodSpec spec = podSpec;
        V1Pod pod = new V1Pod().metadata(meta).spec(spec);
        return pod;
    }

    @Test
    public void testListPods_returnsPodsFromMock() throws ApiException {
        V1Pod p1 = makePod("p1", "default", "nginx:1");
        V1Pod p2 = makePod("p2", "default", "busybox:1");
        V1PodList list = new V1PodList().items(List.of(p1,p2));

        /*
        when(coreV1Api.listNamespacedPod(
                eq("default")
        ).execute())
                .thenReturn(list);
                
         */

        when(coreV1Api.listPodForAllNamespaces()).thenReturn(apIlistPodForAllNamespacesRequest);
        when(apIlistPodForAllNamespacesRequest.execute()).thenReturn(list);

        String pods = k8sPodService.getK8sPods();
        assertNotNull(pods);
        assertEquals("p1", pods);
//        assertEquals("p1", pods.get(0).getMetadata().getName());
    }
}