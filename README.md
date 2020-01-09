1. Скачать и установить IntelliJ IDEA по ссылке https://www.jetbrains.com/ru-ru/idea/
2. Установаить и настроить JAVA jdk:
    - https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
    - https://java-course.ru/begin/install-jdk/
3. Установить и настроить Node.js:
    - https://htmlacademy.ru/blog/useful/nodejs/installing-nodejs
4. Скчать, установить и настроить PostgreSQL https://www.postgresql.org/download/
    - задать пользователя postgres
    - задать пароль 44952649Hs
    - порт 5432
    - создать базу postgres
    - создать схему public
5. Установить и натсроить Git:
    - https://www.examclouds.com/ru/java/java-core-russian/clone-from-github
6. Склонировть и открыть проект в IntelliJ IDEA
7. Выполнить команду mvn clean package
8. Запустить программу (файл SpringBootAngularJsApplication.java)

После запуска открыть страницу программы, по умолчанию http://localhost:8080/
    - Страница содержит 4 вкладки: список сотрудников, список организаций, дерево сотрудников и дерево сотрудников
    - Изначально база пустая и что бы протестировать нужно создать организацию\и на вкладке «список организаций»
    - Далее добавляем сотрудника\ов на вкладке «Список сотрудников»
    - После этого сотрудников и организации можно редактировать, удалять если нет дочерних элементов
    - На страницах «список сотрудников» и «список организаций» можно фильтровать по имени сотрудника или организации
    - На вкладках «дерево сотрудников» и «дерево организаций» создается список в деревовидной форме. Первым делом подгружаются организации, сотрудники у которых нет родительских элементов, в потом есть возможность подгрузить дочернии элементы этих сущностей, если они есть
   
    
    
