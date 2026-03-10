# Specify java runtime base image
FROM amazoncorretto:25-alpine

# CVE-2026-22184
RUN apk update && \
    apk add --no-cache --upgrade zlib && \
    rm -rf /var/cache/apk/*

# Set up working directory in the container
RUN mkdir -p /opt/laa-dwp-benefit-checker-2.0/
WORKDIR /opt/laa-dwp-benefit-checker-2.0/

# Copy the JAR file into the container
COPY bc-service/build/libs/bc-service-1.0.0.jar app.jar

# Create a group and non-root user
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup

# Set the default user
USER 1001

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
CMD java -jar app.jar