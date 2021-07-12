# MARVEL HEROES: 
# Android Kotlin + MVVM + Clean Architecture APP demo by myanes
Sample Android app with implementation for every layer of Clean Architecture:

## Presentation <-> Domain <-> Data
- Presentation: Screens/Views + ViewModels
- Domain: Models + UseCases
- Data: DTOs + Repositories + DataSources

## Libraries 📦
- View binding
- LiveData
- View models
- Coroutines
- Navigation (Navigaion graph)
- Image loading with Picasso
- Networking with Ktor
- 'Dependency injection' with Koin
- Error handling with class Either<L, R> -> 'inspired' by https://arrow-kt.io/docs/0.10/apidocs/arrow-core-data/arrow.core/-either/
- Testing with Mockito & JUnit4

## Features 📋

It consists on two screen for showing list of heroes its details.
Data comes from MARVEL API: https://developer.marvel.com/docs

- **Splash screen** with custom android + Marvel logo
- **List screen:** 
It shows latest modified characters from marvel.
It support searching by name
- **Detail screen:**
Shows name, image, description and counters of comics, events, stories and series
- **Light/Dark** modes (from system settings)
- **Survives to rotation changes**: When the devices rotates the app keeps state
- **Error handling**: When networking fails it updates the ui accordingly

## Testing ⚙️

**Unit tests** are available for both List and Detail **ViewModels.**
It verifies all events are handled and dispatched from/to ui fragments.
It mocks usecases responses.
It supports coroutines.

Some examples:

```
SHOW/HIDE LOADING INDICATOR
SHOW/HIDE EMPTY UI
SHOW ERROR MSG
ENTER/LEVAVE SEARCHING MODE
LIST UPDATED AFTER DATA RESULTS
LIST CLEAR BEFORE NEW SEARCH
[...]
and more
```

## Run 📲

It targets android > 6.0
Sample APK is provided here:

[apk_debug](sample/app-debug.apk)


## License 📄

Feel free to use it

---
con ❤️ by [myanes](https://github.com/myanes-dev) 😜
