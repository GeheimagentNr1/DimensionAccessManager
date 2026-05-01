# CLAUDE.md - Dimension Access Manager

## Projekt-Übersicht

**Dimension Access Manager** ist ein NeoForge Minecraft Mod für Minecraft 1.21.1.
- **Mod ID**: `dimension_access_manager`
- **Package**: `de.geheimagentnr1.dimension_access_manager`
- **Java Version**: 21
- **NeoForge Version**: 21.1.x

Verwaltet den Zugang zu Dimensionen für Spieler.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Projektstruktur

```
src/main/java/de/geheimagentnr1/dimension_access_manager/
├── DimensionAccessManager.java                # Haupt-Mod-Klasse
├── config/
│   └── ServerConfig.java                      # Server-Konfiguration
├── elements/
│   ├── capabilities/
│   │   └── ModAttachmentTypes.java            # Data Attachments
│   └── commands/
│       ├── DimensionsCommand.java             # /dimensions Command
│       ├── ModArgumentTypesRegisterFactory.java
│       └── ModCommandsRegisterFactory.java
├── handlers/
│   └── DimensionAccessHandler.java            # Zugangs-Handler
└── util/
    ├── ResourceLocationHelper.java
    └── GameProfileUtils.java
```

## Besonderheiten

- **Data Attachments**: Nutzt NeoForge Data Attachments für Spieler-Daten
- **Commands**: Custom Commands für Dimensions-Verwaltung
- **Custom Argument Types**: Eigene Command-Argument-Typen

## Code-Stil

- **Annotations**: `@NotNull` aus `org.jetbrains.annotations`
- **Lombok**: Projekt nutzt Lombok
- **Formatierung**: Leerzeichen nach `(` und vor `)` bei Methodenaufrufen

## Build & Test

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

## Deployment

- **CurseForge**: `./gradlew curseforge`
- **Modrinth**: `./gradlew modrinth`

## Testing

### Java-Versionen

Verschiedene Java-Versionen sind unter `C:\Program Files\Eclipse Adoptium` installiert. Für einen Gradle-Build muss die passende Java-Version gewählt werden:

```powershell
# Java 21 für MC 1.20.5+ (NeoForge)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Für Integration Tests in einer echten Minecraft-Umgebung:

```bash
./gradlew runGameTestServer
```

GameTest-Klassen werden mit `@GameTestHolder` annotiert und liegen unter `src/main/java/.../elements/gametests/`.

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus
3. **GameTests**: Startet GameTestServer (optional)

### Was kann automatisiert getestet werden?

| Aspekt | Automatisiert? | Methode |
|--------|----------------|---------|
| Utility-Klassen | ✅ | JUnit |
| Config-Parsing | ✅ | JUnit |
| Commands | ✅ | GameTest |
| Block/Item-Verhalten | ✅ | GameTest |
| Multi-MC-Version | ⚠️ Pro Branch | CI Matrix |
