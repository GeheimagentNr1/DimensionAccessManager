# CLAUDE.md - Dimension Access Manager

Dimension Access Manager sperrt oder öffnet Dimensionen pro Welt: jede Dimension ist `GRANTED` (mit Blacklist) oder `LOCKED` (mit Whitelist), Reisen von Spielern und Entities werden entsprechend abgebrochen. Mod-ID `dimension_access_manager`, Package `de.geheimagentnr1.dimension_access_manager`. In der Workspace-Übersicht als **Client+Server** geführt (kein `@Mod( dist = .. )`); die Logik läuft rein serverseitig, der Mod registriert aber einen eigenen Befehls-Argument-Typ (`DimensionAccessTypeArgument`).

Diese Datei ist auf allen `develop_*`-Branches identisch; Quelle: `../Docs/mod-claude/DimensionAccessManager.md` (Änderungen dort, dann `Docs/tools/sync_mod_claude_md.sh`). Versionsstand: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Versionen

- Forge: `develop_1.18.2` .. `develop_1.21`, `develop_1.21.1_forge` (letzter Forge-Stand 1.21.1).
- NeoForge: sieben Jars - `develop_1.21.1`, `develop_1.21.2` (1.21.2 - 1.21.4), `develop_1.21.5`, `develop_1.21.6` (1.21.6 - 1.21.8), `develop_1.21.9` (1.21.9 - 1.21.10), `develop_1.21.11`, `develop_26.1` (ein Jar 26.1 - 26.3). Schnitte wegen Level-Attachment-API, `NameAndId` und Rechtesystem, Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) Abschnitt 4g.
- Ranges, Tags und Java-Versionen pro Branch: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Abhängigkeiten

- Keine Mod-Abhängigkeiten. Forge 1.20.1 - 1.21.1-Forge: MinecraftForgeAPI per `jarJar` gebündelt (`AbstractMod`, `AbstractConfig`), siehe [`../Docs/forge/README.md`](../Docs/forge/README.md).

## Architektur

- `DimensionAccessManager` - Mod-Klasse; registriert Server-Config, `ModAttachmentTypes`, `ModArgumentTypesRegisterFactory`, Befehle und Handler.
- `elements/capabilities/` - pro Dimension (Level) drei Daten: `DimensionAccessCapability` (`DimensionAccessType` GRANTED/LOCKED), `DimensionAccessWhitelistCapability`, `DimensionAccessBlacklistCapability` (Spielerlisten). NeoForge: Level-Attachments in `ModAttachmentTypes`; Forge: Capabilities über `ModCapabilitiesRegisterFactory`.
- `elements/commands/` - `DimensionsCommand` (`/dimensions`), `dimension/DimensionCommand` (`/dimension`) mit Helfern (`DimensionCommandAccessHelper`, `DimensionCommandPlayersHelper`, `DimensionCommandRunner`) und `DimensionAccessTypeArgument`.
- `handlers/DimensionAccessHandler` - bricht `EntityTravelToDimensionEvent` ab: Spieler bei GRANTED nur, wenn auf der Blacklist, bei LOCKED, wenn nicht auf der Whitelist; andere Entities bei LOCKED immer.
- `handlers/LegacyAttachmentMigrationHandler` (ab 1.21.6) - übernimmt alte Attachment-Daten einmalig in `LevelEvent.Load` (siehe Fallstricke).
- `utils/GameProfileUtils`, `util/ResourceLocationHelper` - Hilfsklassen.

## Funktionen, Befehle, Konfiguration

- `/dimensions status` - alle Dimensionen mit Zugriffsart (ohne Rechte).
- `/dimensions default defaultDimensionAccessType [GRANTED|LOCKED]` - Standard für neue Dimensionen anzeigen/setzen (Rechtestufe 3, ab 1.21.11 `LEVEL_ADMINS`); Werte in Großbuchstaben.
- `/dimension <dim> access status` (ohne Rechte), `/dimension <dim> access grant|lock`, `/dimension <dim> players list`, `... players whitelist|blacklist list|add|remove <spieler>` - Rechtestufe aus der Config. Hinzufügen zur Whitelist entfernt den Spieler von der Blacklist.
- Config `world/serverconfig/dimension_access_manager-server.toml`: `default_dimension_access_type` (Standard `GRANTED`), `dimension_command_permission_level` (0 - 4, Standard 3; Änderungen wirken ohne Neustart).
- Weltdaten: Level-Attachments pro Dimension (Zugriffsart + Listen mit `Name` und `Id`).

## Fallstricke

- **Config `save()`**: `ConfigValue.set(..)` speichert nicht - `setDefaultDimensionAccessType` ruft `save()` (gefixt in 4.0.2, auch Fix-Release `1.21.1-4.0.2`).
- **Attachment-Format**: bis NeoForge 21.5 `INBTSerializable` (Int bzw. Liste), ab 1.21.6 `ValueIOSerializable` (Compound) - alte Daten werden nicht mehr gelesen, deshalb `LegacyAttachmentMigrationHandler`. Ab 26.1 liegt die Datei unter `dimensions/<ns>/<path>/data/neoforge/data_attachments.dat`, das Welt-Upgrade lässt die alte `neoforge_data_attachments.dat` liegen - der Handler übernimmt beide Alt-Formate. Eine einmalige ERROR-Zeile „Expected … COMPOUND“ beim ersten Laden alter Welten ist erwartet.
- **Kein Codec-Umbau der Listen**: ein früherer Versuch las die UUID als String statt Int-Array - bestehende Listen wären verloren gegangen. UUIDs über `UUIDUtil.CODEC` (gleiches Format).
- **1.21.9**: `GameProfile` ist ein Record, Listen intern als `NameAndId`; alte `Properties` werden ignoriert. **1.21.11**: `hasPermission(int)` entfernt, Rechtestufe per `PermissionLevel.byId(..)`. Siehe [`../Docs/migrations/BREAKING_CHANGES.md`](../Docs/migrations/BREAKING_CHANGES.md).
- GameTest-Smoke-Test nur auf `develop_1.21.1`; ab 1.21.2 entfernt.

## Testen

- Per RCON: Nether sperren, Spieler auf Nether-Whitelist und End-Blacklist, `/dimensions default defaultDimensionAccessType LOCKED`; abfragen, Neustart, erneut abfragen (Config-Datei prüfen).
- Format-Upgrades: Welt eines älteren Packs mit gesetzten Daten kopieren und abfragen (Kette 1.21.4 → 1.21.5 → 1.21.6 → 1.21.9 → 1.21.11 → 26.1 verifiziert).
- Ingame: Reise in Nether/Ende per Portal oder `/execute in .. run tp @s ~ ~ ~` wird gemäß White-/Blacklist erlaubt bzw. blockiert.
- Allgemeine Regeln: [`../Docs/testing/README.md`](../Docs/testing/README.md).

## Links

- Wiki: https://github.com/GeheimagentNr1/DimensionAccessManager/wiki
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/dimension-access-manager (Projekt-ID 388197)
- Modrinth: https://modrinth.com/mod/dimension-access-manager (Projekt-ID `8KrgVqz6`)
- [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) (4g), [`../Docs/migrations/1.21.11-to-26.1.md`](../Docs/migrations/1.21.11-to-26.1.md), [`../Docs/releasing/release-process.md`](../Docs/releasing/release-process.md)
