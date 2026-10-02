# CLAUDE.md - Dimension Access Manager

## Projekt-Übersicht

**Dimension Access Manager** ist ein NeoForge Minecraft Mod.
- **Mod ID**: `dimension_access_manager`
- **Package**: `de.geheimagentnr1.dimension_access_manager`
- **Java Version**: 21 (`develop_26.1`: 25, `jdk-25.0.4.7-hotspot`)
- **NeoForge Version**: je Branch, siehe Tabelle

Verwaltet den Zugang zu Dimensionen für Spieler.

| Branch | MC | Range | NeoForge (kompiliert gegen) | Hinweis |
|---|---|---|---|---|
| `develop_1.21.1` | 1.21.1 | `[1.21.1,1.21.2)` | 21.1.x | Fix-Release `1.21.1-4.0.2` (Config-`save()`) |
| `develop_1.21.2` | 1.21.2 - 1.21.4 | `[1.21.2,1.21.5)` | `21.2.1-beta` | `INBTSerializable` unverändert |
| `develop_1.21.5` | 1.21.5 | `[1.21.5,1.21.6)` | `21.5.98` | `CompoundTag`-API mit `Optional`, UUID über `UUIDUtil.CODEC` (gleiches Format) |
| `develop_1.21.6` | 1.21.6 - 1.21.8 | `[1.21.6,1.21.9)` | `21.6.20-beta` | `ValueIOSerializable` + `LegacyAttachmentMigrationHandler` (alte Int-/Listen-Attachments) |
| `develop_1.21.9` | 1.21.9 - 1.21.10 | `[1.21.9,1.21.11)` | `21.9.16-beta` | Spielerlisten als `NameAndId` (Format `Name` + `Id`) |
| `develop_1.21.11` | 1.21.11 | `[1.21.11,1.21.12)` | `21.11.45` | `Identifier`, konfigurierbare Rechtestufe über `PermissionLevel.byId(..)` |
| `develop_26.1` | 26.1 - 26.3 | `[26.1,27)` | `26.1.0.19-beta` (Java 25) | 26.x-Tooling; Migration aus der alten `neoforge_data_attachments.dat` (Int/Liste und Compound) |

Alle 4.0.2, released 2026-10-02. Lokaler Branch `wip_1.21.2_first_attempt_base` sichert einen früheren, verworfenen Codec-Versuch (las die UUID im falschen Format); `develop_1.21.3` ist ein alter Forge-Stand. Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) 4g.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Projektstruktur

```
src/main/java/de/geheimagentnr1/dimension_access_manager/
├── DimensionAccessManager.java                                    # Haupt-Mod-Klasse
├── config/
│   └── ServerConfig.java                                          # Server-Konfiguration
├── elements/
│   ├── capabilities/
│   │   ├── ModAttachmentTypes.java                                # Data Attachments Registry
│   │   ├── dimension_access/
│   │   │   ├── DimensionAccessCapability.java
│   │   │   └── DimensionAccessType.java
│   │   └── dimension_access_list/
│   │       ├── DimensionAccessListCapability.java
│   │       ├── dimension_access_blacklist/
│   │       │   └── DimensionAccessBlacklistCapability.java
│   │       └── dimension_access_whitelist/
│   │           └── DimensionAccessWhitelistCapability.java
│   ├── commands/
│   │   ├── DimensionsCommand.java                                 # /dimensions Command
│   │   ├── ModArgumentTypesRegisterFactory.java
│   │   ├── ModCommandsRegisterFactory.java
│   │   └── dimension/
│   │       ├── DimensionAccessTypeArgument.java
│   │       ├── DimensionCommand.java
│   │       ├── DimensionCommandAccessHelper.java
│   │       ├── DimensionCommandPlayersHelper.java
│   │       └── DimensionCommandRunner.java
├── handlers/
│   ├── DimensionAccessHandler.java                                # Zugangs-Handler
│   └── LegacyAttachmentMigrationHandler.java                      # ab 1.21.6: alte Attachments übernehmen
├── util/
│   └── ResourceLocationHelper.java
└── utils/
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
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Ab den 1.21.2+-Branches keine GameTests mehr (trivialer Smoke-Test samt Run-Config und CI-Job entfernt).

### Automatischer Test (RCON)

Nether sperren, Spieler auf Nether-Whitelist und End-Blacklist, `/dimensions default defaultDimensionAccessType LOCKED` (Großbuchstaben); dann abfragen, Neustart, erneut abfragen. Für Format-Upgrades die Welt eines älteren Packs kopieren. Ingame: Reise in den Nether/ins Ende per Portal oder `/execute in .. run tp @s ~ ~ ~` wird gemäß White-/Blacklist erlaubt bzw. blockiert.

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus

### Was kann automatisiert getestet werden?

| Aspekt | Automatisiert? | Methode |
|--------|----------------|---------|
| Utility-Klassen | ✅ | JUnit |
| Config-Parsing | ✅ | JUnit |
| Commands | ✅ | GameTest |
| Block/Item-Verhalten | ✅ | GameTest |
| Multi-MC-Version | ⚠️ Pro Branch | CI Matrix |

## Referenzen

- [NeoForge Migration Primer](https://docs.neoforged.net/primer/docs/) — Dokumentiert API-Aenderungen zwischen Minecraft/NeoForge-Versionen; nuetzlich fuer die Pruefung von Breaking Changes beim Upgrade auf neue Versionen

---

## Wissensdatenbank

Versionsübergreifende Migrations- und Entwicklungs-Erkenntnisse (Breaking Changes, Fixes, Testumgebungs-Patterns) werden zentral in [`../Docs/`](../Docs/) gepflegt. Bei neuen relevanten Erkenntnissen dort ergänzen, nicht nur hier.
