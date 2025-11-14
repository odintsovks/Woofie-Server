# Woofie-Server
Сервер для Woofie - https://github.com/odintsovks/Woofie

## API

### 1. Работа с текстом

#### GET /translations

Описание: запрос всех единиц перевода (пар "оригинал-перевод")

- Статус: `200 OK`
  - Тело:
    ```json
    {
      "timestamp": 0,
      "translations": [
        {
          "id": "",
          "sourceText": "",
          "targetText": "",
          "connections": [
            {
              "id": "",
              "description": ""
            },
            // ...
          ]
        },
        // ...
      ]
    }
    ```

#### POST /translations-fetch-updates

Описание: запрос обновлённых единиц перевода с определённой отметки времени

- Тело запроса:
  ```json
  {
    "timestamp": 0
  }
  ```

- Статус: `200 OK`
  - Тело:
    ```json
    [
      {
        "id": "",
        "sourceText": "",
        "targetText": "",
        "connections": [
          {
            "id": "",
            "description": ""
          },
          // ...
        ]
      },
      // ...
    ]
    ```
- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует поле `timestamp`

#### POST /translations

Описание: добавить единицу перевода

- Тело запроса:
  ```json
  {
    "sourceText": "",
    "targetText": "",
    "connections": [
      {
        "id": "",
        "description": ""
      },
      // ...
    ]
  }
  ```
  
- Статус: `201 Created`
  - Тело:
    ```json
    {
      "id": "",
      "sourceText": "",
      "targetText": "",
      "connections": [
        {
          "id": "",
          "description": ""
        },
        // ...
      ]
    }
    ```
- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует одно из полей

#### GET /translations/{id}

Описание: запросить единицу перевода по заданному индексу

- Статус: `200 OK`
  - Тело:
    ```json
    {
      "sourceText": "",
      "targetText": "",
      "connections": [
        {
          "id": "",
          "description": ""
        },
        // ...
      ]
    }
    ```
- Статус: `404 Not Found`
  - Единицы перевода не сущесвтует по заданному индексу

#### PUT /translations/{id}

Описание: установить значения единицы перевода по заданному индексу (добавить если не существует)

- Тело запроса:
  ```json
  {
    "sourceText": "",
    "targetText": "",
    "connections": [
      {
        "id": "",
        "description": ""
      },
      // ...
    ]
  }
  ```
  
- Статус: `200 OK` и `201 Created`
  - Тело
    ```json
    {
      "id": "",
      "sourceText": "",
      "targetText": "",
      "connections": [
        {
          "id": "",
          "description": ""
        },
        // ...
      ]
    }
    ```

- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует одно из полей.

#### DELETE /translations/{id}

Описание: удалить единицу перевода по заданному индексу

- Статус: `204 No Content`

- Статус: `404 Not Found`
  - Единицы перевода не существовало по заданному индексу перед запросом на удаление

### 2. Работа с глоссарием

#### GET /glossary

Описание: запрос списка записей глоссария

- Статус: `200 OK`
  - Тело:
    ```json
    {
      "timestamp": 0,
      "entries": [
        {
          "id": "",
          "targetTerm": "",
          "sourceTerm": "",
          "definition": ""
        },
        // ...
      ]
    }
    ```

#### POST /translations-fetch-updates

Описание: запрос обновлённых записей глоссария с определённой отметки времени

- Тело запроса:
  ```json
  {
    "timestamp": 0
  }
  ```

- Статус: `200 OK`
  - Тело:
    ```json
    [
      {
        "id": "",
        "targetTerm": "",
        "sourceTerm": "",
        "definition": ""
      },
      // ...
    ]
    ```
- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует поле `timestamp`



#### POST /glossary

Описание: добавить запись в глоссарий

- Тело запроса:
  ```json
  {
    "targetTerm": "",
    "sourceTerm": "",
    "definition": ""
  }
  ```
  
- Статус: `201 Created`
  - Тело:
    ```json
    {
      "id": "",
      "targetTerm": "",
      "sourceTerm": "",
      "definition": ""
    }
    ```
- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует одно из полей.

#### GET /glossary/{id}

Описание: запросить запись из глоссария по индексу

- Статус: `200 OK`
  - Тело:
    ```json
    {
      "targetTerm": "",
      "sourceTerm": "",
      "definition": ""
    }
    ```
- Статус: `404 Not Found`
  - Записи глоссария не сущесвтует по заданному индексу

#### PUT /glossary/{id}

Описание: изменить запись глоссария по индексу (добавить если не существует)

- Тело запроса:
  ```json
  {
    "targetTerm": "",
    "sourceTerm": "",
    "definition": ""
  }
  ```
  
- Статус: `200 OK` и `201 Created`
  - Тело
    ```json
    {
      "id": ""
      "targetTerm": "",
      "sourceTerm": "",
      "definition": ""
    }
    ```
- Статус: `400 Bad Request`
  - Неправильно сформирован запрос; отсутствует одно из полей.

#### DELETE /glossary/{id}

Описание: 

- Статус: `204 No Content`

- Статус: `404 Not Found`
  - Записи глоссария не существовало по заданному индексу перед запросом на удаление

### 3. Машинный перевод

#### GET /translation-services

Описание: запросить информацию о всех доступных сервисах машинного перевода

- Статус: `200 OK`
  - Тело:
    ```json
    [
      {
        "id": "",
        "name": ""
      },
      //...
    ]
    ```

#### POST /translate/{id}

Описание: запросить перевод строки у сервиса машинного перевода по заданному индексу

- Тело запроса:
  ```json
  {
    "text": ""
  }
  ```
  
- Статус: `200 OK`
  - Тело:
    ```json
    {
      "text": ""
    }
    ```
- Статус: `503 Service Unavailable`
  - Сервис перевода не доступен; сервер не получил ответ
