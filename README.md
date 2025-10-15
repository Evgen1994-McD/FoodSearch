# 🍳 FoodSearch

**FoodSearch** - это Android приложение для поиска рецептов и ингредиентов с использованием Spoonacular API. Приложение позволяет пользователям находить рецепты, сохранять их в локальную базу данных и управлять своей коллекцией рецептов.

## 📱 Описание

FoodSearch предоставляет удобный интерфейс для:
- 🔍 Поиска рецептов по ключевым словам
- 🎲 Просмотра случайных рецептов
- 📚 Сохранения рецептов в локальную базу данных
- ❤️ Добавления рецептов в избранное
- 📖 Просмотра детальной информации о рецептах
- 🏷️ Поиска по категориям (завтрак, десерт, салат и др.)

## ✨ Основные функции

### 🔍 Поиск рецептов
- Поиск по названию или ингредиентам
- Пагинация с бесконечной прокруткой (Paging3)
- Поиск по категориям блюд
- Отображение случайных рецептов при запуске

### 📖 Детальная информация о рецептах
- Полная информация о рецепте
- Список ингредиентов с изображениями
- Пошаговые инструкции приготовления
- Информация о времени приготовления, порциях и стоимости
- Теги диет (вегетарианское, веганское, безглютеновое и др.)

### 💾 Локальное хранение
- Автоматическое кеширование найденных рецептов
- Сохранение избранных рецептов
- Работа в офлайн режиме с кешированными данными
- Управление кешем (очистка, удаление отдельных рецептов)

### 📚 Книга рецептов
- Просмотр всех сохраненных рецептов
- Раздел избранных рецептов
- Навигация между экранами

## 🏗️ Архитектура

Приложение построено с использованием **Clean Architecture** и следующих технологий:

### 📦 Технологический стек

- **Язык**: Kotlin
- **UI**: Jetpack Compose
- **Архитектура**: MVVM + Clean Architecture
- **DI**: Dagger Hilt
- **Сеть**: Retrofit + OkHttp
- **База данных**: Room
- **Пагинация**: Paging3
- **Навигация**: Navigation Compose
- **Загрузка изображений**: Coil
- **API**: Spoonacular API

### 🏛️ Структура проекта

```
app/src/main/java/com/example/foodsearch/
├── data/                    # Слой данных
│   ├── db/                  # База данных Room
│   │   ├── converters/      # Конвертеры типов
│   │   ├── dao/            # Data Access Objects
│   │   ├── entity/         # Сущности базы данных
│   │   └── MainDb.kt       # Главная база данных
│   └── search/             # Репозиторий поиска
│       ├── datasource/     # Источники данных
│       ├── dto/            # Data Transfer Objects
│       ├── impl/           # Реализации
│       ├── mapper/         # Мапперы
│       └── network/        # Сетевой слой
├── domain/                 # Слой бизнес-логики
│   ├── cache/             # Use cases для кеша
│   ├── common/            # Общие компоненты
│   ├── models/            # Доменные модели
│   └── search/            # Use cases для поиска
├── presentation/          # Слой представления
│   ├── book/              # Экран книги рецептов
│   ├── details/           # Экран деталей рецепта
│   ├── main/              # Главный экран
│   └── search/            # Экран поиска
├── di/                    # Dependency Injection
├── ui/                    # UI компоненты и темы
└── utils/                 # Утилиты
```

### 🔄 Поток данных

1. **Presentation Layer** - UI компоненты (Compose) и ViewModels
2. **Domain Layer** - Use Cases и бизнес-логика
3. **Data Layer** - Repository, Data Sources, Network, Database

## 🚀 Установка и запуск

### Предварительные требования

- Android Studio Arctic Fox или новее
- Android SDK 29+ (API Level 29)
- Kotlin 2.0.21+
- Gradle 8.9.3+

### Шаги установки

1. **Клонируйте репозиторий**
   ```bash
   git clone https://github.com/your-username/FoodSearch.git
   cd FoodSearch
   ```

2. **Откройте проект в Android Studio**
   - Запустите Android Studio
   - Выберите "Open an existing project"
   - Выберите папку FoodSearch

3. **Настройте API ключ Spoonacular**
   - Зарегистрируйтесь на [Spoonacular](https://spoonacular.com/food-api)
   - Получите бесплатный API ключ
   - Замените API ключ в файле `RetrofitNetworkClient.kt`:
   ```kotlin
   private val apiKey = "YOUR_API_KEY_HERE"
   ```

4. **Синхронизируйте проект**
   - Android Studio автоматически синхронизирует Gradle файлы
   - Дождитесь завершения загрузки зависимостей

5. **Запустите приложение**
   - Подключите Android устройство или запустите эмулятор
   - Нажмите кнопку "Run" в Android Studio

## 📱 Скриншоты

### Главный экран поиска
- Строка поиска рецептов
- Категории блюд (завтрак, десерт, салат, закуски, хлеб)
- Случайные рецепты при запуске
- Бесконечная прокрутка результатов

### Экран деталей рецепта
- Изображение рецепта
- Название и кнопка "лайк"
- Информационные карточки (порции, время, стоимость)
- Теги диет и типы блюд
- Список ингредиентов с изображениями
- Пошаговые инструкции приготовления

### Книга рецептов
- Все сохраненные рецепты
- Раздел избранных рецептов
- Возможность удаления рецептов

## 🔧 Конфигурация

### API настройки
Приложение использует Spoonacular API для получения данных о рецептах. Основные настройки находятся в `MainModule.kt`:

```kotlin
private const val BASE_URL = "https://api.spoonacular.com/"
```

### База данных
Локальная база данных Room настроена в `MainDb.kt`:
- Имя базы: `recipe.db`
- Версия: автоматически управляется Room
- Конвертеры для сложных типов данных

## 🧪 Тестирование

### Запуск тестов
```bash
# Unit тесты
./gradlew test

# Instrumented тесты
./gradlew connectedAndroidTest
```

### Структура тестов
- Unit тесты для бизнес-логики
- UI тесты для Compose компонентов
- Интеграционные тесты для репозитория

## 📦 Зависимости

Основные зависимости проекта:

```kotlin
// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose")

// Navigation
implementation("androidx.navigation:navigation-compose")

// Hilt
implementation("com.google.dagger:hilt-android")
ksp("com.google.dagger:hilt-compiler")

// Room
implementation("androidx.room:room-runtime")
ksp("androidx.room:room-compiler")

// Paging
implementation("androidx.paging:paging-runtime-ktx")
implementation("androidx.paging:paging-compose")

// Network
implementation("com.squareup.retrofit2:retrofit")
implementation("com.squareup.retrofit2:converter-gson")

// Image loading
implementation("io.coil-kt:coil-compose")
```

## 🤝 Вклад в проект

Мы приветствуем вклад в развитие проекта! Вот как вы можете помочь:

1. **Fork** репозиторий
2. Создайте **feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit** изменения (`git commit -m 'Add some AmazingFeature'`)
4. **Push** в branch (`git push origin feature/AmazingFeature`)
5. Откройте **Pull Request**

### Руководство по стилю кода
- Следуйте [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Используйте meaningful имена для переменных и функций
- Добавляйте комментарии для сложной логики
- Покрывайте новый код тестами

## 🐛 Известные проблемы

- [ ] Некоторые изображения ингредиентов могут не загружаться
- [ ] При первом запуске может потребоваться время для загрузки данных
- [ ] API ключ Spoonacular имеет ограничения на количество запросов

## 📄 Лицензия

Этот проект распространяется под лицензией MIT

## 👨‍💻 Автор

Авдейкин Евгений
Mrmarwell@gmail.com


