/*package com.pulse_project.pulse_cd.infrastructure.adapters.k8s;


import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.reset;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { K8sPodService.class })
public class K8sPodServiceTest {
    @Autowired
    private CoreV1Api coreV1Api;
    @Autowired
    private K8sPodService k8sPodService;

    @BeforeEach
    public void setup(){
        reset(coreV1Api);
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
    public void testListPods_returnsPodsFromMock() {
        V1Pod p1 = makePod("p1", "default", "nginx:1");
        V1PodList list = new V1PodList().items(List.of(p1));
    }
}
*/