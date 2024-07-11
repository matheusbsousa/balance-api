.PHONY: build
build:
	@echo "Building image"
	./gradlew build && docker image rm balance-api && docker build -t balance-api .