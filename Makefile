# windows boilerplate
ifeq ($(OS),Windows_NT)
    SHELL := pwsh.exe
else
   SHELL := pwsh
endif
.SHELLFLAGS := -NoProfile -Command


compose:
	docker compose up
create-env:
	cp .env.example .env.local
test:
	./gradlew test
