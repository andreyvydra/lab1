service_up:
	mvn clean package -DskipTests
	docker compose up -d --no-deps --build spring-web front

front:
	$(MAKE) -C frontend all
	docker compose up -d --no-deps --build front
