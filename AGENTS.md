# AGENTS.md - Dimension Access Manager

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
