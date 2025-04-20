# Use a base image suitable for running Java applications
FROM maven:3.9.9-openjdk:latest

# Create a working directory
WORKDIR /app

# Copy the script (if it exists)
COPY run-task.sh .

# Make the script executable
RUN if [ -f "run-task.sh" ]; then chmod +x run-task.sh; fi

# Define a volume
VOLUME /app

# Set the entrypoint
ENTRYPOINT if [ -f "run-task.sh" ]; then ./run-task.sh; else echo "run-task.sh not found"; fi
