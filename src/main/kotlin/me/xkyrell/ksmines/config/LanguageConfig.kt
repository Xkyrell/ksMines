package me.xkyrell.ksmines.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import me.xkyrell.ksmines.config.section.TitleSection
import me.xkyrell.ksmines.util.locale.LocaleComponent
import me.xkyrell.ksmines.util.TemporalStyles
import me.xkyrell.temporal.TemporalUnit
import me.xkyrell.temporal.format.registry.TemporalEntry
import me.xkyrell.temporal.format.registry.TemporalRegistry
import me.xkyrell.temporal.format.style.TextualTemporalStyle

@Serializable
data class LanguageConfig(
    @Contextual val prefix: LocaleComponent = LocaleComponent(
        "<#A7F432><bold>ksMines ><bold/> "
    ),
    @Contextual val noPermission: LocaleComponent = LocaleComponent(
        "<prefix><red>У вас недостаточно прав."
    ),
    @Contextual val reload: LocaleComponent = LocaleComponent(
        "<prefix><green>Конфигурация успешно перезагружена."
    ),
    @Contextual val templateNotFound: LocaleComponent = LocaleComponent(
        "<prefix><red>Шаблон шахты <white><template></white> не найден."
    ),
    @Contextual val mineNotFound: LocaleComponent = LocaleComponent(
        "<prefix><red>Шахта <white><mine></white> не найдена."
    ),
    @Contextual val invalidMine: LocaleComponent = LocaleComponent(
        "<prefix><red>Невозможно создать шахту с таким именем, используйте "
                + "формат: <white><world></white>:<white><name><white/>.</red>"
    ),
    @Contextual val mineAlreadyExists: LocaleComponent = LocaleComponent(
        "<prefix><red>Шахта <white><mine></white> уже существует."
    ),
    @Contextual val mineInactive: LocaleComponent = LocaleComponent(
        "<prefix><red>Шахта <white><mine></white> сейчас неактивна."
    ),
    @Contextual val mineCreated: LocaleComponent = LocaleComponent(
        "<prefix><green>Шахта <white><mine></white> успешно создана."
    ),
    @Contextual val mineRemoved: LocaleComponent = LocaleComponent(
        "<prefix><green>Шахта <white><mine></white> успешно удалена."
    ),
    @Contextual val mineRefilled: LocaleComponent = LocaleComponent(
        "<prefix><green>Шахта <white><mine></white> успешно перезаполнена, благодаря <player>."
    ),
    @Contextual val mineRotated: LocaleComponent = LocaleComponent(
        "<prefix><green>Шахта <white><mine></white> переключена на следующий этап, благодаря <player>."
    ),
    @Contextual val mineTeleported: LocaleComponent = LocaleComponent(
        "<prefix><green>Вы телепортированы на шахту <white><mine></white>."
    ),
    @Contextual val mineRegionMissing: LocaleComponent = LocaleComponent(
        "<prefix><red>У шахты <white><mine></white> не задан регион."
    ),
    val mineList: MineListSection = MineListSection(),
    val session: SessionSection = SessionSection(),
    val timeFormat: TemporalRegistrySection = TemporalRegistrySection(),
    val help: HelpSection = HelpSection(),
    val refillTeleport: TitleSection = TitleSection(
        title = LocaleComponent(" "),
        subtitle = LocaleComponent("<green>Новые ресурсы уже здесь!")
    )
) {

    val temporalStyle: TextualTemporalStyle by lazy {
        TemporalStyles.createTextual(timeFormat)
    }
}

@Serializable
data class MineListSection(
    val layout: List<@Contextual LocaleComponent> = listOf(
        LocaleComponent(""),
        LocaleComponent("<gradient:#FFD700:#B87333><bold>Доступные шахты</bold></gradient>"),
        LocaleComponent(""),
        LocaleComponent("<mine-entry>"),
        LocaleComponent("")
    ),
    @Contextual
    val entry: LocaleComponent = LocaleComponent(
        "<dark_gray>* <#A7F432><mine></#A7F432> (<mine_is_active>) <green>[<location>]"
    ),
    @Contextual val empty: LocaleComponent = LocaleComponent(
        "<red>На данный момент не существует ни одной шахты."
    ),
    @Contextual val active: LocaleComponent = LocaleComponent(
        "<green>Активна"
    ),
    @Contextual val inactive: LocaleComponent = LocaleComponent(
        "<red>Неактивна"
    )
)

