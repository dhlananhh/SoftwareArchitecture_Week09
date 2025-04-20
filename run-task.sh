sh
#!/bin/bash

services=("api-gateway" "customer-service" "eureka-server" "identity-service" "order-service" "product-service")

for service in "${services[@]}"; do
  echo "Testing service: $service"
  cd "$service"
  if ! ./mvnw test; then
    echo "Tests failed for service: $service"
    exit 1
  fi
  cd ..
done

echo "All tests passed successfully."