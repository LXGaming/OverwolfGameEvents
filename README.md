# OverwolfGameEvents

[![License](https://img.shields.io/github/license/LXGaming/OverwolfGameEvents?label=License&cacheSeconds=86400)](https://github.com/LXGaming/OverwolfGameEvents/blob/master/LICENSE)

## Natives
[overwolf/game-events-sdk](https://github.com/overwolf/game-events-sdk)
- `src/main/resources/win32-x86/libowgameevents.dll` from [libowgameevents.dll](https://github.com/overwolf/game-events-sdk/blob/master/libowgameevents/lib/Release/Win32/libowgameevents.dll)
- `src/main/resources/win32-x86-64/libowgameevents.dll` from [libowgameevents.dll](https://github.com/overwolf/game-events-sdk/blob/master/libowgameevents/lib/Release/x64/libowgameevents.dll)

### API
Repository: `mavenCentral()`
<br>
Dependency: `io.github.lxgaming:overwolfgameevents:1.0.0`

## Usage
```java
// Create a key.
Key usernameKey = Key.of("username", 1024);

// Create a category and register keys.
Category playerCategory = Category.of("player", usernameKey);

// Create an overwolf instance and register categories.
Overwolf overwolf = Overwolf.of(1234, playerCategory);

// Connect to the Overwolf SDK.
overwolf.connect();

// Set the value of the key.
overwolf.set(usernameKey, "LX_Gaming");

// Trigger an event.
overwolf.triggerEvent(Event.of("event-name", "event-data"));

// Close the connection.
overwolf.disconnect();
```

## License
OverwolfGameEvents is licensed under the [Apache 2.0](https://github.com/LXGaming/OverwolfGameEvents/blob/master/LICENSE) license.