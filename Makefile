service_up:
	mvn clean package -DskipTests
	docker compose up -d --no-deps --build spring-web
