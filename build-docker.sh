#!/bin/bash
#Author: Ignacio Illanes Bequer

# package the app
./mvnw clean package -DskipTests
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#build docker image
echo "####################"
echo "Building docker image"
echo "####################"

echo -n "Docker container name (ms-users): "
read container_name

if [ -z "$container_name" ]; then
    container_name="ms-users"
fi

while [[ -z "$version_tag" ]]; do
  echo -n "$container_name version tag: "
  read version_tag

  if [[ -z "$version_tag" ]]; then
    echo "Version tag cannot be empty!"
  fi
done

docker build -t $container_name:$version_tag .

echo "####################"
echo "Docker image built"
echo "####################"

echo "would you like to deploy the image to docker hub? (y/n)"
read deploy

if [ "$deploy" != "y" ]; then
  echo "Operation completed successfully!"
  exit 0
fi

echo "Deploying docker image..."
docker login

sleep 5
docker tag $container_name:$version_tag ucbdegree/$container_name:$version_tag
sleep 2

docker push ucbdegree/$container_name:$version_tag

if [ $? -eq 0 ]; then
  echo "#################################"
  echo "Operation completed successfully!"
  echo "#################################"
else
  echo "Operation failed."
fi
