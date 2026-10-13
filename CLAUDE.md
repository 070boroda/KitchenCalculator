# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Язык общения

Все ответы пользователю — на русском языке.

## Команды сборки и запуска

Сборка ведётся через Gradle wrapper (Windows: `gradlew.bat`, *nix: `./gradlew`).

- Сборка debug APK: `gradlew.bat :app:assembleDebug`
- Сборка release APK: `gradlew.bat :app:assembleRelease`
- Установка на устройство: `gradlew.bat :app:installDebug`
- Очистка: `gradlew.bat clean`
- Unit-тесты: `gradlew.bat :app:testDebugUnitTest`
- Один тест: `gradlew.bat :app:testDebugUnitTest --tests "com.zelianko.kitchencalculator.SomeTest.someMethod"`
- Instrumented тесты: `gradlew.bat :app:connectedDebugAndroidTest` (нужен подключённый девайс/эмулятор)
- Lint: `gradlew.bat :app:lintDebug`

Минимальные требования: JDK 17, Android SDK 35 (compileSdk/targetSdk = 35, minSdk = 29). `applicationId = com.zelianko.kitchencalculator`.

## Архитектура

Однo-модульное Android-приложение (`:app`), полностью на Jetpack Compose + Hilt + Room.

### Слой данных (`data/`)

Room-база `MainDb` собирается из preseeded-ассета `app/src/main/assets/main.db` через `createFromAsset("main.db")` — миграции отключены, изменение схемы требует обновления ассета. Три сущности и три репозитория:

- `Recipe` / `RecipeDao` / `RecipeRepository[Impl]` — пользовательские рецепты.
- `ProductEn` / `ProductEnDao` / `ProductEnRepository[Imp]` — справочник продуктов (используется в калькуляторе).
- `CookingTime` / `CookingTimeDao` / `CookingTimeRepository[Impl]` — справочник времени приготовления.

Все бинды репозиториев и БД живут в `di/App.kt` (Hilt `@Module` в `SingletonComponent`). Схемы Room экспортируются в `app/schemas` (см. `ksp { arg("room.schemaLocation", ...) }`).

DI генерируется через **KSP** (миграция с kapt уже выполнена, коммит `4c0b817`). Hilt + Room compiler — оба через `ksp(...)`.

### UI и навигация

Стартовый Activity — `activity/FirstScreen.kt`. Навигация задаётся в `navigation/NavGrapf.kt` и `navigation/RecipeNavGraph.kt`. Маршруты централизованы в `util/Routes`. Стартовый экран — `Routes.RECIPE_LIST_SCREEN`.

Экраны разнесены по feature-пакетам верхнего уровня: `recipe_list_screen`, `recipe_add_screen`, `recipe_update_screen`, `recipt_about_screen`, `timer_screen`, `meat_screen`, `cook_temp_screen`, `sous_vide`, `subscriptions`. ViewModel-и обычно лежат рядом с экраном; общий `ProductViewModel` — в `modelview/`.

`subactivity/` и `dialog/` содержат вспомогательные активити и диалоги (sheets-compose-dialogs).

### Реклама, биллинг, аналитика

- **Yandex Ads** (`yandex_ads/`) — основной рекламный SDK (`com.yandex.android:mobileads-mediation`, версия `yandexAds` в `libs.versions.toml`), включая `AppOpenAdManager` и баннеры. ID баннеров — в `BannerId.kt`. Из медиации через `exclude` в `app/build.gradle.kts` убран BidMachine (`io.bidmachine*` + `com.unity3d.ads-mediation:bidmachine-adapter`): его SDK лежит в отдельном репозитории и раздувает сборку до OOM в R8. При обновлении Yandex Ads проверять, не появились ли новые артефакты BidMachine.
- **Google Ads** (`google_ads/`) — присутствует код (`AppOpenAdManager`, `GoogleBannerAd`), но Play Services Ads зависимость закомментирована в `app/build.gradle.kts`. При работе с гугл-рекламой проверять, не отключён ли SDK.
- **Биллинг** (`subscriptions/`) — `BillingViewModel`, экраны `ChooseSubscription` и `SubscribesScreen`. Прямая зависимость `billing`/`billing-ktx` сейчас **закомментирована** в `app/build.gradle.kts` (коммит `107d021` «убрал биллинг — сборка для GP»). При этом Play Billing всё равно попадает в сборку транзитивно: Yandex Ads → myTarget → MyTracker → `com.android.billingclient:billing`. Google Play требует billing **8.0.0+** (с 30.08.2026), сейчас резолвится 9.1.0. После обновления рекламных SDK проверять версию: `gradlew.bat :app:dependencies --configuration releaseRuntimeClasspath` и искать `billingclient`.
- **Аналитика**: Firebase Analytics + Crashlytics (`firebase-bom:34.0.0`, `google-services.json` в `app/`) и Yandex AppMetrica (`io.appmetrica.analytics:analytics`).

### Прочее

- `datastore/` — DataStore Preferences (флаги, настройки).
- `lottie/` — Lottie-анимации (`com.airbnb.android:lottie-compose`).
- `constants/`, `util/`, `model/` — константы, утилиты, доменные модели.
- Composer-плагин Kotlin 2.2.10, Compose BOM `2025.07.00`, `kotlinCompilerExtensionVersion = "2.0.0"`.

## Особенности окружения

- Shell — PowerShell, использовать `gradlew.bat`, а не `./gradlew`.
- Дополнительные maven-репозитории (jitpack, mintegral, ironsource, pangle, tapjoy, chartboost, appnext) подключены в `settings.gradle.kts` для медиации Yandex Ads — при сетевых ошибках сборки смотреть туда.
- `release` build-тип собирается **с R8**: `isMinifyEnabled = true` + `isShrinkResources = true`, правила — в `app/proguard-rules.pro` (включено в коммите `30d4912`). В `debug` minify выключен, поэтому проблемы R8 (вырезанные/переименованные классы, используемые через рефлексию: Room, Hilt, рекламные SDK) видны только в release — после изменения зависимостей или keep-правил проверять именно release-сборку на устройстве. Для читаемых стектрейсов в Crashlytics нужен `mapping.txt`.