@Serializable
data class SessionSection(
    @Contextual val started: LocaleComponent = LocaleComponent(
        "<prefix><green>Сессия шахты успешно начата."
    ),
    @Contextual val alreadyInSession: LocaleComponent = LocaleComponent(
        "<prefix><yellow>Вы уже находитесь в сессии шахты."
    ),
    @Contextual val noActiveSession: LocaleComponent = LocaleComponent(
        "<prefix><red>У вас нет активной сессии шахты."
    ),
    @Contextual val terminated: LocaleComponent = LocaleComponent(
        "<prefix><yellow>Сессия шахты завершена."
    ),
    @Contextual val invalidStage: LocaleComponent = LocaleComponent(
        "<prefix><red>Этап <white><stage></white> не существует у данного шаблона."
    ),
    @Contextual val regionMinPointSet: LocaleComponent = LocaleComponent(
        "<prefix><green>Минимальная точка региона установлена: <white><location></white>"
    ),
    @Contextual val regionMaxPointSet: LocaleComponent = LocaleComponent(
        "<prefix><green>Максимальная точка региона установлена: <white><location></white>"
    ),
    @Contextual val refillLocationSet: LocaleComponent = LocaleComponent(
        "<prefix><green>Точка телепортации при перезаполении шахты установлена: <white><location></white>"
    ),
    @Contextual val blockAdded: LocaleComponent = LocaleComponent(
        "<prefix><green>Блок <white><material></white> добавлен в шахту."
    ),
    @Contextual val blockNotFound: LocaleComponent = LocaleComponent(
        "<prefix><red>Блок <white><material></white> не найден в шахте."
    ),
    @Contextual val blockRemoved: LocaleComponent = LocaleComponent(
        "<prefix><green>Блок <white><material></white> удалён из шахты."
    ),
    @Contextual val dropAdded: LocaleComponent = LocaleComponent(
        "<prefix><green>Дроп для блока <white><material></white> успешно добавлен."
    ),
    @Contextual val dropNotFound: LocaleComponent = LocaleComponent(
        "<prefix><green>Дроп для блока <white><material></white> не найдет."
    ),
    @Contextual val dropRemoved: LocaleComponent = LocaleComponent(
        "<prefix><green>Дроп <white><material></white> удалён."
    ),
    @Contextual val invalidBlock: LocaleComponent = LocaleComponent(
        "<prefix><red>Данный материал не может быть использован в  шахте."
    ),
    @Contextual val invalidProbability: LocaleComponent = LocaleComponent(
        "<prefix><red>Вероятность должна находиться в диапазоне от <white>0.0</white> до <white>1.0</white>."
    ),
    @Contextual val blockInteractionDenied: LocaleComponent = LocaleComponent(
        "<prefix><red>Взаимодействие с блоками во время сессии редактирования запрещено!"
    ),
    @Contextual val noItemInHand: LocaleComponent = LocaleComponent(
        "<prefix><red>Возьмите предмет дропа в руку."
    ),
    @Contextual val noItemsInInventory: LocaleComponent = LocaleComponent(
        "<prefix><red>В вашем инвентаре нет предметов, которые можно добавить как дроп."
    ),
    @Contextual val noChangesSession: LocaleComponent = LocaleComponent(
        "<prefix><yellow>Изменений в текущей сессии нет."
    ),
    @Contextual val appliedChangesSession: LocaleComponent = LocaleComponent(
        "<prefix><green>Изменения сессии успешно применены."
    )
)

@Serializable
data class HelpSection(
    val layout: List<@Contextual LocaleComponent> = listOf(
        LocaleComponent(""),
        LocaleComponent("<green>/ksmines help <dark_gray>- <white>справка по командам"),
        LocaleComponent("<green>/ksmines reload <dark_gray>- <white>перезагрузить конфигурацию"),
        LocaleComponent("<green>/ksmines list <dark_gray>- <white>список шахт"),
        LocaleComponent("<green>/ksmines refill <key> <dark_gray>- <white>перезаполнить шахту"),
        LocaleComponent("<green>/ksmines rotate <key> <dark_gray>- <white>переключить этап шахты"),
        LocaleComponent("<green>/ksmines remove <key> <dark_gray>- <white>удалить шахту"),
        LocaleComponent("<green>/ksmines create <key> <template> <dark_gray>- <white>создать шахту из шаблона"),
        LocaleComponent("<green>/ksmines session start <key> <stage> <dark_gray>- <white>начать редактирование шахты"),
        LocaleComponent("<green>/ksmines session terminate <dark_gray>- <white>применить и завершить сессию"),
        LocaleComponent("<green>/ksmines addblock <material> <probability> [--drop-by-hand/--drop-by-inventory] <dark_gray>- <white>добавить блок"),
        LocaleComponent("<green>/ksmines adddrop <material> <probability> <dark_gray>- <white>добавить дроп для блока"),
        LocaleComponent("")
    )
)

@Serializable
data class TemporalRegistrySection(
    val day: String = "d",
    val hour: String = "h",
    val minute: String = "m",
    val second: String = "s"
) : TemporalRegistry<String, TemporalEntry> {

    override fun loadStyles(): Map<String, TemporalEntry> =
        mapOf(
            day to entry(TemporalUnit.DAYS, day),
            hour to entry(TemporalUnit.HOURS, hour),
            minute to entry(TemporalUnit.MINUTES, minute),
            second to entry(TemporalUnit.SECONDS, second)
        )

    private fun entry(
        unit: TemporalUnit,
        name: String
    ): TemporalEntry =
        TemporalEntry(
            unit,
            arrayOf(name)
        )
}