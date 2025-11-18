build-app:
	./mvnw clean install

run-app:
	./mvnw spring-boot:run

create-k8s-cluster:
	echo "starting podman machine and minikube k8s cluster..."
	podman machine init --cpus 6 --memory 8000 --disk-size 15 minikube-6-8
	podman system connection default minikube-6-8-root
	podman machine start minikube-6-8
	minikube start -p 1-node-cluster --nodes 1 --memory 6000 --cpus 4 --kubernetes-version=v1.34.2 --driver=podman --container-runtime=containerd
	minikube profile list
	echo "DONE"

destroy-k8s-cluster:
	echo "stopping + deleting minikube cluster, stopping podman machine..."
	minikube stop -p 1-node-cluster
	minikube delete -p 1-node-cluster
	podman machine stop minikube-6-8
	podman machine rm minikube-6-8
	echo "CLEANUP DONE"
