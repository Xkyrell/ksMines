package me.xkyrell.ksmines.config

import kotlinx.serialization.Serializable
import me.xkyrell.ksmines.config.section.LocaleSection
import me.xkyrell.ksmines.util.TemporalStyles
import me.xkyrell.temporal.TemporalUnit
import me.xkyrell.temporal.format.registry.TemporalEntry
import me.xkyrell.temporal.format.registry.TemporalRegistry
import me.xkyrell.temporal.format.style.TextualTemporalStyle

@Serializable
data class LanguageConfig(
    val prefix: LocaleSection = LocaleSection(
        "<#A7F432>ksMines >"
    ),
    val onlyPlayer: LocaleSection = LocaleSection(
        "<prefix><red>Эта команда доступна только игроку."
    ),
    val noPermission: LocaleSection = LocaleSection(
        "<prefix><red>У вас недостаточно прав."
    ),
    val mineNotFound: LocaleSection = LocaleSection(
        "<prefix><red>Шахта <white><mine></white> не найдена."
    ),
    val mineAlreadyExists: LocaleSection = LocaleSection(
        "<prefix><red>Шахта <white><mine></white> уже существует."
    ),
    val mineInactive: LocaleSection = LocaleSection(
        "<prefix><red>Шахта <white><mine></white> сейчас неактивна."
    ),
    val templateNotFound: LocaleSection = LocaleSection(
        "<prefix><red>Шаблон <white><template></white> не найден."
    ),
    val reload: LocaleSection = LocaleSection(
        "<prefix><green>Конфигурация успешно перезагружена."
    ),
    val mineCreated: LocaleSection = LocaleSection(
        "<prefix><green>Шахта <white><mine></white> успешно создана."
    ),
    val mineRemoved: LocaleSection = LocaleSection(
        "<prefix><green>Шахта <white><mine></white> успешно удалена."
    ),
    val mineRefilled: LocaleSection = LocaleSection(
        "<prefix><green>Шахта <white><mine></white> успешно перезаполнена, благодаря <player>."
    ),
    val mineRotated: LocaleSection = LocaleSection(
        "<prefix><green>Шахта <white><mine></white> переключена на следующий этап, благодаря <player>."
    ),
    val mineList: MineListSection = MineListSection(),
    val session: SessionSection = SessionSection(),
    val timeFormat: TemporalRegistrySection,
    val help: HelpSection = HelpSection()
) {

    val temporalStyle: TextualTemporalStyle by lazy {
        TemporalStyles.createTextual(timeFormat)
    }
}

@Serializable
data class MineListSection(
    val layout: List<LocaleSection> = listOf(
        LocaleSection(""),
        LocaleSection("<gradient:#FFD700:#B87333><bold>Доступные шахты</bold></gradient>"),
        LocaleSection(""),
        LocaleSection("<mine-entry>"),
        LocaleSection("")
    ),
    val entry: LocaleSection = LocaleSection(
        "<hover:show_text:'<gray>Нажмите, чтобы телепортироваться'>"
                + "<click:run_command:'/mine teleport <mine>'>"
                + "<dark_gray>* "
                + "<#A7F432><mine></#A7F432> "
                + "<gray>(<mine_is_active>) "
                + "<green>[<location>]"
                + "</click>"
    ),
    val empty: LocaleSection = LocaleSection(
        "<prefix><red>На данный момент не существует ни одной шахты."
    ),
    val active: LocaleSection = LocaleSection(
        "<green>Активна"
    ),
    val inactive: LocaleSection = LocaleSection(
        "<red>Неактивна"
    )
)

@Serializable
data class SessionSection(
    val started: LocaleSection = LocaleSection(
        "<prefix><green>Сессия шахты успешно начата."
    ),
    val alreadyInSession: LocaleSection = LocaleSection(
        "<prefix><yellow>Вы уже находитесь в сессии шахты."
    ),
    val noActiveSession: LocaleSection = LocaleSection(
        "<prefix><red>У вас нет активной сессии шахты."
    ),
    val terminated: LocaleSection = LocaleSection(
        "<prefix><yellow>Сессия шахты завершена."
    ),
    val regionMinPointSet: LocaleSection = LocaleSection(
        "<prefix><green>Минимальная точка региона установлена: <white><location></white>"
    ),
    val regionMaxPointSet: LocaleSection = LocaleSection(
        "<prefix><green>Максимальная точка региона установлена: <white><location></white>"
    ),
    val blockAdded: LocaleSection = LocaleSection(
        "<prefix><green>Блок <white><material></white> добавлен в шахту."
    ),
    val blockNotFound: LocaleSection = LocaleSection(
        "<prefix><red>Блок <white><material></white> не найден."
    ),
    val blockRemoved: LocaleSection = LocaleSection(
        "<prefix><green>Блок <white><material></white> удалён из шахты."
    ),
    val dropAdded: LocaleSection = LocaleSection(
        "<prefix><green>Дроп для блока <white><material></white> успешно добавлен."
    ),
    val invalidProbability: LocaleSection = LocaleSection(
        "<prefix><red>Вероятность должна находиться в диапазоне от <white>0.0</white> до <white>1.0</white>."
    ),
    val blockInteractionDenied: LocaleSection = LocaleSection(
        "<prefix><red>Взаимодействие с блоками во время сессии редактирования запрещено!"
    ),
    val noChangesSession: LocaleSection = LocaleSection(
        "<prefix><yellow>Изменений в текущей сессии нет."
    ),
    val appliedChangesSession: LocaleSection = LocaleSection(
        "<prefix><green>Изменения сессии успешно применены."
    )
)

@Serializable
data class HelpSection(
    val layout: List<LocaleSection> = listOf(
        LocaleSection(""),
        LocaleSection("<green>/ksmines help <dark_gray>- <white>справка по командам"),
        LocaleSection("<green>/ksmines reload <dark_gray>- <white>перезагрузить конфигурацию"),
        LocaleSection("<green>/ksmines list <dark_gray>- <white>список шахт"),
        LocaleSection("<green>/ksmines refill <key> <dark_gray>- <white>перезаполнить шахту"),
        LocaleSection("<green>/ksmines rotate <key> <dark_gray>- <white>переключить этап шахты"),
        LocaleSection("<green>/ksmines remove <key> <dark_gray>- <white>удалить шахту"),
        LocaleSection("<green>/ksmines create <key> <template> <dark_gray>- <white>создать шахту из шаблона"),
        LocaleSection("<green>/ksmines session start <key> <stage> <dark_gray>- <white>начать редактирование шахты"),
        LocaleSection("<green>/ksmines session terminate <dark_gray>- <white>применить и завершить сессию"),
        LocaleSection("<green>/ksmines addblock <material> <probability> [--drop-by-hand/--drop-by-inventory <p>] <dark_gray>- <white>добавить блок"),
        LocaleSection("<green>/ksmines adddrop <material> <probability> <dark_gray>- <white>добавить дроп для блока"),
        LocaleSection("")
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