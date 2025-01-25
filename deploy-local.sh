#!/bin/bash

# Exit on any error
set -e

echo "Starting local deployment..."

# Create Docker network if it doesn't exist
if ! docker network inspect kyosk-network >/dev/null 2>&1; then
    echo "Creating Docker network: kyosk-network"
    docker network create kyosk-network
else
    echo "Docker network kyosk-network already exists"
fi

# Function to check if container exists and remove it
remove_if_exists() {
    if docker ps -a | grep -q $1; then
        echo "Removing existing $1 container"
        docker rm -f $1
    fi
}

# Clean up existing containers
remove_if_exists "mongodb"
remove_if_exists "kyosk-backend"
remove_if_exists "kyosk-frontend"

# Start MongoDB
echo "Starting MongoDB container"
docker run -d --name mongodb \
    --network kyosk-network \
    -p 27017:27017 \
    -e MONGO_INITDB_ROOT_USERNAME=admin \
    -e MONGO_INITDB_ROOT_PASSWORD=password123 \
    mongo:latest

# Wait for MongoDB to be ready
echo "Waiting for MongoDB to start..."
sleep 10

# Build and start backend
echo "Building backend service"
cd backend
docker build -t kyosk-backend .

echo "Starting backend service"
docker run -d --name kyosk-backend \
    --network kyosk-network \
    -p 8080:8080 \
    kyosk-backend

# Build and start frontend
echo "Building frontend service"
cd ../frontend
docker build -t kyosk-frontend \
    --build-arg REACT_APP_API_URL=http://localhost:8080 .

echo "Starting frontend service"
docker run -d --name kyosk-frontend \
    -p 3000:80 \
    kyosk-frontend

echo "Deployment completed!"
echo "Frontend available at: http://localhost:3000"
echo "Backend API available at: http://localhost:8080"

# Check container status
echo "\nContainer Status:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"