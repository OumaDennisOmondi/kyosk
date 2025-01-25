# Full-Stack Application with Kubernetes Deployment

This project demonstrates a containerized full-stack application deployment using Spring Boot, React, and MongoDB, with CI/CD automation through GitHub Actions and deployment on Minikube.

## Project Structure

```
├── backend/          # Spring Boot REST API
├── frontend/         # React frontend application
├── k8s/              # Kubernetes manifests
└── .github/workflows # GitHub Actions CI/CD pipeline
```

## Components

1. Backend (Spring Boot)
   - REST API with /health and /books endpoints
   - MongoDB integration
   - Containerized with Docker
   - Health monitoring endpoints
   - Swagger API documentation

2. Frontend (React)
   - Modern, responsive book list display
   - Real-time integration with backend API
   - Error handling and loading states
   - Containerized with Nginx for production

3. Database
   - MongoDB for data persistence
   - Configurable authentication
   - Volume persistence in Kubernetes

## Local Development Setup

### Prerequisites

- Java 17+
- Node.js 16+
- Docker
- Minikube
- kubectl

### Environment Configuration

1. Backend Configuration:
   - Create `.env` file in backend directory:
   ```
   MONGODB_URI=mongodb://admin:password123@localhost:27017
   MONGODB_DATABASE=booksdb
   SERVER_PORT=8080
   ```

2. Frontend Configuration:
   - Create `.env` file in frontend directory:
   ```
   VITE_API_URL=http://localhost:8080
   ```

### Building and Running with Docker

1. Create a Docker network:
```bash
docker network create kyosk-network
```

2. Run MongoDB:
```bash
docker run -d --name mongodb \
  --network kyosk-network \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password123 \
  mongo:latest
```

3. Build and run the backend:
```bash
cd backend
docker build -t kyosk-backend .
docker run -d --name kyosk-backend \
  --network kyosk-network \
  -p 8080:8080 \
  kyosk-backend
```

4. Build and run the frontend:
```bash
cd frontend
docker build -t kyosk-frontend .
docker run -d --name kyosk-frontend \
  -p 3000:80 \
  -e REACT_APP_API_URL=http://localhost:8080 \
  kyosk-frontend
```

### Verifying the Setup

1. Check container status:
```bash
docker ps
```

2. View container logs:
```bash
docker logs kyosk-backend
docker logs kyosk-frontend
```

3. Access the applications:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/books
- Swagger UI: http://localhost:8080/swagger-ui.html

## Kubernetes Deployment

### Prerequisites

1. Start Minikube with sufficient resources:
```bash
minikube start --cpus 2 --memory 4096
```

2. Enable required addons:
```bash
minikube addons enable ingress
minikube addons enable metrics-server

# Verify ingress controller is running
kubectl get pods -n ingress-nginx
```

### Deployment Steps

1. Create namespace:
```bash
kubectl create namespace kyosk
```

2. Create ConfigMap and Secrets:
```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
```

3. Deploy MongoDB:
```bash
kubectl apply -f k8s/mongodb/
```

4. Deploy application components:
```bash
kubectl apply -f k8s/
```

5. Verify deployment:
```bash
kubectl get pods -n kyosk
kubectl get services -n kyosk
```

6. Access the application:
```bash
minikube service kyosk-frontend --url -n kyosk
```

## CI/CD Pipeline

The GitHub Actions workflow (`ci-cd.yaml`) automates the following steps:

1. Build and Test
   - Runs unit tests for backend and frontend
   - Performs code quality checks
   - Generates test coverage reports

2. Build and Push Images
   - Builds Docker images for both services
   - Tags images with Git SHA and latest
   - Pushes to GitHub Container Registry

3. Deploy to Kubernetes
   - Updates deployment manifests
   - Applies changes to Minikube cluster
   - Verifies deployment status

### Environment Variables

Required GitHub Secrets:
- `GHCR_TOKEN`: GitHub Container Registry access token
- `KUBE_CONFIG`: Base64 encoded kubeconfig file

## Development Decisions

1. **Architecture**:
   - Microservices architecture for independent scaling
   - Containerization for consistent environments
   - API-first design approach

2. **Technology Stack**:
   - Spring Boot: Robust backend framework
   - React: Modern frontend development
   - MongoDB: Flexible document database
   - Nginx: Production-grade web server

3. **Container Registry**:
   - GitHub Container Registry for tight integration
   - Automated image tagging and versioning

## Troubleshooting

1. **Container Issues**:
   - Check logs: `docker logs <container-name>`
   - Verify network: `docker network inspect kyosk-network`
   - Restart container: `docker restart <container-name>`

2. **Kubernetes Issues**:
   - Check pod status: `kubectl describe pod <pod-name> -n kyosk`
   - View logs: `kubectl logs <pod-name> -n kyosk`
   - Restart deployment: `kubectl rollout restart deployment <deployment-name> -n kyosk`

3. **Application Issues**:
   - Backend health: `curl http://localhost:8080/actuator/health`
   - MongoDB connection: Check backend logs for connection errors
   - Frontend API: Check browser console for API errors