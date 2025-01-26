#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check if a pod is ready
wait_for_pods() {
    namespace=$1
    echo "Just a moment, waiting for pods in namespace $namespace to be ready..."
    kubectl wait --for=condition=ready pod --all -n "$namespace" --timeout=300s
}

# Check for required tools
for tool in minikube kubectl docker; do
    if ! command_exists "$tool"; then
        echo "Error: $tool is not installed"
        exit 1
    fi
 done

# Start Minikube if it's not running
if ! minikube status >/dev/null 2>&1; then
    echo "Starting Minikube..."
    minikube start --cpus 2 --memory 4096
else
    echo "Minikube is already running"
fi

# Enable Ingress addon
echo "Enabling Ingress addon..."
minikube addons enable ingress

# Wait for Ingress controller to be ready
echo "Just a moment, waiting for Ingress controller to be ready..."
kubectl wait --for=condition=ready pod -l app.kubernetes.io/component=controller -n ingress-nginx --timeout=300s

# Create namespace if it doesn't exist
if ! kubectl get namespace kyosk >/dev/null 2>&1; then
    echo "Creating kyosk namespace..."
    kubectl create namespace kyosk
fi

# Apply Kubernetes manifests
echo "Applying Kubernetes manifests..."
kubectl apply -f k8s/configmap.yaml -n kyosk
kubectl apply -f k8s/secret.yaml -n kyosk
kubectl apply -f k8s/mongodb.yaml -n kyosk

# Wait for MongoDB to be ready
echo "Just a moment, waiting for MongoDB to be ready..."
kubectl wait --for=condition=ready pod -l app=mongodb -n kyosk --timeout=300s

# Deploy application components
echo "Deploying application components..."
kubectl apply -f k8s/deployment.yaml -n kyosk
kubectl apply -f k8s/service.yaml -n kyosk
kubectl apply -f k8s/ingress.yaml -n kyosk

# Wait for all pods to be ready
wait_for_pods "kyosk"

# Add entry to /etc/hosts if it doesn't exist
if ! grep -q "kyosk.local" /etc/hosts; then
    echo "Adding kyosk.local to /etc/hosts..."
    echo "127.0.0.1 kyosk.local" | sudo tee -a /etc/hosts > /dev/null
fi

echo "Deployment completed successfully!"
echo "You can access the frontend at: http://kyosk.local"
echo "You can access the backend at: http://kyosk.local/api/books"

# Display pod status
echo "\nCurrent pod status:"
kubectl get pods -n kyosk

echo "You can access the frontend at: http://kyosk.local"
echo "You can access the backend at: http://kyosk.local/api/books"

echo "Starting tunnel, will block the terminal."

sudo minikube tunnel 