#Pet Project Backend

##Описание Api
Безопасность основана на [JWT токене](https://jwt.io/).  
* Регистрация 
* Авторизация
* Remember Me
* Работа с постами:
    * Создание
    * Редактирование 
    * Получение
    * Удаление
    * Получение поста по id
* Работа с пользователем



###Для регистрации используется адрес **(POST) [/api/registration]()** 

Пример запроса:
```
{
    "username" : "test",
    "firstName" : "test",
    "lastName" : "test",
    "email" : "test@test.com",
    "password" : "test"
}
```
Ответ: 
```
{
    "registerUser": {
        "id": 7,
        "username": "test",
        "firstName": "test",
        "lastName": "test",
        "email": "test@test.com",
        "userpick": "none_ava.png"
    }
```
###Для авторизации используется адрес **(POST) [/api/auth/login]()**

Пример запроса:

```
{
   {
       "username":"test",
       "password":"test"
   }
}
```
Ответ: 
```
{
    "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNjMyNTM1OCwiZXhwIjoxNjA2MzI1OTU4fQ.GcWUgYOKTXDc7mCzbn20Kd5GHC68TZzAHn8kWa-8GsE",
    "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjA2MzI1MzU4LCJleHAiOjE2MTE1MDkzNTh9.JvnwLuZpW4Fp954KC-XmbAJwVshL5YnfkqBmPjoEWwM",
    "id": 7
}
```
В качестве ответа получаем пару токенов **access** и **refresh**

####Access токен 
Он живет 10 минут, после он становится не активным для получения новой пары access и refresh токенов 
надо воспользоваться адресом **(GET) [/api/auth/refresh]()** где в header "Authorization" будет передан
refresh токен, а также Fingerprint

Fingerprint - уникальный ключ устройства, который позволяет быть авторизованным с нескольких устройств и также накладывает дополнителный слой безопасности.

```
Authorization:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjA2MzI1MzU4LCJleHAiOjE2MTE1MDkzNTh9.JvnwLuZpW4Fp954KC-XmbAJwVshL5YnfkqBmPjoEWwM
Fingerprint: 62d126870786a9270aefb86eb12f4f7d
```
 Также в access токене закодированные данные о пользователе:
 
 Алгоритм кодирования Base64
 
 ```
{
  "sub": "test",
  "roles": [
    "ROLE_USER"
  ],
  "iat": 1606325358,
  "exp": 1606325958
}
```
iat - время выдачи токена

exp - время протухания токена

Все дальнейшие запроса на сервер должны содержать header Authorization с access токеном 

```
Authorization : Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNjMyNTM1OCwiZXhwIjoxNjA2MzI1OTU4fQ.GcWUgYOKTXDc7mCzbn20Kd5GHC68TZzAHn8kWa-8GsE
```

###Работа с постами
####Получение постов

(GET) [/api/posts]()

Ответ:

```
[
    {
        "id": 90,
        "text": "opnop",
        "authorId": 1,
        "count": 0,
        "meLiked": false,
        "updateDate": 1604852305,
        "fileNames": [
            "af138e1a-62f7-47e4-9476-dcb15769b658.Screenshot from 2020-11-08 17-54-31.png",
            "9a3864f6-2ae0-43a0-993f-8c63f8fd29be.Screenshot from 2020-11-08 12-58-24.png",
            "dee191b5-fe14-4305-ae20-f63acf5db7d5.Screenshot from 2020-11-08 12-57-17.png",
            "1dad545f-5eda-47f3-91f5-c6b08c0de433.Screenshot from 2020-11-08 12-35-12.png",
            "130b0400-841d-43fd-a0b1-f63e0d7d2904.Screenshot from 2020-11-04 17-31-26.png",
            "4280be18-a64a-4803-9099-68cb792dd3f6.Screenshot from 2020-11-03 22-31-50.png",
            "4af01ad8-3fa1-4771-9421-9007a6f3e0ec.Screenshot from 2020-11-02 19-38-34.png",
            "74f212c8-2f09-4b6d-bac4-92140aef031d.Canada_Parks_Lake_Mountains_Forests_Scenery_Rocky_567540_3840x2400-scaled.jpg"
        ]
    },
    {
        "id": 97,
        "text": "Лео",
        "authorId": 1,
        "count": 3,
        "meLiked": false,
        "updateDate": 1605029067,
        "fileNames": [
            "69097efa-722b-488e-ad91-9a2ea5e6e6d2.s1200.webp"
        ]
    },
    {
        "id": 98,
        "text": "sdf",
        "authorId": 1,
        "count": 0,
        "meLiked": false,
        "updateDate": 1605188383,
        "fileNames": [
            "dbcacddc-0700-49eb-acd7-e969781fca32.Canada_Parks_Lake_Mountains_Forests_Scenery_Rocky_567540_3840x2400-scaled.jpg"
        ]
    },
    {
        "id": 102,
        "text": "ffffffffffffffffffffffffffffffff",
        "authorId": 1,
        "count": 0,
        "meLiked": false,
        "updateDate": 1605188542,
        "fileNames": [
            "8ef76814-65f6-4f50-af00-5ee8efb87cd9.124321097_755284691731894_2467331361707079711_n.jpg"
        ]
    }
]
```
Для получения фотографий используется адрес: (GET) [/api/img/]()

####Для редактирования поста используется адрес (PUT) [/api/posts/{post_id}]()

Пример запроса:
[/api/posts/98]()
Обновить пост может только создатель поста, обновить можно только текст
```
{
    "text": "test text"
}
```

Ответ:

```
{
    "id": 98,
    "text": "test text",
    "authorId": 1,
    "count": 0,
    "meLiked": false,
    "updateDate": 1606327148,
    "fileNames": [
                "8ef76814-65f6-4f50-af00-5ee8efb87cd9.124321097_755284691731894_2467331361707079711_n.jpg"
            ]
}
```

####Для создания поста используется адрес (POST) [/api/posts/]()
Нужно добавить header : ```"Content-Type": "multipart/form-data"```

Пример запроса:
```
 {
    "text": "test text",
    "file": "fileName1",
    "file": "fileName2"
}
```

Пример ответа:

```
{
    "id": 104,
    "text": "test text",
    "authorId": 1,
    "count": 0,
    "meLiked": false,
    "updateDate": 1606328021,
    "fileNames": ["...fileName1", "...fileName2"]
}
```
####Для удаления поста используется адрес (DELETE) [/api/posts/]()


