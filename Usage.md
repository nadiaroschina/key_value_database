## Использование программы
В этом проекте реализуется консольный интерфейс к базе данных, поддерживающей операции поиска, вставки
и удаления текстовых значений по соответствующим им текстовым ключам. 

База данных хранится в отдельной директории `src/data` и сохраняется между запусками утилиты. Для работы с базой
используются команды, заданные в параметрах командной строки.

### Добавление элемента
Для добавления пары (или нескольких пар) ключ-значение в базу данных нужно указать ключевое слово `add`, а далее
перечислить сами пары ключ-значение, разделяя каждые два слова пробелом. База данных поддерживает хранение ключей и значений, 
являющимися строками любой длины из любых непробельных символов. Ключ не должен содержать символов / или \.
```
$ db add salad tomato,cucumber soup water,meat,potato
$ db add salad pepper,carrot soup water,meat,potato
```

```
Key salad now has the value tomato,cucumber
Key soup now has the value water,meat,potato

Key salad changed its value to pepper,carrot. Old value was tomato,cucumber
Key soup already stores value water,meat,potato
```
При вызове команды `add` с неправильными аргументами (нечетным числом строк) 
программа выбросит соответствующее исключение. При вызове команды для нескольких пар ключ-значение, некоторые из которых
некорректны, программа добавит только валидные ключи и значения

```
$ db add cake flour,eggs,sugar smoothie/juice berries,cream
```

```
Key cake now has the value flour,eggs,sugar
Key smoothie/juice is invalid
```

### Удаление элемента
Для удаления одного или нескольких ключей из базы данных нужно указать ключевое слово `delete`, а далее
перечислить сами ключи, разделяя каждые два слова пробелом. 
```
$ db add smoothie banana,apple,spinach lemonade water,sugar,lemon
$ db delete smoothie aperol_spritz coca\cola
```

```
Key smoothie now has the value banana,apple,spinach
Key lemonade now has the value water,sugar,lemon

Key smoothie deleted; its value was banana,apple,spinach
Key aperol_spritz not found in database
Key coca\cola is invalid
```


### Получение значения по ключу
Для получения одного или нескольких значений по ключам нужно указать ключевое слово `get`, а далее
перечислить сами ключи, разделяя каждые два слова пробелом.

```
$ db add mojito rum,soda,lemon bloody_mary vodka,tomato_juice
$ db get bloody_mary margarita gin/tonic
```

```
Key mojito now has the value rum,soda,lemon
Key bloody_mary now has the value vodka,tomato_juice

vodka,tomato_juice
null
null
```

При неправильном или несуществующем значении ключа `get` возвращает `null`

### Дополнительные команды
Вызов `clear` без параметров удалит все ключи и их значения из базы данных.
```
$ db clear
```

### Обработка ошибок
При вызове программы без параметров, с неправильным запросом или некорректными значениями аргументов выбрасываются
соответствующие исключения.