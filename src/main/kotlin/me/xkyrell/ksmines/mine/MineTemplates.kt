package me.xkyrell.ksmines.mine

import me.xkyrell.ksmines.util.locale.LocaleComponent
import me.xkyrell.ksmines.config.section.SoundSection
import me.xkyrell.ksmines.mine.block.MineBlockDrop
import me.xkyrell.ksmines.mine.block.MineBlockImpl
import me.xkyrell.temporal.Temporal
import me.xkyrell.temporal.TemporalUnit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

 object MineTemplates {

        val DEFAULTS: MineTemplate = MineTemplate(
            stageTime = Temporal.of(
                5,
                TemporalUnit.MINUTES
            ),
            teleportPlayersOnRefill = false,
            rotationStrategy = MineRotationStrategy.SEQUENTIAL,
            rotationStages = listOf(
                MineStage(
                    displayName = LocaleComponent("<green>Этап 1"),
                    activationNotification = listOf(
                        LocaleComponent("<green>Этап авто-шахты повысился на 1."),
                        LocaleComponent("<green>Вперед к новый ресурсам! Локация: <white><location></white>.")
                    ),
                    activationSound = SoundSection(
                        sound = Sound.BLOCK_NOTE_BLOCK_PLING,
                        volume = 1.0f,
                        pitch = 0.8f
                    ),
                    refillNotification = listOf(
                        LocaleComponent("<yellow>Авто-шахта 1 этапа перезаполнилась."),
                        LocaleComponent("<yellow>Локация: <red><location></red>. Тсс.......")
                    ),
                    refillSound = SoundSection(
                        sound = Sound.BLOCK_AMETHYST_BLOCK_CHIME
                    ),
                    refillTime = Temporal.of(
                        2,
                        TemporalUnit.MINUTES
                    ),
                    blocks = setOf(
                        MineBlockImpl(
                            material = Material.AMETHYST_BLOCK,
                            probability = 0.05,
                            customDrops = setOf(
                                MineBlockDrop(
                                    item = ItemStack(Material.DIAMOND),
                                    probability = 0.30
                                ),
                                MineBlockDrop(
                                    item = ItemStack(Material.BONE_BLOCK, 32),
                                    probability = 0.30
                                ),
                                MineBlockDrop(
                                    item = ItemStack(Material.EMERALD),
                                    probability = 0.30
                                ),
                                MineBlockDrop(
                                    item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 5),
                                    probability = 0.10
                                )
                            )
                        ),
                        MineBlockImpl(
                            material = Material.STONE,
                            probability = 0.65
                        ),
                        MineBlockImpl(
                            material = Material.COAL_ORE,
                            probability = 0.20
                        ),
                        MineBlockImpl(
                            material = Material.IRON_ORE,
                            probability = 0.10
                        )
                    )
                ),
                MineStage(
                    displayName = LocaleComponent("<gold>Этап 2"),
                    activationNotification = listOf(
                        LocaleComponent("<green>Этап авто-шахты повысился на 2."),
                        LocaleComponent("<green>Ожидается нечто крутое! Локация: <gradient:#FF3B30:#8A2BE2><location></gradient>.")
                    ),
                    activationSound = SoundSection(
                        sound = Sound.BLOCK_BEACON_ACTIVATE
                    ),
                    refillNotification = listOf(
                        LocaleComponent("<yellow>Авто-шахта 2 этапа перезаполнилась."),
                        LocaleComponent("<yellow>Локация: <gradient:#FFD700:#B87333><location></gradient>.")
                    ),
                    refillSound = SoundSection(
                        sound = Sound.BLOCK_BEACON_POWER_SELECT,
                        volume = 1.0f,
                        pitch = 1.2f
                    ),
                    refillTime = Temporal.of(
                        45,
                        TemporalUnit.SECONDS
                    ),
                    blocks = setOf(
                        MineBlockImpl(
                            material = Material.BEACON,
                            probability = 0.10,
                            customDrops = setOf(
                                MineBlockDrop(
                                    item = ItemStack(Material.DIAMOND_BLOCK, 2),
                                    probability = 0.40
                                ),
                                MineBlockDrop(
                                    item = ItemStack(Material.GOLDEN_CARROT, 21),
                                    probability = 0.40
                                ),
                                MineBlockDrop(
                                    item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 12),
                                    probability = 0.20
                                )
                            )
                        ),
                        MineBlockImpl(
                            material = Material.DEEPSLATE,
                            probability = 0.50
                        ),
                        MineBlockImpl(
                            material = Material.DIAMOND_ORE,
                            probability = 0.20
                        ),
                        MineBlockImpl(
                            material = Material.GOLD_ORE,
                            probability = 0.20
                        ),
                        MineBlockImpl(
                            material = Material.EMERALD_ORE,
                            probability = 0.10
                        )
                    )
                )
            ),
            region = null,
            refillLocation = null
        )
}