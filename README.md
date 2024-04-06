# waliot 🗺️

[![Build](https://github.com/virginonline/waliot/actions/workflows/main-ci.yml/badge.svg)](https://github.com/virginonline/waliot/actions/workflows/main-ci.yml)

## Как запустить

Для запуска необходим ключ от [yandex map api](https://yandex.ru/maps-api/)
<br/>
Если ключ уже есть то можно скопировать .env.example и вставить ключ туда
<br/>

```shell
make create-env
```

<br/>
Далее можно просто запустить docker compose следующей командой:

```shell
make compose
```

Или

```shell
docker compose up
```

Запуск тестов. Тесты также включены в пайплайн github actions

```shell
make test
```
